package apps.jizzu.cryptocoin.view.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.Toast
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.adapter.CoinsAdapter
import apps.jizzu.cryptocoin.model.Coin
import apps.jizzu.cryptocoin.model.CoinsModel
import apps.jizzu.cryptocoin.presenter.HomePresenter
import apps.jizzu.cryptocoin.utils.PreferenceHelper
import apps.jizzu.cryptocoin.view.about.AboutActivity
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_home.*
import kotterknife.bindView
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), HomeView, SwipeRefreshLayout.OnRefreshListener {

    private val mRecyclerView: RecyclerView by bindView(R.id.recycler)
    private val mProgressBar: ProgressBar by bindView(R.id.progressBar)
    private val mSwipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipeToRefresh)
    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mSearchView: MaterialSearchView by bindView(R.id.searchView)

    private val mAdapter = CoinsAdapter()
    private lateinit var mPresenter: HomePresenter

    private val mPreferenceHelper = PreferenceHelper.instance
    private var mSelectedItemPosition = 0

    override fun onRefresh() {
        mPresenter.viewIsRefreshing()
    }

    override fun cancelRefreshAnimation() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showContent(coins: ArrayList<Coin>) {
        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(this, resId)
        mRecyclerView.layoutAnimation = animation
        mAdapter.setCoins(coins)
    }

    override fun readyForSearch(coins: ArrayList<Coin>) {
        mAdapter.searchFilter(coins)
    }

    override fun showLoadingError() {
        mAdapter.clearCoins()
        mPresenter.clearData()
        gNoInternet.visibility = View.VISIBLE
    }

    override fun hideLoadingError() {
        gNoInternet.visibility = View.GONE
    }

    override fun showProgressBar() {
        mProgressBar.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = ProgressBar.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(mToolbar)

        val background = ContextCompat.getDrawable(this, R.drawable.gradient)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawable(background)

        gNoInternet.visibility = View.GONE
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter

        val model = CoinsModel()
        mPresenter = HomePresenter(model)

        PreferenceHelper.instance.init(applicationContext)
        mPresenter.attachView(this)
        mPresenter.viewIsReady()

        mSearchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("HomeActivity", "onQueryTextSubmit")
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mPresenter.searchData(newText)
                Log.d("HomeActivity", "onQueryTextChange: newText = $newText")
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val item = menu.findItem(R.id.actionSearch)
        mSearchView.setMenuItem(item)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        mSelectedItemPosition = mPreferenceHelper.getInt(PreferenceHelper.ITEM_POSITION)

        when (item!!.itemId) {
            R.id.actionAbout -> {
                val activityAbout = Intent(this, AboutActivity::class.java)
                startActivity(activityAbout)
            }
            R.id.actionSort -> {
                val listItems = resources.getStringArray(R.array.sortBy)

                val mBuilder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                mBuilder.setTitle(getString(R.string.dialogTitle))
                mBuilder.setSingleChoiceItems(listItems, mSelectedItemPosition) { dialogInterface, i ->
                    mPresenter.sortItems(i)
                    mSelectedItemPosition = i
                    mPreferenceHelper.putInt(PreferenceHelper.ITEM_POSITION, i)
                    Toast.makeText(this, "${getString(R.string.sortToast)} ${listItems[i]}", Toast.LENGTH_SHORT).show()
                    dialogInterface.dismiss()
                }
                val mDialog = mBuilder.create()
                mDialog.show()
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
