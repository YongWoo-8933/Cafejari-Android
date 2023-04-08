package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.Leader
import com.software.cafejariapp.domain.entity.AccessToken


class GetMonthLeaders(
    private val mainRepository: MainRepository,
) {
    suspend operator fun invoke(
        accessToken: AccessToken,
    ): List<Leader> {
        return try {
            val rankingList = mainRepository.getMonthRankingList(accessToken.value)
                .sortedBy { it.activity }
                .reversed()
            val response = mutableListOf<Leader>()
            rankingList.forEach { ranking ->
                response.add(
                    Leader(
                        nickname = ranking.user.profile.nickname,
                        activity = ranking.activity,
                        ranking = ranking.ranking,
                        userImage = ranking.user.profile.image,
                    )
                )
            }
            response
        } catch (e: CustomException) {
            throw e
        }
    }
}