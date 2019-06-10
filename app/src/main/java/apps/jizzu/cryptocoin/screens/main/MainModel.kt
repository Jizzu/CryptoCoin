package apps.jizzu.cryptocoin.screens.main

import apps.jizzu.cryptocoin.data.Coin
import apps.jizzu.cryptocoin.network.ApiClient
import apps.jizzu.cryptocoin.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainModel : MainContract.Model {
    var mData = arrayListOf<Coin>()

    override fun loadData(callback: LoadDataCallback) {
        ApiClient.client.getData().enqueue(object : Callback<ArrayList<Coin>> {
            override fun onResponse(call: Call<ArrayList<Coin>>, response: Response<ArrayList<Coin>>) {
                val coins = response.body()
                val selectedItemPosition = PreferenceHelper.getInstance().getInt(PreferenceHelper.ITEM_POSITION)

                if (coins != null) {
                    mData = coins
                }
                sortData(selectedItemPosition, null)
                callback.onLoad(coins as ArrayList<Coin>)
            }

            override fun onFailure(call: Call<ArrayList<Coin>>, t: Throwable) = callback.onFailure()
        })
    }

    override fun searchData(text: String): ArrayList<Coin> {
        val newText = text.toLowerCase()
        val list: ArrayList<Coin> = ArrayList()

        for (itemName in mData) {
            if (newText != " " && (itemName.name.toLowerCase().contains(newText) || itemName.symbol.toLowerCase().contains(newText))) {
                list.add(itemName)
            }
        }
        return list
    }

    override fun sortData(key: Int, callback: SortDataCallback?) {
        when (key) {
            0 -> mData.sortWith(compareByDescending { it.priceUSD })
            1 -> mData.sortWith(compareBy { it.priceUSD })
            2 -> mData.sortWith(compareByDescending { it.percentChangeHour })
            3 -> mData.sortWith(compareBy { it.percentChangeHour })
            4 -> mData.sortWith(compareByDescending { it.percentChangeDay })
            5 -> mData.sortWith(compareBy { it.percentChangeDay })
            6 -> mData.sortWith(compareByDescending { it.percentChangeWeek })
            7 -> mData.sortWith(compareBy { it.percentChangeWeek })
            8 -> mData.sortWith(compareByDescending { it.marketCapUSD })
            9 -> mData.sortWith(compareBy { it.marketCapUSD })
        }
        callback?.onSort(mData)
    }

    interface LoadDataCallback {
        fun onLoad(coins: ArrayList<Coin>)
        fun onFailure()
    }

    interface SortDataCallback {
        fun onSort(data: ArrayList<Coin>)
    }
}