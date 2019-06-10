package apps.jizzu.cryptocoin.screens.main

import apps.jizzu.cryptocoin.base.BasePresenter
import apps.jizzu.cryptocoin.data.Coin
import java.util.ArrayList

interface MainContract {
    interface View {
        fun showContent(coins: ArrayList<Coin>)
        fun showSearchResults(coins: ArrayList<Coin>)
        fun cancelRefreshAnimation()
        fun showLoadingError()
        fun hideLoadingError()
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter : BasePresenter<View> {
        fun loadCoinsList()
        fun searchData(searchText: String)
        fun sortData(key: Int)
    }

    interface Model {
        fun loadData(callback: MainModel.LoadDataCallback)
        fun searchData(text: String): ArrayList<Coin>
        fun sortData(key: Int, callback: MainModel.SortDataCallback?)
    }
}