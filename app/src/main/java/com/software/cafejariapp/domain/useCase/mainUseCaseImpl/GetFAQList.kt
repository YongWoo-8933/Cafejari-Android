package com.software.cafejariapp.domain.useCase.mainUseCaseImpl

import com.software.cafejariapp.core.CustomException
import com.software.cafejariapp.domain.repository.MainRepository
import com.software.cafejariapp.domain.entity.Paragraph


class GetFAQList(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(): List<Paragraph> {
        return try {
            val faqs = mainRepository.getFAQs()
            val response = mutableListOf<Paragraph>()
            faqs.forEach { faqs ->
                response.add(
                    Paragraph(
                        order = faqs.order,
                        sub_title = faqs.question,
                        sub_content = faqs.answer
                    )
                )
            }
            response
        } catch (e: CustomException) {
            throw e
        }
    }
}