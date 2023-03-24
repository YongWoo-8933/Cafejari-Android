package com.software.cafejariapp.domain.useCase

import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.GetAccessToken
import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.GetSavedRefreshToken
import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.UpdateSavedRefreshToken
import com.software.cafejariapp.domain.useCase.tokenUseCaseImpl.VerifyToken

data class TokenUseCase(
    val getSavedRefreshToken: GetSavedRefreshToken,
    val updateSavedRefreshToken: UpdateSavedRefreshToken,
    val verifyToken: VerifyToken,
    val getAccessToken: GetAccessToken,
)
