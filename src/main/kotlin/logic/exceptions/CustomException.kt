package org.example.logic.exceptions

sealed class CustomException(
    exceptionMessage: ExceptionMessage
) : Throwable(message = exceptionMessage.message){
    sealed class DataException(open val exceptionMessage: ExceptionMessage) : CustomException(exceptionMessage) {
        data class ReadFileException(
            override val exceptionMessage: ExceptionMessage = ExceptionMessage.READ_EXCEPTION_MESSAGE
        ) : DataException(exceptionMessage)

        data class WriteFileException(
            override val exceptionMessage: ExceptionMessage = ExceptionMessage.WRITE_EXCEPTION_MESSAGE
        ) : DataException(exceptionMessage)
    }

    sealed class LogicException(
        open val exceptionMessage: ExceptionMessage
    ) : CustomException(exceptionMessage) {

    }
}