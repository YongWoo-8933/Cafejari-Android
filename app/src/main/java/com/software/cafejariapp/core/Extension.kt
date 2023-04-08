package com.software.cafejariapp.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.software.cafejariapp.data.util.error.ErrorType
import com.software.cafejariapp.presentation.util.Crowded
import com.software.cafejariapp.data.util.CustomJson
import com.software.cafejariapp.data.util.error.ErrorResponse
import com.software.cafejariapp.data.util.error.RegisterErrorResponse
import com.software.cafejariapp.data.util.error.TokenInvalidResponse
import com.software.cafejariapp.presentation.GlobalState
import com.software.cafejariapp.presentation.feature.map.event.MapEvent
import com.software.cafejariapp.presentation.util.AdId
import io.ktor.client.features.*
import io.ktor.client.statement.*
import io.ktor.util.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * 띄어쓰기를 비슷한 unicode로 변환
 * 이를 통해 문장 양쪽정렬 효과
 */
fun String?.useNonBreakingSpace(): String {
    val space = ' '
    val nonBreakableSpaceUnicode = '\u00A0'
    return this.orEmpty().replace(space, nonBreakableSpaceUnicode)
}


/**
 * 정수값을 층 string으로 변환
 * ex) -1 -> "B층"
 */
fun Int.toFloor(): String {
    return if (this > 0) this.toString() else "B${abs(this)}"
}


/**
 * 정수값을 층 Crowded로 변환
 * 0~4 이외 정수일 경우 crowdedNegative 반환
 */
fun Int.toCrowded(): Crowded {
    return when (this) {
        4 -> Crowded.crowded4
        3 -> Crowded.crowded3
        2 -> Crowded.crowded2
        1 -> Crowded.crowded1
        0 -> Crowded.crowded0
        else -> Crowded.crowdedNegative
    }
}


/**
 * 키보드 바깥화면 터치시 키보드 해제
 */
fun Modifier.addFocusCleaner(
    focusManager: FocusManager, doOnClear: () -> Unit = {}
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}


/**
 * context -> activity 변환
 */
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}


/**
 * context로부터 Fine, coarse 위치권한 혀용 여부 반환
 */
fun Context.isLocationTrackingPermitted(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}


/**
 * context로부터 외부 저장소 접근권한 혀용 여부 반환
 */
fun Context.isReadExternalStoragePermitted(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this, Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}


/**
 * 인터넷 사용 가능 여부 반환
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}


/**
 * 타겟 lat, lng이 내 위치 주변에 있는지 반환
 * 주변: 위도 +- 0.000275, 경도 +- 0.00034범위
 */
fun Location.isNearBy(latitude: Double, longitude: Double): Boolean {
    return this.latitude > latitude - 0.000275 && this.latitude < latitude + 0.000275 && this.longitude > longitude - 0.00034 && this.longitude < longitude + 0.00034 //    return true
}


/**
 * 타겟 lat, lng이 내 위치로부터
 * 몇 m 떨어져있는지 int로 반환
 */
fun Location?.getDistance(latitude: Double, longitude: Double): Int {
    return if (this == null) {
        -1
    } else {
        val earthRadius = 6371000.0
        val rad = Math.PI / 180
        var distance = sin(rad * latitude) * sin(rad * this.latitude)
        distance += cos(rad * latitude) * cos(rad * this.latitude) * cos(rad * (longitude - this.longitude))

        val ret = earthRadius * acos(distance)

        ret.toInt()
    }
}


/**
 * Throwable이 어떤 exception인지 반환
 */
suspend fun Throwable.getException(): Exception {
    return when (this) {
        is UnknownHostException -> CustomException(ErrorType.InternetConnection)
        is UnresolvedAddressException -> CustomException(ErrorType.InternetConnection)
        is ClientRequestException -> {
            when (val errorType = this.response.getErrorType()) {
                is ErrorType.TokenExpired -> TokenExpiredException()
                is ErrorType.MasterExpired -> MasterExpiredException()
                else -> CustomException(errorType)
            }
        }
        is ServerResponseException -> CustomException(ErrorType.InternalServer)
        is HttpRequestTimeoutException -> CustomException(ErrorType.TimeOut)
        is CancellationException -> CustomException(ErrorType.Canceled)
        else -> CustomException(ErrorType.Unknown)
    }
}


