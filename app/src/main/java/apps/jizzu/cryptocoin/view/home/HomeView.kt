package apps.jizzu.cryptocoin.view.home

import apps.jizzu.cryptocoin.model.Coin
import java.util.ArrayList

interface HomeView {

    fun showContent(coins: ArrayList<Coin>)

    fun readyForSearch(coins: ArrayList<Coin>)

    fun cancelRefreshAnimation()

    fun showLoadingError()

    fun hideLoadingError()

    fun showProgressBar()

    fun hideProgressBar()
}