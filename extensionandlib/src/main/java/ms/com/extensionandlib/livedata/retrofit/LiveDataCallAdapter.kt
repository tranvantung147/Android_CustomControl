package ms.com.extensionandlib.livedata.retrofit

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<T>(val responseType: Type) : CallAdapter<T, LiveData<ApiResponse<T>>> {
    override fun adapt(call: Call<T>): LiveData<ApiResponse<T>> {
        return object : LiveData<ApiResponse<T>>() {
            internal var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<T> {
                        override fun onResponse(c: Call<T>, response: Response<T>) {
                            postValue(ApiResponse(response))
                        }

                        override fun onFailure(c: Call<T>, throwable: Throwable) {
                            postValue(
                                ApiResponse<T>(
                                    throwable
                                )
                            )
                        }
                    })
                }
            }
        }
    }
    override fun responseType(): Type = responseType
}
