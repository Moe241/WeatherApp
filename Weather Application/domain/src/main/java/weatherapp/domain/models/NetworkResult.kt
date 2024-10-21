package weatherapp.domain.models

import weatherapp.domain.models.Error as ErrorModel

sealed class NetworkResult<out M> {
    class Success<out M>(val data: M) : NetworkResult<M>()
    class Error(val data: ErrorModel? = null) : NetworkResult<Nothing>()
    class Failure<out M>(val data: M) : NetworkResult<M>()
    object Loading : NetworkResult<Nothing>()
    object Empty : NetworkResult<Nothing>()
}
