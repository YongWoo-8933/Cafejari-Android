package com.software.cafejariapp.presentation.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 시간 계산과 관련된 모든 함수 모음
 *
 * getYearMonthDay -> "2023년 2월 3일",
 * getHourMinute -> "16:46",
 * getKoreanHourMinute -> "16시 46분",
 * getSecondFrom -> 31,
 * getHourMinuteFromSecond -> "12시간 15분",
 * getPassingTimeFrom -> "12시간 15분",
 * getPassingTimeFromTo -> "12시간 15분",
 * getLocalDate LocalDate -> 타입,
 * getAccessTokenExpiration -> String,
 * isPast -> boolean
 */

object Time {

    private fun getLocalDateTimeFrom(at: String): LocalDateTime {
        return LocalDateTime.of(
            ( at.substring(0 until 4) ).toInt(),
            ( at.substring(5 until 7) ).toInt(),
            ( at.substring(8 until 10) ).toInt(),
            ( at.substring(11 until 13) ).toInt(),
            ( at.substring(14 until 16) ).toInt(),
            ( at.substring(17 until 19) ).toInt()
        )
    }

    fun getYearMonthDay(at: String): String {
        val year = at.substring(0..3)
        val month = at.substring(5..6)
        val day = at.substring(8..9)
        return try {
            "${year}년 ${
                if (month.substring(0..0) != "0") month else month.substring(1..1)
            }월 ${
                if (day.substring(0..0) != "0") day else day.substring(1..1)
            }일"
        } catch (e: IndexOutOfBoundsException) {
            ""
        }
    }

    fun getHourMinute(at: String): String {
        return try {
            at.substring(11..15)
        } catch (e: IndexOutOfBoundsException) {
            ""
        }
    }

    fun getKoreanHourMinute(at: String): String {
        val hour = at.substring(11..12)
        val minutes = at.substring(14..15)
        return "${hour}시 ${minutes}분"
    }

    fun getSecondFrom(at: String ):Int {
        val now = LocalDateTime.now()
        val targetTime = getLocalDateTimeFrom(at)
        return ChronoUnit.SECONDS.between( targetTime, now ).toInt()
    }

    fun getHourMinuteFromSecond(second: Int): String {
        val hour = second / 3600
        val minute = (second - hour * 3600) / 60
        return "${if (hour != 0) "${hour}시간 " else ""}${minute}분"
    }

    fun getPassingTimeFrom(at: String): String {
        val now = LocalDateTime.now()
        val targetTime = getLocalDateTimeFrom(at)
        val hours = ChronoUnit.HOURS.between( targetTime, now ).toInt()
        val restMinutes = ChronoUnit.MINUTES.between( targetTime, now ).toInt() - hours*60
        return if(hours==0){
            "${restMinutes}분"
        }else{
            "${hours}시간 ${restMinutes}분"
        }
    }

    fun getPassingDayFrom( at: String ): Int {
        val now = LocalDateTime.now()
        val targetTime = getLocalDateTimeFrom(at)
        val date = ChronoUnit.DAYS.between( targetTime, now ).toInt()
        return if(at.isEmpty()) {
            0
        }else{
            date
        }
    }

    fun getPassingTimeFromTo( start: String, finish: String ): String {
        val startTime = getLocalDateTimeFrom(start)
        val finishTime = getLocalDateTimeFrom(finish)
        val hours = ChronoUnit.HOURS.between( startTime, finishTime ).toInt()
        val restMinutes = ChronoUnit.MINUTES.between( startTime, finishTime ).toInt() - hours*60
        return if(hours==0){
            "${restMinutes}분"
        }else{
            "${hours}시간 ${restMinutes}분"
        }
    }

    fun getLocalDate(string: String): LocalDate? {
        return try{
            val year = string.substring(0..3)
            val month = string.substring(5..6)
            val day =  string.substring(8..9)
            LocalDate.of(year.toInt(), month.toInt(), day.toInt())
        } catch (e: IndexOutOfBoundsException){
            null
        }
    }

    fun getAccessTokenExpiration(): String {
        return LocalDateTime.now().plusMinutes(29).toString()
    }

    fun isPast( at: String ): Boolean {
        val now = LocalDateTime.now()
        val targetTime = getLocalDateTimeFrom(at)
        val passingSeconds = ChronoUnit.SECONDS.between( targetTime, now ).toInt()
        return passingSeconds > 0
    }
}