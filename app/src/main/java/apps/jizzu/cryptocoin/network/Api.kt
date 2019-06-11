package apps.jizzu.cryptocoin.network

import apps.jizzu.cryptocoin.data.Coin
import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    @GET("ticker")
    fun getData(): Single<ArrayList<Coin>>
}
