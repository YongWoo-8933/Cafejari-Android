package com.software.cafejariapp.domain.repository

import com.software.cafejariapp.data.source.remote.dto.TokenRefreshRequest
import com.software.cafejariapp.data.source.remote.dto.TokenRefreshResponse
import com.software.cafejariapp.data.source.remote.dto.TokenVerifyRequest
import com.software.cafejariapp.domain.entity.RefreshToken


interface TokenRepository {

    suspend fun getTokenFromRoom(): RefreshToken

    suspend fun insertTokenToRoom(token: RefreshToken)

    suspend fun getTokenValidity(tokenVerifyRequest: TokenVerifyRequest): Boolean

    suspend fun getAccessTokenFromServer(tokenRefreshRequest: TokenRefreshRequest): TokenRefreshResponse
}
