package apps.jizzu.cryptocoin.screens.details

class DetailsPresenter : DetailsContract.Presenter {
    private var mView: DetailsContract.View? = null

    override fun takeView(view: DetailsContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }
}