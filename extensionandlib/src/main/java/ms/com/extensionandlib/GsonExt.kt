package ms.com.extensionandlib

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

inline fun <reified T> parseJsonToObject(json: String): T? {
    if (json.isEmpty()) {
        return null
    }
    val gson = GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create()
    val type = genericType<T>()
    return gson.fromJson(json, type)
}

fun <T> parseObjectToJson(input: T): String {
    val gson = Gson()
    return gson.toJson(input)
}

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type