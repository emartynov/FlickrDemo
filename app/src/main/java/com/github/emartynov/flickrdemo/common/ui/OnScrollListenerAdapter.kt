package com.github.emartynov.flickrdemo.common.ui

import android.widget.AbsListView

abstract class OnScrollListenerAdapter : AbsListView.OnScrollListener {
    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        // nothing, it is just adapter to remove boilerplate
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        // nothing, it is just adapter to remove boilerplate
    }
}
