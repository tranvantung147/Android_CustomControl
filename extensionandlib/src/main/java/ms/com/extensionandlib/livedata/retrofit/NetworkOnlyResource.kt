package ms.com.extensionandlib.livedata.retrofit

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class NetworkOnlyResource<RequestType> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<RequestType>>()

    init {
        result.setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    @Suppress("UNCHECKED_CAST")
    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        apiResponse?.let {
            result.addSource<ApiResponse<RequestType>>(it) { response ->
                response.body?.let {
                    result.value = Resource.success(response.body)
                } ?: kotlin.run {
                    onFetchFailed()
                    if (response.code == 401) {
                        result.setValue(
                            Resource.error(
                                response.errorMess.toString(),
                                response.code,
                                null
                            )
                        )
                    } else {
                        result.setValue(
                            Resource.error(
                                response.errorMess ?: "",
                                response.code,
                                null
                            )
                        )
                    }
                }
            }
        }
    }


    protected fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<RequestType>> {
        return result
    }

    @WorkerThread
    protected fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>?
}