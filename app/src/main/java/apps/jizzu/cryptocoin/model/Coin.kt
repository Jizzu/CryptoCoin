package apps.jizzu.cryptocoin.model

import com.google.gson.annotations.SerializedName

data class Coin(
        @SerializedName("id")
        val id: String,

        @SerializedName("name")
        val name: String,

        @SerializedName("symbol")
        val symbol: String,

        @SerializedName("price_usd")
        val priceUSD: Double,

        @SerializedName("price_btc")
        val priceBitcoin: Double,

        @SerializedName("24h_volume_usd")
        val volumeUSD: Double,

        @SerializedName("market_cap_usd")
        val marketCapUSD: Double,

        @SerializedName("available_supply")
        val availableSupply: Double,

        @SerializedName("total_supply")
        val totalSupply: Double,

        @SerializedName("percent_change_1h")
        val percentChangeHour: Double,

        @SerializedName("percent_change_24h")
        val percentChangeDay: Double,

        @SerializedName("percent_change_7d")
        val percentChangeWeek: Double
)