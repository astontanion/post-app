package space.stanton.technicaltest.network

import android.os.Bundle
import androidx.core.os.bundleOf

sealed class DataResource<T>(
    val data: T? = null,
    val message: DataMessage
) {

    class Idle<T>(data: T? = null) : DataResource<T>(data, DataMessage.None)

    class Waiting<T>(data: T? = null) : DataResource<T>(data, DataMessage.Waiting)

    class Successful<T>(
        data: T? = null,
        operation: Operation
    ) : DataResource<T>(data = data, message = DataMessage.Success(operation = operation))

    class Failure<T>(
        operation: Operation,
        reason: Reason = GenericFailureReason.UNKNOWN,
        extra: Bundle = bundleOf(),
    ) : DataResource<T>(
        data = null,
        message = DataMessage.Failure(
            operation = operation,
            reason = reason,
            extra = extra
        )
    )
}