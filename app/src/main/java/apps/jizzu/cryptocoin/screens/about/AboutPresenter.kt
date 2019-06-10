package apps.jizzu.cryptocoin.screens.about

class AboutPresenter : AboutContract.Presenter {
    private var mView: AboutContract.View? = null

    override fun takeView(view: AboutContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }
}