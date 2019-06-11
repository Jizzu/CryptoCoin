package apps.jizzu.cryptocoin.screens.main

import apps.jizzu.cryptocoin.data.Coin
import apps.jizzu.cryptocoin.network.ApiClient
import apps.jizzu.cryptocoin.utils.PreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainModel : MainContract.Model {
    var mData = arrayListOf<Coin>()

    override fun loadData(callback: LoadDataCallback) {
        ApiClient.client.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            mData = it
                            sortData(PreferenceHelper.getInstance().getInt(PreferenceHelper.SORT_KEY), null)
                            callback.onLoad(mData)
                        },
                        onError = { callback.onFailure() }
                )
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