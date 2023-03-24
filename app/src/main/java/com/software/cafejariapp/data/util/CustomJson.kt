package com.software.cafejariapp.data.util

object CustomJson {
    val customJson = kotlinx.serialization.json.Json {
        isLenient = true            // "" 따옴표 잘못된건 무시하고 처리
        ignoreUnknownKeys = true    // 모델에 없고, json에 있는경우 해당 key 무시
        encodeDefaults = false      // null 인 값은 포함 안시킴
        allowSpecialFloatingPointValues = true
        coerceInputValues = true    // null일 경우 지정된 디폴트값 사용
    }
}