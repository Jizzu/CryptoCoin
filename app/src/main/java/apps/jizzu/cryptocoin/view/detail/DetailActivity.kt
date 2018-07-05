package apps.jizzu.cryptocoin.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.presenter.DetailPresenter
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DecimalFormat
import java.util.*

class DetailActivity : AppCompatActivity(), DetailView {

    private val mPresenter = DetailPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = intent
        val decimalFormat = DecimalFormat("#,###.##########")

        val symbol = intent.getStringExtra("symbol")
        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")
        val priceUSD = intent.getDoubleExtra("price_usd", 0.0)
        val priceBitcoin = intent.getDoubleExtra("price_btc", 0.0)
        val volumeUSD = intent.getDoubleExtra("24h_volume_usd", 0.0)
        val marketCapUSD = intent.getDoubleExtra("market_cap_usd", 0.0)
        val availableSupply = intent.getDoubleExtra("available_supply", 0.0)
        val totalSupply = intent.getDoubleExtra("total_supply", 0.0)
        val percentChangeHour = intent.getDoubleExtra("percent_change_1h", 0.0)
        val percentChangeDay = intent.getDoubleExtra("percent_change_24h", 0.0)
        val percentChangeWeek = intent.getDoubleExtra("percent_change_7d", 0.0)

        val background = ContextCompat.getDrawable(this, R.drawable.gradient)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawable(background)

        mPresenter.attachView(this)

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

        checkLocale()

        ibBackArrow.setOnClickListener {
            onBackPressed()
        }

        bSourceLink.setOnClickListener {
            mPresenter.openSiteLink("https://coinmarketcap.com/ru/currencies/$id")
        }
    }

    private fun checkLocale() {
        val language = Locale.getDefault().displayLanguage.toString()
        Log.d("DetailActivity", "Current Language: $language")

        val displayMetrics = DisplayMetrics()
        (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        Log.d("DetailActivity", "width = $width, height = $height")

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

    override fun openSiteLink(intent: Intent) {
        startActivity(intent)
    }
}
