package com.software.cafejariapp.presentation.feature.main.component/*
* MIT License
*
* Copyright (c) 2022 Himanshu Singh
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.himanshoe.kalendar.common.KalendarKonfig
import com.himanshoe.kalendar.common.KalendarStyle
import com.himanshoe.kalendar.common.data.KalendarEvent
import com.himanshoe.kalendar.common.theme.Grid
import com.himanshoe.kalendar.common.theme.KalendarShape
import com.himanshoe.kalendar.common.theme.KalendarTheme
import java.time.LocalDate
import java.time.YearMonth

/**
 * [KalendarType] is used to distinguish the type of calendar
 * [Firey] represents the Monthly View
 * [Oceanic] represents the Week View
 */

/**
 * [Kalendar] is exposed to be used as composable
 * @param kalendarType is the type of calendar.See [KalendarType]
 * @param kalendarStyle sets the style of calendar.See [KalendarStyle]
 * @param kalendarKonfig is user to setup config needed for rendering calendar.See [KalendarKonfig]
 * @param selectedDay is representation for selected marker.
 * @param onCurrentDayClick gives the day click listener
 * @param errorMessage gives the error message if any
 */

@Composable
fun CustomKalendar(
    kalendarKonfig: KalendarKonfig = KalendarKonfig(),
    kalendarStyle: KalendarStyle = KalendarStyle(),
    selectedDay: LocalDate = LocalDate.now(),
    kalendarEvents: List<KalendarEvent> = emptyList(),
    onCurrentDayClick: (LocalDate, KalendarEvent?) -> Unit,
    errorMessage: (String) -> Unit = {},
) {

    val shape = if (kalendarStyle.hasRadius) {
        KalendarShape.SelectedShape
    } else {
        KalendarShape.DefaultRectangle
    }

    KalendarTheme {

        Card(
            shape = shape,
            elevation = kalendarStyle.elevation,
            backgroundColor = kalendarStyle.kalendarBackgroundColor ?: Color.White,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Grid.OneHalf)
            ) {

                CustomKalendarMonth(
                    selectedDay = selectedDay,
                    yearMonth = YearMonth.now(),
                    kalendarKonfig = kalendarKonfig,
                    onCurrentDayClick = onCurrentDayClick,
                    errorMessageLogged = errorMessage,
                    kalendarSelector = kalendarStyle.kalendarSelector,
                    kalendarEvents = kalendarEvents
                )
            }
        }
    }
}
