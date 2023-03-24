package com.software.cafejariapp.presentation.feature.main.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.common.KalendarKonfig
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.theme.Grid
import com.software.cafejariapp.presentation.component.HorizontalSpacer
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.YearMonth


const val DAYS_IN_WEEK = 7
const val ZERO = 0

fun getDays(monthState: MutableState<YearMonth>): List<LocalDate> {
    return mutableListOf<LocalDate>().apply {
        val firstDay = monthState.value.atDay(1)
        val firstSunday = if (firstDay.dayOfWeek == java.time.DayOfWeek.SUNDAY) {
            firstDay
        } else {
            firstDay.minusDays(firstDay.dayOfWeek.value.toLong())
        }
        repeat(6) { weekIndex ->
            (0..6).forEach { dayIndex ->
                add(firstSunday.plusDays((7 * weekIndex + dayIndex).toLong()))
            }
        }
    }
}

fun Int.validateMaxDate(year: Int): Boolean {
    return if (year == 0) {
        true
    } else {
        year > this
    }
}

fun Int.validateMinDate(year: Int): Boolean {
    return if (year == 0) {
        true
    } else {
        year < this
    }
}

fun getTextColor(
    isSelected: Boolean,
    kalendarSelector: KalendarSelector,
    isEvent: Boolean,
): Color {
    return when {
        isEvent -> kalendarSelector.eventTextColor
        else -> when {
            isSelected -> kalendarSelector.selectedTextColor
            else -> kalendarSelector.defaultTextColor
        }
    }
}

@Composable
fun KalendarWeekDayNames(kalendarKonfig: KalendarKonfig) {
    val weekdays = DateFormatSymbols(kalendarKonfig.locale).weekdays.takeLast(DAYS_IN_WEEK)

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val width = (maxWidth / DAYS_IN_WEEK)
        Row(modifier = Modifier.fillMaxWidth()) {
            weekdays.forEach { weekDay: String ->
                KalendarWeekDay(
                    modifier = Modifier
                        .requiredWidth(width)
                        .wrapContentHeight(),
                    text = weekDay.subSequence(ZERO, kalendarKonfig.weekCharacters).toString()
                )
            }
        }
    }
}

@Composable
fun KalendarWeekDay(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, modifier = Modifier.alpha(0.5F), style = MaterialTheme.typography.body2)
    }
}

@Composable
fun KalendarHeader(
    text: String,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    kalendarSelector: KalendarSelector,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        KalendarButton(
            kalendarSelector = kalendarSelector,
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Previous Month",
            onClick = onPreviousMonthClick
        )
        HorizontalSpacer(width = 30.dp)
        Text(
            modifier = Modifier.padding(Grid.Two),
            style = MaterialTheme.typography.subtitle1,
            text = text,
            textAlign = TextAlign.Center,
        )
        HorizontalSpacer(width = 30.dp)
        KalendarButton(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Next Month",
            onClick = onNextMonthClick,
            kalendarSelector = kalendarSelector
        )
    }
}

@Composable
fun KalendarButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    kalendarSelector: KalendarSelector,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(Grid.Two)
            .clip(RoundedCornerShape(50))
            .background(color = Color.White)
    ) {
        Icon(
            modifier = Modifier.padding(Grid.Zero),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = Color.Black
        )
    }
}

@Composable
fun KalendarEmptyDay(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = " ",
        maxLines = 1,
        textAlign = TextAlign.Center
    )
}