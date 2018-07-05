package apps.jizzu.cryptocoin.model

import android.util.Log
import apps.jizzu.cryptocoin.network.App
import apps.jizzu.cryptocoin.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoinsModel {

    var data = arrayListOf<Coin>()

    fun loadData(callback: LoadUserCallback) {
        val api = App.serverApi

        // enqueue() - выполняет асинхронный запрос
        api.getData().enqueue(object : Callback<ArrayList<Coin>> {
            override fun onResponse(call: Call<ArrayList<Coin>>, response: Response<ArrayList<Coin>>) {
                Log.d("CoinsModel", "onResponse")

                val coins = response.body()

                if (coins != null) {
                    data = coins
                }

                val mPreferenceHelper = PreferenceHelper.instance
                val selectedItemPosition = mPreferenceHelper.getInt(PreferenceHelper.ITEM_POSITION)

                sortData(selectedItemPosition, null)

                // Сообщаем презентеру о том, что запрос был успешно получен
                // И передаём ему список с данными криптовалют
                callback.onLoad(coins as ArrayList<Coin>)
            }

            override fun onFailure(call: Call<ArrayList<Coin>>, t: Throwable) {
                Log.d("CoinsModel", "onFailure")

                // Сообщаем презентеру о том, что запрос не был получен
                callback.onFailure()
            }
        })
    }

    fun searchData(text: String): ArrayList<Coin> {
        val newText = text.toLowerCase()
        val list: ArrayList<Coin> = ArrayList()

        for (itemName in data) {
            if (newText != " " && (itemName.name.toLowerCase().contains(newText) || itemName.symbol.toLowerCase().contains(newText))) {
                list.add(itemName)
            }
        }
        return list
    }

    fun sortData(key: Int, callback: SortDataCallback?) {
        when (key) {
            0 -> data.sortWith(compareByDescending { it.priceUSD })
            1 -> data.sortWith(compareBy { it.priceUSD })
            2 -> data.sortWith(compareByDescending { it.percentChangeHour })
            3 -> data.sortWith(compareBy { it.percentChangeHour })
            4 -> data.sortWith(compareByDescending { it.percentChangeDay })
            5 -> data.sortWith(compareBy { it.percentChangeDay })
            6 -> data.sortWith(compareByDescending { it.percentChangeWeek })
            7 -> data.sortWith(compareBy { it.percentChangeWeek })
            8 -> data.sortWith(compareByDescending { it.marketCapUSD })
            9 -> data.sortWith(compareBy { it.marketCapUSD })
        }
        callback?.onSort()
    }

    interface LoadUserCallback {
        fun onLoad(coins: ArrayList<Coin>)
        fun onFailure()
    }

    interface SortDataCallback {
        fun onSort()
    }
}