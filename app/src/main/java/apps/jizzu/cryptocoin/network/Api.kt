package apps.jizzu.cryptocoin.network

import apps.jizzu.cryptocoin.data.Coin
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @GET("ticker")
    fun getData(): Call<ArrayList<Coin>>
}
