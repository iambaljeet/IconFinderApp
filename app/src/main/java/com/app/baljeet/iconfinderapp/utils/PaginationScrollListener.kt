package com.app.baljeet.iconfinderapp.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(var gridLayoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {
    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)


        val visibleItemCount = gridLayoutManager.childCount
        val totalItemCount = gridLayoutManager.itemCount
        val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()
}