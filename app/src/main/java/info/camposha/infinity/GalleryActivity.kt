package info.camposha.infinity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import info.camposha.infinityrecyclerview.Infinity
import info.camposha.infinityrecyclerview.OnLoadMoreListener
import kotlinx.android.synthetic.main.slide.*
import java.util.*
import kotlin.collections.ArrayList

open class GalleryActivity : AppCompatActivity() {

    var pageToFetch = 1
    var data: ArrayList<String> = ArrayList()
    private var adapter: ImageAdapter? = null
    private var reachedEnd = false

    private fun show(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun prepareData(): List<String> {
        val end: Int = rv.layoutManager!!.itemCount + 10

        val start: Int = if (pageToFetch <= 1) {
            1
        } else {
            rv.layoutManager!!.itemCount + 1
        }
        return (start..end).map { "https://picsum.photos/1000/700?image=$it" }
    }

    /**
     * Let's simulate a download operation
     */
    fun simulateDownload(pb: ProgressBar, data: List<String>): MutableLiveData<List<String>> {
        pb.visibility = View.VISIBLE
        val timer = Timer()
        val mLiveData = MutableLiveData<List<String>>()
        timer.schedule(object : TimerTask() {
            override fun run() {
                mLiveData.postValue(data)
            }
        }, 3000)
        return mLiveData
    }

    /**
     * Setup the adapter. Pass a layoutmanager to recyclerview before setting up Infinity
     */
    private fun setupAdapter() {
        rv.layoutManager = GridLayoutManager(this, 2)
        adapter = ImageAdapter(data, ::goToDetails)
        rv.adapter = adapter
    }

    /**
     * Setup our infinity library
     */
    private fun setupInfinity() {
        Infinity().setup(rv, object : OnLoadMoreListener {
            override fun onLoadMore() {
                pb.visibility = View.VISIBLE

                if (!reachedEnd) {
                    pageToFetch++
                    simulateDownload(pb, prepareData()).observe(this@GalleryActivity, Observer {
                        adapter?.addAll(it)
                        pb.visibility = View.GONE
                    })
                } else {
                    show("No more Data")
                }
            }
        })
    }

    private fun goToDetails(s: String, view: View) {
        show("Clicked: $s")
    }

    override fun onResume() {
        super.onResume()
        simulateDownload(pb, prepareData()).observe(this@GalleryActivity, Observer {
            adapter?.addAll(it)
            pb.visibility = View.GONE
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide)
        setupAdapter()
        setupInfinity()
    }
}