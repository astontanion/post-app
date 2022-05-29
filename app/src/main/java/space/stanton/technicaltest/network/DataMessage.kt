package space.stanton.technicaltest.network

import android.os.Bundle
import androidx.core.os.bundleOf

sealed class Reason

sealed class Operation {
    object POST : Operation()
    object GET : Operation()
    object PUT : Operation()
    object DEL : Operation()
}

sealed class NetworkFailureReason : Reason() {
    object UNKNOWN : NetworkFailureReason()
    object CONNECTION: NetworkFailureReason()
}

sealed class DataMessage {
    object None : DataMessage()

    object Waiting : DataMessage()

    data class Success(
        val operation: Operation
    ) : DataMessage()

    data class Failure(
        val operation: Operation,
        val reason: Reason = NetworkFailureReason.UNKNOWN,
        val extra: Bundle = bundleOf()
    ) : DataMessage()
}