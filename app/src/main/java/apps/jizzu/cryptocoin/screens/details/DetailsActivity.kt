package apps.jizzu.cryptocoin.screens.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.di.App
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), DetailsContract.View {
    @Inject lateinit var mPresenter: DetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        App.getApp(this).getAppComponent().createScreensComponent().inject(this)
        initUI()
        checkLocale()
        initListeners()
    }

    private fun initUI() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@DetailsActivity, android.R.color.transparent)
            navigationBarColor = ContextCompat.getColor(this@DetailsActivity, android.R.color.transparent)
            setBackgroundDrawable(ContextCompat.getDrawable(this@DetailsActivity, R.drawable.gradient))
        }

        val decimalFormat = DecimalFormat("#,###.##########")
        val symbol = intent.getStringExtra("symbol")
        val name = intent.getStringExtra("name")
        val priceUSD = intent.getDoubleExtra("price_usd", 0.0)
        val priceBitcoin = intent.getDoubleExtra("price_btc", 0.0)
        val volumeUSD = intent.getDoubleExtra("24h_volume_usd", 0.0)
        val marketCapUSD = intent.getDoubleExtra("market_cap_usd", 0.0)
        val availableSupply = intent.getDoubleExtra("available_supply", 0.0)
        val totalSupply = intent.getDoubleExtra("total_supply", 0.0)
        val percentChangeHour = intent.getDoubleExtra("percent_change_1h", 0.0)
        val percentChangeDay = intent.getDoubleExtra("percent_change_24h", 0.0)
        val percentChangeWeek = intent.getDoubleExtra("percent_change_7d", 0.0)

        tvSymbol.text = symbol
        tvName.text = name
        tvPriceUSDValue.text = getString(R.string.priceUsd, decimalFormat.format(priceUSD))
        tvPriceBTCValue.text = getString(R.string.priceBtc, decimalFormat.format(priceBitcoin))
        tv24hVolumeUSDValue.text = getString(R.string.priceUsd, decimalFormat.format(volumeUSD))
        tvMarketCapUSDValue.text = getString(R.string.priceUsd, decimalFormat.format(marketCapUSD))
        tvAvailableSupplyValue.text = getString(R.string.supply, decimalFormat.format(availableSupply), symbol)
        tvTotalSupplyValue.text = getString(R.string.supply, decimalFormat.format(totalSupply), symbol)

        if (percentChangeHour >= 0) {
            tvHourlyPercentChangeValue.text = getString(R.string.percentChangePlus, percentChangeHour)
            tvHourlyPercentChangeValue.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            tvHourlyPercentChangeValue.text = getString(R.string.percentChangeMinus, percentChangeHour)
            tvHourlyPercentChangeValue.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        if (percentChangeDay >= 0) {
            tvDailyPercentChangeValue.text = getString(R.string.percentChangePlus, percentChangeDay)
            tvDailyPercentChangeValue.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            tvDailyPercentChangeValue.text = getString(R.string.percentChangeMinus, percentChangeDay)
            tvDailyPercentChangeValue.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        if (percentChangeWeek >= 0) {
            tvWeeklyPercentChangeValue.text = getString(R.string.percentChangePlus, percentChangeWeek)
            tvWeeklyPercentChangeValue.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            tvWeeklyPercentChangeValue.text = getString(R.string.percentChangeMinus, percentChangeWeek)
            tvWeeklyPercentChangeValue.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }

    private fun initListeners() {
        ibBackArrow.setOnClickListener {
            onBackPressed()
        }

        bSourceLink.setOnClickListener {
            openLink("https://coinmarketcap.com/ru/currencies/${intent.getStringExtra("id")}")
        }
    }

    private fun openLink(link: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

    private fun checkLocale() {
        val language = Locale.getDefault().displayLanguage.toString()
        Log.d("DetailsActivity", "Current Language: $language")

        val displayMetrics = DisplayMetrics()
        (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        Log.d("DetailsActivity", "width = $width, height = $height")

        if (language == "русский" && (width < 720 || height < 1184)) {
            tvPriceUSD.textSize = 15.5f
            tvPriceBTC.textSize = 15.5f
            tv24hVolumeUSD.textSize = 15.5f
            tvMarketCapUSD.textSize = 15.5f
            tvAvailableSupply.textSize = 15.5f
            tvTotalSupply.textSize = 15.5f
            tvHourlyPercentChange.textSize = 15.5f
            tvDailyPercentChange.textSize = 15.5f
            tvWeeklyPercentChange.textSize = 15.5f

            tvPriceUSDValue.textSize = 14.0f
            tvPriceBTC.textSize = 14.0f
            tv24hVolumeUSDValue.textSize = 14.0f
            tvMarketCapUSDValue.textSize = 14.0f
            tvAvailableSupplyValue.textSize = 14.0f
            tvTotalSupplyValue.textSize = 14.0f
            tvHourlyPercentChangeValue.textSize = 14.0f
            tvDailyPercentChangeValue.textSize = 14.0f
            tvWeeklyPercentChangeValue.textSize = 14.0f
        }
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
