package apps.jizzu.cryptocoin.base

interface BasePresenter<T> {
    fun takeView(view: T)
    fun dropView()
}