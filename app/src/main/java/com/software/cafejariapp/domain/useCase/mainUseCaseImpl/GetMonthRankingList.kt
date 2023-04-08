package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.Ranker
import com.software.cafejariapp.domain.entity.AccessToken


class GetMonthRankingList(
    private val mainRepository: MainRepository,
) {
    suspend operator fun invoke(
        accessToken: AccessToken,
    ): List<Ranker> {
        return try {
            val rankingList = mainRepository.getMonthRankingList(accessToken.value)
                .sortedBy { it.activity }
                .reversed()
            val response = mutableListOf<Ranker>()
            rankingList.forEach { ranking ->
                response.add(
                    Ranker(
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