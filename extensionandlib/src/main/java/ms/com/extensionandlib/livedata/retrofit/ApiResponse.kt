package ms.com.extensionandlib.livedata.retrofit

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class ApiResponse<T> {
    var code: Int = 0
    var body: T? = null
    var errorMess: String? = ""
    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            errorMess = null
        } else {
            body = null
            var message: String = ""
            if (response.code() == ErrorNumber.NotFound.error) {
                message = "Không tìm thấy máy chủ!!!"
            } else if (response.code() == ErrorNumber.InternalServerError.error) {
                message = "InternalServerError"
            } else {
                response.errorBody()?.let {
                    val errorBody = it.toString()
                    try {
                        val jsonError = JSONObject(errorBody)
                        when {
                            jsonError.has("error") -> message = jsonError["error"].toString()
                            jsonError.has("Message") -> message = jsonError.getString("Message")
                            else -> message = it.string()
                        }
                    } catch (e: JSONException) {
                        Log.e("ApiRespinse", "error while parsing error")
                        message = "Định dạng dữ liệu không hợp lệ"
                    } catch (ignored: IOException) {
                        Log.e("ApiRespinse", "error while parsing error")
                        message = "Định dạng dữ liệu không hợp lệ"
                    }
                }
            }
            if (message?.trim { it <= ' ' }.isEmpty()) {
                message = response.message()
            }
            errorMess = message
        }
    }

    constructor(e: Throwable) {
        errorMess = e.message
        errorMess = "Không có kết nối mạng, vui lòng kiểm tra lại kết nối!"
        body = null
    }
}