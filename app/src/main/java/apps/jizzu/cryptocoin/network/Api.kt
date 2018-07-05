package apps.jizzu.cryptocoin.network

import apps.jizzu.cryptocoin.model.Coin
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    // Данный метод возвращает список криптовалют с заполненными данными
    @GET("ticker")
    fun getData(): Call<ArrayList<Coin>>
}
