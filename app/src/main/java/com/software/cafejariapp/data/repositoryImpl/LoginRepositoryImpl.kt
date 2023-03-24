package com.software.cafejariapp.data.repositoryImpl

import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.core.getException
import com.software.cafejariapp.data.source.local.dao.LoginDao
import com.software.cafejariapp.data.source.remote.dto.*
import com.software.cafejariapp.domain.repository.LoginRepository
import com.software.cafejariapp.domain.entity.Login
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.File

class LoginRepositoryImpl(
    private val dao: LoginDao,
    private val client: HttpClient,
    private val multiPartFormDataClient: HttpClient
) : LoginRepository {

    override suspend fun getLoginsFromRoom(): List<Login> {
        return dao.getLogins()
    }

    override suspend fun insertLoginToRoom(login: Login) {
        return dao.insertLogin(login)
    }

    override suspend fun deleteLoginFromRoom(login: Login) {
        return dao.deleteLogin(login)
    }

    override suspend fun login(loginRequest: CafejariLoginRequest): LoginResponse {
        return try {
            client.post {
                url(HttpRoutes.LOGIN)
                body = loginRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun socialLogin(
        url: String, socialLoginRequest: SocialLoginRequest
    ): SocialLoginResponse {
        return try {
            client.post {
                url(url)
                body = socialLoginRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun googleLogin(googleLoginRequest: GoogleLoginRequest): GoogleLoginResponse {
        return try {
            client.post {
                url(HttpRoutes.GOOGLE_LOGIN)
                body = googleLoginRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun kakaoLogin(kakaoLoginRequest: KakaoLoginRequest): KakaoLoginResponse {
        return try {
            client.post {
                url(HttpRoutes.KAKAO_LOGIN)
                body = kakaoLoginRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun logout(logoutRequest: LogoutRequest) {
        try {
            client.post<HttpResponse> {
                url(HttpRoutes.LOGOUT)
                body = logoutRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getUser(accessToken: String): UserResponse {
        return try {
            client.get {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                url(HttpRoutes.GET_USER)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun makeNewProfile(
        accessToken: String, makeNewProfileRequest: MakeNewProfileRequest
    ): UserResponse {
        return try {
            client.post {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                url(HttpRoutes.MAKE_NEW_PROFILE)
                body = makeNewProfileRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun preRegistration(registerRequest: RegisterRequest): PreRegisterResponse {
        return try {
            client.post {
                url(HttpRoutes.PRE_REGISTRATION)
                body = registerRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun registration(registerRequest: RegisterRequest): RegisterResponse {
        return try {
            client.post {
                url(HttpRoutes.REGISTRATION)
                body = registerRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun smsSend(smsSendRequest: SmsSendRequest) {
        try {
            client.post<HttpResponse> {
                url(HttpRoutes.SMS_SEND)
                body = smsSendRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun resetSmsSend(smsSendRequest: SmsSendRequest) {
        try {
            client.post<HttpResponse> {
                url(HttpRoutes.RESET_SMS_SEND)
                body = smsSendRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun smsAuth(smsAuthRequest: SmsAuthRequest) {
        try {
            client.post<HttpResponse> {
                url(HttpRoutes.SMS_AUTH)
                body = smsAuthRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun resetSmsAuth(smsAuthRequest: SmsAuthRequest): ResetSmsAuthResponse {
        return try {
            client.post {
                url(HttpRoutes.RESET_SMS_AUTH)
                body = smsAuthRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        try {
            client.post<HttpResponse> {
                url(HttpRoutes.RESET_PASSWORD)
                body = resetPasswordRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun preRecommendation(preRecommendationDto: RecommendationDto): RecommendationDto {
        return try {
            client.post {
                url(HttpRoutes.PRE_RECOMMENDATION)
                body = preRecommendationDto
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun recommendation(
        accessToken: String,
        recommendationDto: RecommendationDto
    ): UserResponse {
        return try {
            client.post {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                url(HttpRoutes.RECOMMENDATION)
                body = recommendationDto
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun preAuthorization(preAuthorizeDto: PreAuthorizeDto): PreAuthorizeDto {
        return try {
            client.post {
                url(HttpRoutes.PRE_AUTHORIZATION)
                body = preAuthorizeDto
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun authorization(
        accessToken: String, profileId: Int, authRequest: AuthRequest
    ): UserResponse {
        return try {
            client.post {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                url(HttpRoutes.authorization(profileId))
                body = authRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun updateFcmToken(
        accessToken: String, profile_id: Int, updateFcmTokenRequest: UpdateFcmTokenRequest
    ): UserResponse {
        return try {
            client.put {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                url(HttpRoutes.updateProfile(profile_id))
                body = updateFcmTokenRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun updateProfile(
        accessToken: String, profile_id: Int, image_path: String?, nickname: String?
    ): UserResponse {

        return try {
            multiPartFormDataClient.put(HttpRoutes.updateProfile(profile_id)) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                    append(HttpHeaders.Accept, "*/*")
                }
                body = MultiPartFormDataContent(formData {
                    if (nickname != null) {
                        append("nickname", nickname)
                    }
                    if (image_path != null) {
                        append("image", File(image_path).readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/jpg")
                            append(HttpHeaders.ContentDisposition, "filename=${image_path}")
                        })
                    }
                })
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getSocialUserType(accessToken: String): SocialUserCheckResponse {
        return try {
            client.get {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                url(HttpRoutes.SOCIAL_USER_CHECK)
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }
}