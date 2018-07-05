package apps.jizzu.cryptocoin.view.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.presenter.AboutPresenter
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), AboutView {

    private val mPresenter = AboutPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        title = getString(R.string.aboutPage)

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val background = ContextCompat.getDrawable(this, R.drawable.gradient)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawable(background)

        mPresenter.attachView(this)
        setOnClickListeners()
    }

    override fun sendFeedback(email: Intent) {
        try {
            startActivity(Intent.createChooser(email, "Send feedback"))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun rateThisApp(appPackageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    override fun openSiteLink(intent: Intent) {
        startActivity(intent)
    }

    private fun setOnClickListeners() {
        cvFeedback.setOnClickListener {
            mPresenter.sendFeedbackButtonClicked()
        }

        cvRate.setOnClickListener {
            mPresenter.rateThisAppButtonClicked()
        }

        cvGitHub.setOnClickListener {
            mPresenter.openSiteLink("https://github.com/Jizzu/CryptoCoin")
        }

        llRetrofit.setOnClickListener {
            mPresenter.openSiteLink("https://github.com/square/retrofit")
        }

        llKotterKnife.setOnClickListener {
            mPresenter.openSiteLink("https://github.com/JakeWharton/kotterknife")
        }

        llShapeOfView.setOnClickListener {
            mPresenter.openSiteLink("https://github.com/florent37/ShapeOfView")
        }

        llMaterialSearchView.setOnClickListener {
            mPresenter.openSiteLink("https://github.com/MiguelCatalan/MaterialSearchView")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}
