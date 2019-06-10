package apps.jizzu.cryptocoin.screens.main

import android.content.Intent
import android.os.Bundle
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
import apps.jizzu.cryptocoin.screens.main.adapter.CoinsAdapter
import apps.jizzu.cryptocoin.utils.*
import apps.jizzu.cryptocoin.screens.about.AboutActivity
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotterknife.bindView

class MainActivity : AppCompatActivity(), MainContract.View, SwipeRefreshLayout.OnRefreshListener {
    private val mRecyclerView: RecyclerView by bindView(R.id.rvCoinsList)
    private val mProgressBar: ProgressBar by bindView(R.id.progressBar)
    private val mSwipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipeToRefresh)
    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mSearchView: MaterialSearchView by bindView(R.id.searchView)

    private val mAdapter = CoinsAdapter()
    private val mPresenter = MainPresenter()
    private val mPreferenceHelper = PreferenceHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        initListeners()
        mPreferenceHelper.init(applicationContext)
        mPresenter.loadCoinsList()
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
        mSearchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = true

            override fun onQueryTextChange(newText: String): Boolean {
                mPresenter.searchData(newText)
                return false
            }
        })
    }

    override fun onRefresh() = mPresenter.loadCoinsList()

    override fun cancelRefreshAnimation() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showContent(coins: ArrayList<Coin>) {
        mRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_animation_fall_down)
        mAdapter.setCoins(coins)
    }

    override fun showSearchResults(coins: ArrayList<Coin>) = mAdapter.searchFilter(coins)

    override fun showLoadingError() {
        mAdapter.clearCoins()
        gNoInternet.visible()
    }

    override fun hideLoadingError() = gNoInternet.gone()

    override fun showProgressBar() = mProgressBar.visible()

    override fun hideProgressBar() = mProgressBar.invisible()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        mSearchView.setMenuItem(menu.findItem(R.id.actionSearch))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var selectedItemPosition = mPreferenceHelper.getInt(PreferenceHelper.ITEM_POSITION)

        when (item.itemId) {
            R.id.actionAbout -> startActivity(Intent(this, AboutActivity::class.java))
            R.id.actionSort -> {
                val listItems = resources.getStringArray(R.array.sortBy)

                AlertDialog.Builder(this, R.style.AlertDialogStyle).apply {
                    setTitle(getString(R.string.dialogTitle))
                    setSingleChoiceItems(listItems, selectedItemPosition) { dialogInterface, i ->
                        mPresenter.sortData(i)
                        selectedItemPosition = i
                        mPreferenceHelper.putInt(PreferenceHelper.ITEM_POSITION, i)
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

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dropView()
    }
}
