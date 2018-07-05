package apps.jizzu.cryptocoin.presenter

import apps.jizzu.cryptocoin.model.Coin
import apps.jizzu.cryptocoin.model.CoinsModel
import apps.jizzu.cryptocoin.view.home.HomeView
import java.util.*

class HomePresenter(private val mModel: CoinsModel) {

    private lateinit var mView: HomeView

    fun attachView(view: HomeView) {
        this.mView = view
    }

    fun viewIsReady() {
        mView.showProgressBar()
        loadCoinsList()
        mView.cancelRefreshAnimation()
    }

    fun viewIsRefreshing() {
        loadCoinsList()
    }

    private fun loadCoinsList() {
        mModel.loadData(object : CoinsModel.LoadUserCallback {
            override fun onFailure() {
                mView.hideProgressBar()
                mView.cancelRefreshAnimation()
                mView.showLoadingError()
            }

            override fun onLoad(coins: ArrayList<Coin>) {
                mView.showContent(coins)
                mView.hideLoadingError()
                mView.hideProgressBar()
                mView.cancelRefreshAnimation()
            }
        })
    }

    fun clearData() {
        mModel.data = ArrayList()
    }

    fun searchData(text: String) {
        val data = mModel.searchData(text)
        mView.readyForSearch(data)
    }

    fun sortItems(key: Int) {
        mModel.sortData(key, object : CoinsModel.SortDataCallback {
            override fun onSort() {
                mView.showContent(mModel.data)
            }
        })
    }
}