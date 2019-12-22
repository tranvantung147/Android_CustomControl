package ms.com.extensionandlib.livedata.retrofit

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class NetworkBoundResource<ResultType, RequestType> {
    private var appExecutors: AppExecutors

    private val result = MediatorLiveData<Resource<ResultType>>()

    @MainThread
    constructor(appExecutors: AppExecutors) {
        this.appExecutors = appExecutors
        result.setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        apiResponse?.let {
            result.addSource<ApiResponse<RequestType>>(apiResponse) { response ->
                if (response?.body != null) {
                    result.value =
                        Resource.success(response.body as ResultType)
                    appExecutors.diskIO().execute {
                        saveCallResult((response.body as RequestType))
                    }
                } else {
                    result.value =
                        Resource.error(
                            response.errorMess
                                ?: "Đã xảy ra lỗi xin vui lòng thử lại ", response.code, null
                        )
                    onFetchFailed()
                }
            }
        }
    }

    protected fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean


    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>?

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>?
}