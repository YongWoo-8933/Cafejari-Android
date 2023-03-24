package com.software.cafejariapp.data.util.error

sealed class ErrorType {
    object TokenExpired : ErrorType()
    object InternalServer : ErrorType()
    object InternetConnection : ErrorType()
    object NoObject : ErrorType()
    object MasterExpired : ErrorType()
    object TimeOut : ErrorType()
    object Unknown : ErrorType()
    object Canceled : ErrorType()

    data class ErrorMessage(val message: String): ErrorType()
}