/**
 * client error가 어떤 error type인지 반환
 */
suspend fun HttpResponse.getErrorType(): ErrorType {
    val resText = this.readText(Charsets.UTF_8)
    val errorResponse = CustomJson.customJson.decodeFromString(ErrorResponse.serializer(), resText)
    val tokenInvalidResponse =
        CustomJson.customJson.decodeFromString(TokenInvalidResponse.serializer(), resText)
    val registerErrorResponse =
        CustomJson.customJson.decodeFromString(RegisterErrorResponse.serializer(), resText)

    val errorExistence = errorResponse.error_code != null
    val tokenInvalidErrorExistence =
        tokenInvalidResponse.code != null && tokenInvalidResponse.code == "token_not_valid"
    val registerErrorExistence =
        registerErrorResponse.email != null || registerErrorResponse.non_field_errors != null || registerErrorResponse.password1 != null || registerErrorResponse.nickname != null || registerErrorResponse.phone_number != null

    return when {
        errorExistence -> {
            when (errorResponse.error_code) {
                0 -> ErrorType.ErrorMessage("요청에 필요한 필수정보(${errorResponse.detail})가 누락되었습니다")
                1 -> ErrorType.ErrorMessage("요청에 해당하는 ${errorResponse.detail}(이)가 없습니다")
                2 -> ErrorType.ErrorMessage("요청을 수행할 권한이 없습니다")

                100 -> ErrorType.ErrorMessage("마스터 활동중인 카페입니다")
                101 -> ErrorType.MasterExpired
                102 -> ErrorType.ErrorMessage("마스터 활동이 불가능한 시간입니다. (${errorResponse.detail})")

                200 -> ErrorType.ErrorMessage("자신의 활동에는 좋아요를 누를 수 없습니다")
                201 -> ErrorType.ErrorMessage("이미 좋아요를 누른 대상입니다")
                202 -> ErrorType.ErrorMessage("해당 대상에 너무많은 좋아요를 눌렀습니다. 다른 활동을 추천해주세요!")

                400 -> ErrorType.ErrorMessage("구글 로그인 오류: ${errorResponse.detail}")
                401 -> ErrorType.ErrorMessage("해당 이메일은 ${errorResponse.detail}(으)로 가입되어있습니다")
                402 -> ErrorType.ErrorMessage("해당 카카오 계정에 등록된 이메일이 없습니다. 이메일 등록후 진행해주세요")
                403 -> ErrorType.ErrorMessage("이미 인증에 사용된 번호입니다")
                404 -> ErrorType.ErrorMessage("번호인증을 먼저 진행해주세요")
                405 -> ErrorType.ErrorMessage("인증번호 발송 후 3분이 초과되었습니다. 다시 시도해주세요")
                406 -> ErrorType.ErrorMessage("인증번호가 일치하지 않습니다")
                407 -> ErrorType.ErrorMessage("인증번호 전송에 실패했습니다")
                408 -> ErrorType.ErrorMessage("소셜계정으로 가입된 번호입니다. ${errorResponse.detail} 로그인으로 진행해주세요")
                409 -> ErrorType.ErrorMessage("해당 번호와 일치하는 유저 정보가 없습니다")
                410 -> ErrorType.ErrorMessage("비밀번호 변경 정보가 일치하지 않습니다")
                411 -> ErrorType.ErrorMessage("해당 닉네임을 가진 유저가 존재하지 않습니다")

                else -> ErrorType.Unknown
            }
        }
        registerErrorExistence -> {
            var message = ""
            val (non_field_errors, email, password1, nickname, phone_number) = registerErrorResponse
            listOf(non_field_errors, email, password1, nickname, phone_number).forEach { list ->
                if (list != null && list.isNotEmpty()) list.forEach { message += it + "\n" }
            }
            ErrorType.ErrorMessage(message)
        }
        tokenInvalidErrorExistence -> ErrorType.TokenExpired
        else -> ErrorType.Unknown
    }
}

