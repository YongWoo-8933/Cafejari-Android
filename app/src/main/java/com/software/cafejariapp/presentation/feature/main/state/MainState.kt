package com.software.cafejariapp.presentation.feature.main.state

import com.software.cafejariapp.domain.entity.Event
import com.software.cafejariapp.domain.entity.InquiryCafe
import com.software.cafejariapp.domain.entity.InquiryEtc
import com.software.cafejariapp.domain.entity.Paragraph

data class MainState (
    // faq
    val isFaqsLoading: Boolean = false,
    val faqs: List<Paragraph> = emptyList(),

    // promotion
    val events: List<Event> = emptyList(),
    val expiredEvents: List<Event> = emptyList(),
    val isEventsLoading: Boolean = false,

    // inquiry etc
    val inquiryEtcs: List<InquiryEtc> = emptyList(),
    val isInquiryEtcLoading: Boolean = true,

    // inquiry cafe
    val inquiryCafes: List<InquiryCafe> = emptyList(),
    val isInquiryCafeLoading: Boolean = true
)