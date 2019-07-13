package apps.jizzu.cryptocoin.screens.activity

import android.content.Intent
import android.os.Bundle
import android.support.constraint.Group
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.data.Coin
import apps.jizzu.cryptocoin.screens.adapter.CoinsAdapter
import apps.jizzu.cryptocoin.screens.vm.*
import apps.jizzu.cryptocoin.utils.*
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotterknife.bindView
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val mRecyclerView: RecyclerView by bindView(R.id.rvCoinsList)
    private val mProgressBar: ProgressBar by bindView(R.id.progressBar)
    private val mSwipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipeToRefresh)
    private val mGroupNoInternet: Group by bindView(R.id.gNoInternet)
    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mSearchView: MaterialSearchView by bindView(R.id.searchView)

    private val mAdapter: CoinsAdapter by inject()
    private val mPreferenceHelper: PreferenceHelper by inject()
    private val mViewModel: CoinsViewModel by viewModel()

    private var mIsSearchViewShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        initListeners()

        mViewModel.mLiveData.observeNonNull(this@MainActivity, ::updateViewState)
    }

    private fun initUI() {
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(mToolbar)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@MainActivity, android.R.color.transparent)
            navigationBarColor = ContextCompat.getColor(this@MainActivity, android.R.color.transparent)
            setBackgroundDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.gradient))
        }

        mSwipeRefreshLayout.apply {
            setOnRefreshListener(this@MainActivity)
            setColorSchemeColors(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
        }

        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun initListeners() {
        mSearchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                mIsSearchViewShown = false
            }

            override fun onSearchViewShown() {
                mIsSearchViewShown = true
            }
        })

        mSearchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = true

            override fun onQueryTextChange(newText: String): Boolean {
                if (mIsSearchViewShown) {
                    mViewModel.searchData(newText)
                }
                return false
            }
        })

        mAdapter.setOnItemClickListener(object : CoinsAdapter.OnAdapterClickListener {
            override fun onItemClick(position: Int) = showDetailsActivity(mAdapter.getCoinAtPosition(position))
        })
    }

    private fun updateViewState(state: ViewState) {
        when (state) {
            is ViewStateAppStart -> showProgressBar()
            is ViewStateSearch -> showSearchResults(state.coins)
            is ViewStateSort -> showContent(state.coins)
            is ViewStateError -> showError()
            is ViewStateSuccess -> {
                hideLoadingError()
                hideProgressBar()
                cancelRefreshAnimation()
                showContent(state.coins)
            }
        }
    }

    private fun showSearchResults(coins: ArrayList<Coin>) = mAdapter.searchFilter(coins)

    private fun showContent(coins: ArrayList<Coin>) {
        mRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_animation_fall_down)
        mAdapter.setCoins(coins)
    }

    private fun showError() {
        hideProgressBar()
        cancelRefreshAnimation()
        mAdapter.clearCoins()
        mViewModel.clearData()
        showLoadingError()
    }

    private fun showDetailsActivity(coin: Coin) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("symbol", coin.symbol)
            putExtra("name", coin.name)
            putExtra("id", coin.id)
            putExtra("price_usd", coin.priceUSD)
            putExtra("price_btc", coin.priceBitcoin)
            putExtra("24h_volume_usd", coin.volumeUSD)
            putExtra("market_cap_usd", coin.marketCapUSD)
            putExtra("available_supply", coin.availableSupply)
            putExtra("total_supply", coin.totalSupply)
            putExtra("percent_change_1h", coin.percentChangeHour)
            putExtra("percent_change_24h", coin.percentChangeDay)
            putExtra("percent_change_7d", coin.percentChangeWeek)
        }
        startActivity(intent)
    }

    private fun cancelRefreshAnimation() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    private fun showLoadingError() = mGroupNoInternet.visible()

    private fun hideLoadingError() = mGroupNoInternet.gone()

    private fun showProgressBar() = mProgressBar.visible()

    private fun hideProgressBar() = mProgressBar.invisible()

    override fun onRefresh() = mViewModel.loadCoinsList()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        mSearchView.setMenuItem(menu.findItem(R.id.actionSearch))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var selectedItemPosition = mPreferenceHelper.getInt(PreferenceHelper.SORT_KEY)

        when (item.itemId) {
            R.id.actionAbout -> startActivity(Intent(this, AboutActivity::class.java))
            R.id.actionSort -> {
                val listItems = resources.getStringArray(R.array.sortBy)

                AlertDialog.Builder(this, R.style.AlertDialogStyle).apply {
                    setTitle(getString(R.string.dialogTitle))
                    setSingleChoiceItems(listItems, selectedItemPosition) { dialogInterface, i ->
                        mViewModel.sortData(i, true)
                        selectedItemPosition = i
                        mPreferenceHelper.putInt(PreferenceHelper.SORT_KEY, i)
                        toast(getString(R.string.sortToast, listItems[i]))
                        dialogInterface.dismiss()
                    }
                    create().show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mSearchView.isSearchOpen) {
            mSearchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }
}
