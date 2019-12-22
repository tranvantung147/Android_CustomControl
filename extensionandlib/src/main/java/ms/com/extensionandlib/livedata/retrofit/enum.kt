package ms.com.extensionandlib.livedata.retrofit

enum class ErrorNumber(val error: Int) {
    NotFound(404), InternalServerError(500), MissUpdateFields(121), ProfileError(181), FalseConnect(
        101)
}
enum class Status {
    SUCCESS, ERROR, LOADING, NULL
}