package info.camposha.infinityrecyclerview

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class Infinity {
    /**
     * [RecyclerView] to render data and be scrolled
     */
    private var rv: RecyclerView? = null

    /**
     * [Boolean] To hold the loading state of our data
     */
    private var isLoading = false

    /**
     * [OnLoadMoreListener] Every time a user reaches the end of the list this listener will
     * be triggered
     */
    private var onLoadMoreListener: OnLoadMoreListener? = null

    /**
     * [LayoutManager] this is what is responsible for item positioning in our [.recyclerView]
     *
     * It will inform us of the following: [.lastVisibleItem],[.totalItemCount],[.previousItemCount]
     */
    private var layoutManager: RecyclerView.LayoutManager? = null

    /**
     * [Int] This is the position of last visible item in our [.recyclerview]
     */
    private var lastVisibleItem = 0

    /**
     * [Int] This is the total number of items in our [.recyclerView]
     */
    private var totalItemCount = 0

    /**
     * The total number of items before a call to [.onLoadMoreListener]
     */
    private var previousItemCount = 0

    /**
     * This function will setup the [.recyclerview] with the [.OnLoadMoreListener]
     *
     * @param recyclerView       The recyclerview to setup
     * @param onLoadMoreListener callback for notifying when user reach list ends.
     */
    fun setup(recyclerView: RecyclerView, onLoadMoreListener: OnLoadMoreListener?) {
        this.rv = recyclerView
        this.onLoadMoreListener = onLoadMoreListener
        layoutManager = recyclerView.layoutManager
        setInfiniteScroll(layoutManager)
    }

    /**
     * this function get scrolling control of [.recyclerView] and whenever
     * user reached list ends, [.onLoadMoreListener] will be called
     */
    private fun setInfiniteScroll(layoutManager: RecyclerView.LayoutManager?) {
        rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                totalItemCount = layoutManager!!.itemCount
                when {
                    previousItemCount > totalItemCount -> {
                        previousItemCount = totalItemCount - THRESHOLD
                    }
                }
                when (layoutManager) {
                    is GridLayoutManager -> {
                        lastVisibleItem =
                            (layoutManager as GridLayoutManager?)!!.findLastVisibleItemPosition()
                    }
                    is LinearLayoutManager -> {
                        lastVisibleItem =
                            (layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
                    }
                    is StaggeredGridLayoutManager -> {
                        val staggeredGridLayoutManager: StaggeredGridLayoutManager? =
                            layoutManager
                        val spanCount: Int = staggeredGridLayoutManager!!.spanCount
                        val ids = IntArray(spanCount)
                        staggeredGridLayoutManager.findLastVisibleItemPositions(ids)
                        var max = ids[0]
                        for (i in 1 until ids.size) {
                            if (ids[1] > max) {
                                max = ids[1]
                            }
                        }
                        lastVisibleItem = max
                    }
                }
                if (totalItemCount > THRESHOLD) {
                    if (previousItemCount <= totalItemCount && isLoading) {
                        isLoading = false
                        Log.i("Infinity", "Data fetched")
                    }
                    if (!isLoading && lastVisibleItem > totalItemCount - THRESHOLD && totalItemCount > previousItemCount) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener!!.onLoadMore()
                        }
                        Log.i("Infinity", "Reached End Of List")
                        isLoading = true
                        previousItemCount = totalItemCount
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    companion object {
        private const val THRESHOLD = 3
    }
}