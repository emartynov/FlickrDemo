package com.github.emartynov.flickrdemo.imagelist.ui

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.GridView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.emartynov.flickrdemo.R
import com.github.emartynov.flickrdemo.common.async.AsyncImpl
import com.github.emartynov.flickrdemo.common.http.HttpImpl
import com.github.emartynov.flickrdemo.common.image.BitmapScaleImpl
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

    private val imagesAdapter = ImagesAdapter(HttpImpl(), AsyncImpl(), BitmapScaleImpl())

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

            model.search("kittens")
        }

        retryButton.setOnClickListener { model?.retry() }
        setupGridView()
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
