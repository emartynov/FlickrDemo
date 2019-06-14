package com.github.emartynov.flickrdemo.imagelist.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.GridView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.github.emartynov.flickrdemo.R
import com.github.emartynov.flickrdemo.common.async.AsyncImpl
import com.github.emartynov.flickrdemo.common.http.HttpImpl
import com.github.emartynov.flickrdemo.common.image.BitmapScaleImpl
import com.github.emartynov.flickrdemo.common.image.CacheImpl
import com.github.emartynov.flickrdemo.common.ui.OnScrollListenerAdapter
import com.github.emartynov.flickrdemo.imagelist.model.ImageListSearchModel
import com.github.emartynov.flickrdemo.imagelist.model.State

class MainActivity : AppCompatActivity() {
    private val gridView: GridView
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<GridView>(R.id.images_view) }
    private val progressView: ProgressBar
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<ProgressBar>(R.id.progress_view) }
    private val errorView: View
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<View>(R.id.error_view) }
    private val retryButton: Button
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<Button>(R.id.retry_button) }

    private val imagesAdapter = ImagesAdapter(HttpImpl(), AsyncImpl(), BitmapScaleImpl(), CacheImpl())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (model == null) {
            model = ImageListSearchModel()
        }

        model?.also { model ->
            model.addObserver { _, _ ->
                progressView.visibility = if (model.state == State.LOADING) View.VISIBLE else View.GONE
                errorView.visibility = if (model.state == State.ERROR) View.VISIBLE else View.GONE

                imagesAdapter.setImages(model.images)
            }
        }

        retryButton.setOnClickListener { model?.retry() }
        setupGridView()

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                model?.run {
                    search(query)
                }
            }
        }
    }

    private fun setupGridView() {
        gridView.adapter = imagesAdapter
        gridView.setOnScrollListener(
            object : OnScrollListenerAdapter() {
                override fun onScroll(
                    view: AbsListView,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
                ) {
                    val lastInScreen = firstVisibleItem + visibleItemCount

                    model?.run {
                        val pageToLoad = this.currentPage + 1
                        // request new page when in the middle of the current one
                        if (lastInScreen >= totalItemCount - totalItemCount / 2 && pageToLoad <= this.totalPages) {
                            this.loadPage(pageToLoad)
                        }
                    }
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.opitions_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        return true
    }

    override fun onDestroy() {
        if (isFinishing) {
            model?.clear()
            model = null
        }

        super.onDestroy()
    }

    companion object {
        var model: ImageListSearchModel? = null
    }
}
