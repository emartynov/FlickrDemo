package com.github.emartynov.flickrdemo.imagelist

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.github.emartynov.flickrdemo.R
import com.github.emartynov.flickrdemo.imagelist.model.ImageListSearchModel
import com.github.emartynov.flickrdemo.imagelist.model.State

class MainActivity : AppCompatActivity() {
    private val gridView: GridView
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<GridView>(R.id.images_view) }
    private val progressView: ContentLoadingProgressBar
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<ContentLoadingProgressBar>(R.id.progress_view) }
    private val errorView: View
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<View>(R.id.error_view) }
    private val retryButton: Button
            by lazy(LazyThreadSafetyMode.NONE) { findViewById<Button>(R.id.retry_button) }

    private val imagesAdapter = ImagesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (model == null) {
            model = ImageListSearchModel()
        }

        model?.also { model ->
            model.addObserver { _, _ ->
                gridView.visibility = if (model.state == State.READY) View.VISIBLE else View.GONE
                if (model.state == State.LOADING) progressView.show() else progressView.hide()
                errorView.visibility = if (model.state == State.ERROR) View.VISIBLE else View.GONE

                imagesAdapter.setImages(model.images)
            }

            model.search("kittens")
        }

        retryButton.setOnClickListener { model?.retry() }
        gridView.adapter = imagesAdapter
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
