package com.software.cafejariapp.di

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.kakao.sdk.common.KakaoSdk
import com.software.cafejariapp.BuildConfig
import com.software.cafejariapp.data.util.CustomJson
import com.software.cafejariapp.data.source.local.db.BaseDatabase
import com.software.cafejariapp.data.source.local.db.migration_1_2
import com.software.cafejariapp.data.repositoryImpl.LoginRepositoryImpl
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.useCase.LoginUseCase
import com.software.cafejariapp.data.repositoryImpl.MainRepositoryImpl
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.useCase.MainUseCase
import com.software.cafejariapp.data.repositoryImpl.CafeRepositoryImpl
import com.software.cafejariapp.domain.repository.CafeRepository
import com.software.cafejariapp.domain.useCase.CafeUseCase
import com.software.cafejariapp.data.repositoryImpl.TokenRepositoryImpl
import com.software.cafejariapp.data.source.local.db.migration_2_3
import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.useCase.TokenUseCase
import com.software.cafejariapp.domain.useCase.cafeUseCaseImpl.*
import com.software.cafejariapp.domain.useCase.loginUseCaseImpl.*
import com.software.cafejariapp.domain.useCase.mainUseCaseImpl.*
import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.GetAccessToken
import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.GetSavedRefreshToken
import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.UpdateSavedRefreshToken
import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.VerifyToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // DB, Client

    @Provides
    @Singleton
    fun provideBaseDatabase(app: Application): BaseDatabase {
        return Room.databaseBuilder(
            app,
            BaseDatabase::class.java,
            BaseDatabase.DATABASE_NAME
        )
            .addMigrations(migration_1_2, migration_2_3)
            .build()
    }

    @Provides
    @Singleton
    fun provideHTTPClient(): HttpClient {
        return HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(CustomJson.customJson)
            }

            install(DefaultRequest) {
                header("Accept", "application/json")
                header("Content-type", "application/json")
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 12_000L
                connectTimeoutMillis = 12_000L
                socketTimeoutMillis = 12_000L
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    @Provides
    @Singleton
    fun provideGoogleClient(app: Application): GoogleSignInClient {
        val context = app.applicationContext
        KakaoSdk.init(context, BuildConfig.kakaonativeappkey)

        val signInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.googleserverclientid, true)
            .requestIdToken(BuildConfig.googleserverclientid)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, signInOption)
    }


    @Provides
    @Singleton
    fun providePlaceClient(app: Application): PlacesClient {
        val context = app.applicationContext
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.googleandroidapikey)
        }
        return Places.createClient(context)
    }

    // Repository

    @Provides
    @Singleton
    fun provideTokenRepository(db: BaseDatabase, client: HttpClient): TokenRepository {
        return TokenRepositoryImpl(db.tokenDao, client)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(db: BaseDatabase, client: HttpClient): LoginRepository {
        val multipartFormDataClient = HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(CustomJson.customJson)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 12_000L
                connectTimeoutMillis = 12_000L
                socketTimeoutMillis = 12_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {

                    }
                }
                level = LogLevel.ALL
            }
        }
        return LoginRepositoryImpl(db.loginDao, client, multipartFormDataClient)
    }

    @Provides
    @Singleton
    fun provideMainRepository(db: BaseDatabase, client: HttpClient): MainRepository {
        return MainRepositoryImpl(db.disableDateDao, client)
    }

    @Provides
    @Singleton
    fun provideCafeRepository(client: HttpClient): CafeRepository {
        return CafeRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideMainUseCase(mainRepository: MainRepository): MainUseCase {
        return MainUseCase(
            getVersion = GetVersion(mainRepository),
            getEventList = GetEventList(mainRepository),
            getEventPointHistoryList = GetEventPointHistoryList(mainRepository),
            getItemList = GetItemList(mainRepository),
            getPurchaseRequestList = GetPurchaseRequestList(mainRepository),
            getFAQList = GetFAQList(mainRepository),
            getMyInquiryCafes = GetMyInquiryCafes(mainRepository),
            getMyInquiryEtcs = GetMyInquiryEtcs(mainRepository),
            isOnboardingWatched = IsOnboardingWatched(mainRepository),
            isTodayExecutable = IsTodayExecutable(mainRepository),
            setTodayDisable = SetTodayDisable(mainRepository),
            submitInquiryCafe = SubmitInquiryCafe(mainRepository),
            submitInquiryEtc = SubmitInquiryEtc(mainRepository),
            submitInquiryCafeAdditionalInfo = SubmitInquiryCafeAdditionalInfo(mainRepository),
            submitPurchaseRequest = SubmitPurchaseRequest(mainRepository),
            deletePurchaseRequest = DeletePurchaseRequest(mainRepository),
            deleteInquiryCafe = DeleteInquiryCafe(mainRepository),
            deleteInquiryEtc = DeleteInquiryEtc(mainRepository),
            getTotalLeaders = GetTotalLeaders(mainRepository),
            getMonthLeaders = GetMonthLeaders(mainRepository),
            getWeekLeaders = GetWeekLeaders(mainRepository),
            getPopUpNotificationList = GetPopUpNotificationList(mainRepository),
            getOnSaleCafeList = GetOnSaleCafeList(mainRepository),
        )
    }

    @Provides
    @Singleton
    fun provideTokenUseCase(tokenRepository: TokenRepository): TokenUseCase {
        return TokenUseCase(
            getSavedRefreshToken = GetSavedRefreshToken(tokenRepository),
            updateSavedRefreshToken = UpdateSavedRefreshToken(tokenRepository),
            verifyToken = VerifyToken(tokenRepository),
            getAccessToken = GetAccessToken(tokenRepository)
        )
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(
        loginRepository: LoginRepository, tokenRepository: TokenRepository
    ): LoginUseCase {
        return LoginUseCase(
            cafeJariLogin = CafeJariLogin(loginRepository, tokenRepository),
            googleLogin = GoogleLogin(loginRepository),
            googleLoginFinish = GoogleLoginFinish(loginRepository, tokenRepository),
            kakaoLogin = KakaoLogin(loginRepository),
            kakaoLoginFinish = KakaoLoginFinish(loginRepository, tokenRepository),
            logout = Logout(loginRepository),
            getPredictions = GetPredictions(loginRepository),
            deleteLoginInfo = DeleteLoginInfo(loginRepository),
            getUser = GetUser(loginRepository),
            verifyEmailPassword = VerifyEmailPassword(loginRepository),
            verifyNicknamePhoneNumber = VerifyNicknamePhoneNumber(loginRepository),
            smsSend = SmsSend(loginRepository),
            resetSmsSend = ResetSmsSend(loginRepository),
            smsAuth = SmsAuth(loginRepository),
            resetSmsAuth = ResetSmsAuth(loginRepository),
            resetPassword = ResetPassword(loginRepository),
            recommend = Recommend(loginRepository),
            authorize = Authorize(loginRepository, tokenRepository),
            verifyRecommendationNickname = VerifyRecommendationNickname(loginRepository),
            makeNewProfile = MakeNewProfile(loginRepository),
            updateFcmToken = UpdateFcmToken(loginRepository),
            updateProfile = UpdateProfile(loginRepository),
            getSocialUserType = GetSocialUserType(loginRepository)
        )
    }

    @Provides
    @Singleton
    fun provideCafeUseCase(
        cafeRepository: CafeRepository
    ): CafeUseCase {
        return CafeUseCase(
            getCafeInfoList = GetCafeInfoList(cafeRepository),
            registerMaster = RegisterMaster(cafeRepository),
            updateCrowded = UpdateCrowded(cafeRepository),
            expireMaster = ExpireMaster(cafeRepository),
            addAdPoint = AddAdPoint(cafeRepository),
            getMyUnExpiredCafeLog = GetMyUnExpiredCafeLog(cafeRepository),
            getMyCafeLogList = GetMyCafeLogList(cafeRepository),
            deleteCafeDetailLog = DeleteCafeDetailLog(cafeRepository),
            getAutoExpiredCafeLog = GetAutoExpiredCafeLog(cafeRepository),
            deleteAutoExpiredCafeLog = DeleteAutoExpiredCafeLog(cafeRepository),
            requestThumbsUp = RequestThumbsUp(cafeRepository),
            search = Search(cafeRepository)
        )
    }
}
