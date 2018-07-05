package apps.jizzu.cryptocoin.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object App {

    private val builder = Retrofit.Builder()
    private const val baseURL = "https://api.coinmarketcap.com/v1/"

    private val retrofit = builder.baseUrl(baseURL) // Базовая часть адреса
            .addConverterFactory(GsonConverterFactory.create()) // Конвертер, необходимый для преобразования JSON'а в объекты
            .build()

    val serverApi = retrofit.create(Api::class.java) // Создаем объект, при помощи которого будем выполнять запросы
}
