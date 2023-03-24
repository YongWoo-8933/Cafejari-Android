package com.software.cafejariapp.core

import com.software.cafejariapp.data.util.error.ErrorType


class CustomException(type: ErrorType) : Exception(
    when(type){
        is ErrorType.InternetConnection -> "인터넷 연결이 불안합니다. 통신환경을 확인해주세요"
        is ErrorType.InternalServer -> "내부 서버 오류입니다. 잠시 후에 다시 시도해주세요"
        is ErrorType.NoObject -> "결과가 없습니다"
        is ErrorType.MasterExpired -> "일정 시간 활동을 하지 않아 카페마스터가 자동 해제되었습니다"
        is ErrorType.Unknown -> "오류가 발생했습니다"
        is ErrorType.TimeOut -> "응답시간 초과, 인터넷 연결을 확인하시고 앱을 재부팅해보세요"
        is ErrorType.Canceled -> "데이터 로드중 취소됨"
        is ErrorType.ErrorMessage -> type.message
        else -> "오류가 발생했습니다"
    }
)

class TokenExpiredException: Exception()

class RefreshTokenExpiredException: Exception()

class MasterExpiredException: Exception()

class LocationTrackingNotPermitted: Exception()