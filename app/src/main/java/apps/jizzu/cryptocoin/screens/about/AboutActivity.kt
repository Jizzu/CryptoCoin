package apps.jizzu.cryptocoin.screens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import apps.jizzu.cryptocoin.BuildConfig
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.di.App
import apps.jizzu.cryptocoin.utils.*
import kotlinx.android.synthetic.main.activity_about.*
import javax.inject.Inject

class AboutActivity : AppCompatActivity(), AboutContract.View {
    @Inject lateinit var mPresenter: AboutPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        App.getApp(this).getAppComponent().createScreensComponent().inject(this)
        initUI()
        initListeners()
    }

    private fun initUI() {
        title = getString(R.string.aboutPage)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@AboutActivity, android.R.color.transparent)
            navigationBarColor = ContextCompat.getColor(this@AboutActivity, android.R.color.transparent)
            setBackgroundDrawable(ContextCompat.getDrawable(this@AboutActivity, R.drawable.gradient))
        }
    }

    private fun sendFeedback() {
        val email = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.Builder().scheme("mailto").build()
            putExtra(Intent.EXTRA_EMAIL, arrayOf("ilya.ponomarenko.dev@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.feedbackTitle))
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.feedbackDeviceInfo) + DeviceInfo.deviceInfo
                    + "\n" + resources.getString(R.string.feedbackAppVersion) + BuildConfig.VERSION_NAME
                    + "\n" + resources.getString(R.string.feedback) + "\n")
        }
        try {
            startActivity(Intent.createChooser(email, "Send feedback"))
        } catch (ex: android.content.ActivityNotFoundException) {
            toast( getString(R.string.noEmailAppsError))
        }
    }

    private fun rateThisApp() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (exception: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    private fun openLink(link: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

    private fun initListeners() {
        cvFeedback.setOnClickListener {
            sendFeedback()
        }

        cvRate.setOnClickListener {
            rateThisApp()
        }

        cvGitHub.setOnClickListener {
            openLink(GIT_HUB_PAGE)
        }

        llRetrofit.setOnClickListener {
            openLink(RETROFIT_PAGE)
        }

        llKotterKnife.setOnClickListener {
            openLink(KOTTER_KNIFE_PAGE)
        }

        llShapeOfView.setOnClickListener {
            openLink(SHAPE_OF_VIEW_PAGE)
        }

        llMaterialSearchView.setOnClickListener {
            openLink(MATERIAL_SEARCH_VIEW_PAGE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dropView()
    }
}
