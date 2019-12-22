package ms.com.extensionandlib.livedata.retrofit


class Resource<T> constructor(val status: Status?, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> responseNull(): Resource<T> {
            return Resource(Status.NULL, null, null)
        }

        fun <T> error(msg: String,code : Int?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }
}
