package info.camposha.infinityrecyclerview

/**
 * [.onLoadMoreListener] called when [.recyclerView] reach to item with position [.totalItemCount] - {@value THRESHOLD}
 */
open interface OnLoadMoreListener {
    fun onLoadMore()
}