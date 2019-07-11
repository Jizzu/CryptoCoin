package apps.jizzu.cryptocoin.screens.vm

import apps.jizzu.cryptocoin.data.Coin

sealed class ViewState

object ViewStateError : ViewState()

class ViewStateSuccess(val coins: ArrayList<Coin>) : ViewState()

class ViewStateSearch(val coins: ArrayList<Coin>) : ViewState()

class ViewStateSort(val coins: ArrayList<Coin>) : ViewState()