package ms.com.extensionandlib


open class BaseRepo {
    inline fun <reified T> api(): T? {
        return APIManager.client?.create(T::class.java)
    }

}