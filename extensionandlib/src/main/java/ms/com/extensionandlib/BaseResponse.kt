package ms.com.extensionandlib


class BaseResponse<T> {
    var OK: Boolean? = null
    var Message: String? = null
    var ErrorNumber: Int? = null
    var ErrorCode: String? = null
    var Content: T? = null
}