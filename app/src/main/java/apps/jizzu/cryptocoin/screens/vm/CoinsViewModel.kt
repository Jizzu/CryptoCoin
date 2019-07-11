package apps.jizzu.cryptocoin.screens.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import apps.jizzu.cryptocoin.data.Coin
import apps.jizzu.cryptocoin.network.ApiClient
import apps.jizzu.cryptocoin.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CoinsViewModel : ViewModel() {
    private var mData = arrayListOf<Coin>()
    val mLiveData = MutableLiveData<ViewState>()

    fun loadCoinsList() {
        ApiClient.client.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            mData = it
                            sortData(PreferenceHelper.getInstance().getInt(PreferenceHelper.SORT_KEY))
                            mLiveData.value = ViewStateSuccess(mData)
                        },
                        onError = { mLiveData.value = ViewStateError }
                )
    }

    fun clearData() = mData.clear()

    fun searchData(text: String) {
        val newText = text.toLowerCase()
        val searchResults: ArrayList<Coin> = ArrayList()

        for (itemName in mData) {
            if (newText != " " && (itemName.name.toLowerCase().contains(newText) || itemName.symbol.toLowerCase().contains(newText))) {
                searchResults.add(itemName)
            }
        }
        mLiveData.value = ViewStateSearch(searchResults)
    }

    fun sortData(key: Int) {
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
        mLiveData.value = ViewStateSort(mData)
    }
}