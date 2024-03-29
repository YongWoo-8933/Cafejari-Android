package com.software.cafejariapp.presentation.feature.main.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.common.KalendarKonfig
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.data.KalendarEvent
import com.software.cafejariapp.presentation.feature.main.util.*
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun CustomKalendarMonth(
    selectedDay: LocalDate,
    yearMonth: YearMonth = YearMonth.now(),
    kalendarKonfig: KalendarKonfig,
    onCurrentDayClick: (LocalDate, KalendarEvent?) -> Unit,
    errorMessageLogged: (String) -> Unit,
    kalendarSelector: KalendarSelector,
    kalendarEvents: List<KalendarEvent>,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {

        val haptic = LocalHapticFeedback.current
        val monthState = remember { mutableStateOf(yearMonth) }
        val clickedDay = remember { mutableStateOf(selectedDay) }

        KalendarHeader(
            kalendarSelector = kalendarSelector,
            text = "${monthState.value.year}년  ${monthState.value.monthValue}월",
            onPreviousMonthClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val year = monthState.value.year
                val isLimitAttached = year.validateMinDate(kalendarKonfig.yearRange.min)
                if (isLimitAttached) {
                    monthState.value = monthState.value.minusMonths(1)
                } else {
                    errorMessageLogged("Minimum year limit reached")
                }
            },
            onNextMonthClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val year = monthState.value.year
                val isLimitAttached = year.validateMaxDate(kalendarKonfig.yearRange.max)
                if (isLimitAttached) {
                    monthState.value = monthState.value.plusMonths(1)
                } else {
                    errorMessageLogged("Minimum year limit reached")
                }
            },
        )

        KalendarWeekDayNames(kalendarKonfig = kalendarKonfig)

        val days: List<LocalDate> = getDays(monthState)

        days.chunked(DAYS_IN_WEEK).forEach { weekDays ->
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {

                val size = (maxWidth / DAYS_IN_WEEK)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    weekDays.forEach { localDate ->
                        val isFromCurrentMonth = YearMonth.from(localDate) == monthState.value
                        if (isFromCurrentMonth) {
                            val isSelected =
                                monthState.value.month == clickedDay.value.month && monthState.value.year == clickedDay.value.year && localDate == clickedDay.value

                            CustomKalendarDay(
                                size = size,
                                date = localDate,
                                isSelected = isSelected,
                                isToday = localDate == LocalDate.now(),
                                kalendarSelector = kalendarSelector,
                                kalendarEvents = kalendarEvents,
                                onDayClick = { date, event ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    clickedDay.value = date
                                    onCurrentDayClick(date, event)
                                }
                            )
                        } else {
                            KalendarEmptyDay(modifier = Modifier.size(size))
                        }
                    }
                }
            }
        }
    }
}