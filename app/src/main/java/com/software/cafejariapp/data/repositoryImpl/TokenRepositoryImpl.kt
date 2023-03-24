package com.software.cafejariapp.data.repositoryImpl


import com.software.cafejariapp.data.source.remote.HttpRoutes
import com.software.cafejariapp.core.getException
import com.software.cafejariapp.data.source.local.dao.TokenDao
import com.software.cafejariapp.data.source.remote.dto.TokenRefreshRequest
import com.software.cafejariapp.data.source.remote.dto.TokenRefreshResponse
import com.software.cafejariapp.data.source.remote.dto.TokenVerifyRequest
import com.software.cafejariapp.domain.repository.TokenRepository
import com.software.cafejariapp.domain.entity.RefreshToken
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class TokenRepositoryImpl(
    private val dao: TokenDao, private val client: HttpClient
) : TokenRepository {

    override suspend fun getTokenFromRoom(): RefreshToken {
        return dao.getToken() ?: RefreshToken.empty
    }

    override suspend fun insertTokenToRoom(token: RefreshToken) {
        return dao.insertToken(token)
    }

    override suspend fun getTokenValidity(tokenVerifyRequest: TokenVerifyRequest): Boolean {
        return try {
            client.post<HttpResponse> {
                url(HttpRoutes.VERIFY)
                body = tokenVerifyRequest
            }
            true
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }

    override suspend fun getAccessTokenFromServer(tokenRefreshRequest: TokenRefreshRequest): TokenRefreshResponse {
        return try {
            client.post {
                url(HttpRoutes.REFRESH)
                body = tokenRefreshRequest
            }
        } catch (throwable: Throwable) {
            throw throwable.getException()
        }
    }
}