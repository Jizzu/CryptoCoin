package apps.jizzu.cryptocoin.screens.main

import apps.jizzu.cryptocoin.data.Coin
import kotlin.collections.ArrayList

class MainPresenter: MainContract.Presenter {
    private var mView: MainContract.View? = null
    private val mModel = MainModel()

    override fun loadCoinsList() {
        mModel.loadData(object : MainModel.LoadDataCallback {
            override fun onFailure() {
                mView?.hideProgressBar()
                mView?.cancelRefreshAnimation()
                mView?.showLoadingError()
            }

            override fun onLoad(coins: ArrayList<Coin>) {
                mView?.hideLoadingError()
                mView?.hideProgressBar()
                mView?.cancelRefreshAnimation()
                mView?.showContent(coins)
            }
        })
    }

    override fun searchData(searchText: String) {
        mView?.showSearchResults(mModel.searchData(searchText))
    }

    override fun sortData(key: Int) {
        mModel.sortData(key, object : MainModel.SortDataCallback {
            override fun onSort(data: ArrayList<Coin>) {
                mView?.showContent(data)
            }
        })
    }

    override fun takeView(view: MainContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }
}