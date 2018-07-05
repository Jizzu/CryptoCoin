package apps.jizzu.cryptocoin.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.model.Coin
import apps.jizzu.cryptocoin.view.detail.DetailActivity
import kotterknife.bindView
import java.text.DecimalFormat
import java.util.*


class CoinsAdapter : RecyclerView.Adapter<CoinsAdapter.CoinViewHolder>() {

    private var mCoins = ArrayList<Coin>()
    private lateinit var mContext: Context
    private var mLastPosition = -1

    fun setCoins(coins: ArrayList<Coin>) {
        this.mCoins = coins
        notifyDataSetChanged()
    }

    fun clearCoins() {
        mCoins = ArrayList()
        notifyDataSetChanged()
    }

    fun searchFilter(newList: ArrayList<Coin>) {
        clearCoins()
        mCoins.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        mContext = parent.context
        return CoinViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_coin, parent, false))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(mCoins[position])

        val coin = mCoins[position]

        val itemView = holder.itemView
        itemView.setOnClickListener {
            val intent = Intent(mContext, DetailActivity::class.java)

            intent.putExtra("symbol", coin.symbol)
            intent.putExtra("name", coin.name)
            intent.putExtra("id", coin.id)
            intent.putExtra("price_usd", coin.priceUSD)
            intent.putExtra("price_btc", coin.priceBitcoin)
            intent.putExtra("24h_volume_usd", coin.volumeUSD)
            intent.putExtra("market_cap_usd", coin.marketCapUSD)
            intent.putExtra("available_supply", coin.availableSupply)
            intent.putExtra("total_supply", coin.totalSupply)
            intent.putExtra("percent_change_1h", coin.percentChangeHour)
            intent.putExtra("percent_change_24h", coin.percentChangeDay)
            intent.putExtra("percent_change_7d", coin.percentChangeWeek)

            mContext.startActivity(intent)
        }

        val displayMetrics = DisplayMetrics()
        (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        Log.d("CoinsAdapter", "width = $width, height = $height")

        if (width < 720 || height < 1184) {
            holder.hourlyText.textSize = 12.0f
            holder.dailyText.textSize = 12.0f
            holder.weeklyText.textSize = 12.0f
            holder.percentChangeHour.textSize = 12.0f
            holder.percentChangeDay.textSize = 12.0f
            holder.percentChangeWeek.textSize = 12.0f
        }

        val animation = AnimationUtils.loadAnimation(mContext,
                if (position > mLastPosition)
                    R.anim.up_from_bottom
                else
                    R.anim.down_from_top)
        holder.itemView.startAnimation(animation)
        mLastPosition = position
    }

    override fun getItemCount() = mCoins.size

    override fun onViewDetachedFromWindow(holder: CoinViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val symbol: TextView by bindView(R.id.tvSymbol)
        private val name: TextView  by bindView(R.id.tvName)
        private val price: TextView  by bindView(R.id.tvPrice)

        val hourlyText: TextView  by bindView(R.id.tvHourlyText)
        val dailyText: TextView  by bindView(R.id.tvDailyText)
        val weeklyText: TextView  by bindView(R.id.tvWeeklyText)
        val percentChangeHour: TextView  by bindView(R.id.tvHourlyPercentChange)
        val percentChangeDay: TextView  by bindView(R.id.tvDailyPercentChange)
        val percentChangeWeek: TextView  by bindView(R.id.tvWeeklyPercentChange)

        private val context: Context = itemView.context

        fun bind(coin: Coin) {
            symbol.text = coin.symbol
            name.text = coin.name
            price.text = context.getString(R.string.priceUsd, DecimalFormat("#,###.##########").format(coin.priceUSD))

            if (coin.percentChangeHour >= 0) {
                percentChangeHour.text = context.getString(R.string.percentChangePlus, coin.percentChangeHour)
                percentChangeHour.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                percentChangeHour.text = context.getString(R.string.percentChangeMinus, coin.percentChangeHour)
                percentChangeHour.setTextColor(ContextCompat.getColor(context, R.color.red))
            }

            if (coin.percentChangeDay >= 0) {
                percentChangeDay.text = context.getString(R.string.percentChangePlus, coin.percentChangeDay)
                percentChangeDay.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                percentChangeDay.text = context.getString(R.string.percentChangeMinus, coin.percentChangeDay)
                percentChangeDay.setTextColor(ContextCompat.getColor(context, R.color.red))
            }

            if (coin.percentChangeWeek >= 0) {
                percentChangeWeek.text = context.getString(R.string.percentChangePlus, coin.percentChangeWeek)
                percentChangeWeek.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                percentChangeWeek.text = context.getString(R.string.percentChangeMinus, coin.percentChangeWeek)
                percentChangeWeek.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }
}
