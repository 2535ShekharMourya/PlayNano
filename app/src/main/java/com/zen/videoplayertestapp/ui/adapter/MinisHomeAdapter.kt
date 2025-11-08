package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.brochill.minismodule.CarouselModel
import com.brochill.minismodule.CarouselResponse
import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.data.model.HorizontalItemResponse
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.data.model.VerticalItemResponse
import com.brochill.minismodule.util.LogStatus.Companion.logs
import com.brochill.minismodule.util.LogStatus.Companion.logs1
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zen.videoplayertestapp.R

class MinisHomeAdapter(val context: Context, var listener: RecyclerviewClickListener,var carouselItemListener: CarouselAdapter.CarouselItemListener) :
    RecyclerView.Adapter<MinisHomeAdapter.MinisHomeMainViewHolder>(),
    CarouselAdapter.CarouselItemListener, RecyclerviewClickListener {

    companion object {
        const val TYPE_CAROUSEL = "type_carousel"
        const val TYPE_SERIES = "type_series"
        const val TYPE_EPISODE = "type_episode"
        const val SHOW_LOADER = "show_loader"
    }

    private var carouselAdapter: CarouselAdapter? = null
    private var seriesVideoVerticalAdapter: SeriesVideoVerticalAdapter? = null
    private var seriesCollectionHorizontalAdapter: SeriesCollectionHorizontalAdapter? = null
    private var data = mutableListOf<Any>()

    val handler = Handler()
    lateinit var slideHandler: Runnable
    var isSliderHandlerStarted = false
    var numberOfItems: Int = 0
    var lastIndex: Int = 0
    var nextItem: Int = 0

    override fun getItemViewType(position: Int): Int {
        val dataItem = data[position]
        return when (dataItem) {
            is String -> {
                if (dataItem == TYPE_CAROUSEL) {
                    return 0
                } else if (dataItem == TYPE_SERIES) {
                    return 2
                } else return 1

            }

            is CarouselResponse -> {
                "getItemViewType : CarouselResponse ".logs()
                0
            }

            is VerticalItemResponse -> {
                2
            }
            is HorizontalItemResponse ->{
                1
            }

            else -> {
                -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinisHomeMainViewHolder {
        "$viewType".logs()
        return when (viewType) {
            0 -> {
                val carouselLayout = LayoutInflater.from(context)
                    .inflate(R.layout.carousel_itemview, parent, false)
                "onCreateViewHolder: carouselLayout".logs()
                return CarouselViewHolder(carouselLayout)

            }

            2 -> {
                "onCreateViewHolder: seriesCollectionVertical".logs()
                val seriesVideoVerticalLayout = LayoutInflater.from(context)
                    .inflate(R.layout.series_collection_vertical, parent, false)
                return SeriesVideoVerticalViewHolder(seriesVideoVerticalLayout)


            }

            1 -> {
                "onCreateViewHolder: seriesCollectionHorizontal".logs()
                val seriesCollectionHorizontalLayout = LayoutInflater.from(context)
                    .inflate(R.layout.series_collection_horizontal, parent, false)
                return SeriesCollectionHorizontalViewHolder(seriesCollectionHorizontalLayout)


            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MinisHomeMainViewHolder, position: Int) {
        val listItem = data[position]
        "listItem: ${listItem}".logs()
        "${holder.itemViewType}".logs()

        when (holder.itemViewType) {
            0 -> {
                "onBindViewHolder: carouselHolder".logs()
                if (listItem is String) {
                    " carousel if string".logs()

                    val carouselHolder = holder as CarouselViewHolder
                    carouselHolder.carouselShimmerLayout.visibility = View.VISIBLE
                    carouselHolder.carouselShimmer.startShimmer()
                    carouselHolder.carouselHolderLayout.visibility = View.GONE
                } else if (listItem is CarouselResponse) {
                    " carousel else if carousel".logs()
                    val carouselHolder = holder as CarouselViewHolder
                    "${carouselHolder.carouselShimmerLayout} ${carouselHolder.carouselShimmer} ${carouselHolder.carouselHolderLayout}".logs()
                    val carouselResponse = listItem
                    val listOfCarousels = carouselResponse.data
                    carouselAdapter = CarouselAdapter(context, this)

                    if (listOfCarousels.isNullOrEmpty()) {
                        "list of carousel is empty/null".logs()

                        carouselHolder.carouselShimmerLayout.visibility = View.GONE
                        carouselHolder.carouselShimmer.stopShimmer()
                        "carousel layout visible.".logs()
                        carouselHolder.carouselHolderLayout.visibility = View.GONE
//                        carouselHolder.carouselViewPager2.visibility = View.GONE
                    } else {
                        carouselHolder.carouselShimmerLayout.visibility = View.GONE
                        carouselHolder.carouselShimmer.stopShimmer()
                        "carousel layout visible.".logs()
                        carouselHolder.carouselHolderLayout.visibility = View.VISIBLE
                        val newHeightInDp = 242 // Replace with your desired height in dp
                        val newHeightInPixels = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            newHeightInDp.toFloat(),
                            context.resources.displayMetrics
                        ).toInt()

                        val layoutParams = carouselHolder.carouselViewPager2.layoutParams
                        layoutParams.height = newHeightInPixels
                        carouselHolder.carouselViewPager2.layoutParams = layoutParams
//                        carouselHolder.carouselViewPager2.visibility = View.VISIBLE

                        carouselAdapter!!.additems(listOfCarousels)
                        carouselHolder.carouselViewPager2.adapter = carouselAdapter

                        TabLayoutMediator(
                            carouselHolder.carouselTablayout,
                            carouselHolder.carouselViewPager2
                        ) { tab, pos ->
                            //Some implementation
                        }.attach()

                        carouselHolder.carouselViewPager2.clipToPadding = false
                        carouselHolder.carouselViewPager2.setPadding(0,0,0,0)
                        carouselHolder.carouselViewPager2.clipChildren = false
                        carouselHolder.carouselViewPager2.offscreenPageLimit = 3
                        carouselHolder.carouselViewPager2.getChildAt(0).overScrollMode =
                            RecyclerView.OVER_SCROLL_NEVER

                        val compositePageTransformer = CompositePageTransformer()
                        compositePageTransformer.addTransformer(MarginPageTransformer(40))
                        compositePageTransformer.addTransformer(object :
                            ViewPager2.PageTransformer {
                            override fun transformPage(page: View, position: Float) {
                                val r = 1 - Math.abs(position)
                                page.scaleY = (0.85f + r * 0.15f)
                            }

                        })



                        if (!isSliderHandlerStarted) {
                            slideHandler = Runnable {

                                isSliderHandlerStarted = true

                                numberOfItems =
                                    carouselHolder.carouselViewPager2.adapter?.itemCount ?: 0
                                lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
                                nextItem =
                                    if (carouselHolder.carouselViewPager2.currentItem == lastIndex) 0 else carouselHolder.carouselViewPager2.currentItem + 1
                                carouselHolder.carouselViewPager2.setCurrentItem(nextItem, true)
//                                Utils.log("inside carousel handler nextItem: " + nextItem)
                            }
                        } else {
                            // stop the handler
                            //reset all the values to zero
                            handler.removeCallbacks(slideHandler)
                            numberOfItems = 0
                            lastIndex = 0
                            nextItem = 0

                            slideHandler = Runnable {
                                numberOfItems =
                                    carouselHolder.carouselViewPager2.adapter?.itemCount ?: 0
                                lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
                                nextItem =
                                    if (carouselHolder.carouselViewPager2.currentItem == lastIndex) 0 else carouselHolder.carouselViewPager2.currentItem + 1
                                carouselHolder.carouselViewPager2.setCurrentItem(nextItem, true)
//                                Utils.log("inside carousel handler nextItem: " + nextItem)
                            }
                        }

                        carouselHolder.carouselViewPager2.setPageTransformer(
                            compositePageTransformer
                        )


                        carouselHolder.carouselViewPager2.registerOnPageChangeCallback(object :
                            ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)

                                if (carouselAdapter != null) {
                                    carouselHolder.carouselTablayout.visibility = View.VISIBLE
                                }
                                handler.removeCallbacks(slideHandler)
                                handler.postDelayed(slideHandler, 3000)
                            }
                        })

                    }

                }

            }

            2 -> {
                if (listItem is VerticalItemResponse) {
                    "HomepageResponse 1 : ${listItem}".logs()
                    val seriesVerticalHolder = holder as SeriesVideoVerticalViewHolder
                    seriesVerticalHolder.title.text = listItem.title

                  //  seriesVerticalHolder.viewAll
                    val seriesVerticalVideoResponse = listItem
                    val seriesVerticalVideoList = seriesVerticalVideoResponse.series
                    seriesVerticalHolder.viewAll.setOnClickListener {
                        seriesVerticalVideoList?.toMutableList()
                            ?.let { it1 -> listener.onSeriesItemClick(listItem,position, it1) }

                    }
                    if (seriesVerticalVideoList.isNullOrEmpty()) {
                        "seriesVerticalVideoList is Empty".logs()
                    } else {
                        "HomepageResponse : ${seriesVerticalVideoList}".logs()
                        val layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        seriesVideoVerticalAdapter = SeriesVideoVerticalAdapter(context, this)
                        seriesVerticalHolder.verticalVideosRecyclerView.layoutManager =
                            layoutManager
                        seriesVerticalHolder.verticalVideosRecyclerView.adapter =
                            seriesVideoVerticalAdapter
                        seriesVideoVerticalAdapter?.updateItems(seriesVerticalVideoList)

                    }

                }

            }

            1 -> {
                if (listItem is HorizontalItemResponse) {
                    "HomepageResponse 2 : ${listItem}".logs()
                    val seriesVerticalHolder = holder as SeriesCollectionHorizontalViewHolder
                    seriesVerticalHolder.title.text= listItem.title
                    val seriesHorizontalVideoResponse = listItem
                    val seriesHorizontalVideoList = seriesHorizontalVideoResponse.episodes
                    if (seriesHorizontalVideoList.isNullOrEmpty()) {
                        "seriesVerticalVideoList is Empty".logs()
                    } else {
                        "HomepageResponse : ${seriesHorizontalVideoList}".logs()
                        val layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        seriesCollectionHorizontalAdapter =
                            SeriesCollectionHorizontalAdapter(context, this)
                        seriesVerticalHolder.HorizontalCollectionRecyclerView.layoutManager =
                            layoutManager
                        seriesVerticalHolder.HorizontalCollectionRecyclerView.adapter =
                            seriesCollectionHorizontalAdapter
                        seriesCollectionHorizontalAdapter?.updateItem(seriesHorizontalVideoList)

                    }

                }
            }
        }

    }

    open class MinisHomeMainViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // ViewHolders
    inner class CarouselViewHolder(view: View) : MinisHomeMainViewHolder(view) {

        val carouselViewPager2 = view.findViewById<ViewPager2>(R.id.carousel_vp)
        val carouselTablayout = view.findViewById<TabLayout>(R.id.carousel_tablayout)

        var carouselHolderLayout = view.findViewById<LinearLayout>(R.id.carousel_layout)
        val carouselShimmerLayout = view.findViewById<LinearLayout>(R.id.carousel_shimmer_layout)
        val carouselShimmer = view.findViewById<ShimmerFrameLayout>(R.id.carousel_shimmer)


    }

    inner class SeriesVideoVerticalViewHolder(view: View) : MinisHomeMainViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val viewAll = view.findViewById<TextView>(R.id.view_all)
        val verticalVideosRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)


    }

    inner class SeriesCollectionHorizontalViewHolder(view: View) : MinisHomeMainViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val viewAll = view.findViewById<TextView>(R.id.view_all)
        val HorizontalCollectionRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

    }


    fun addVerticalItem(): Int {
        val addingAtIndex = data.size
        "data size before VERTICAL_ITEM: ${data.size}".logs()
        data.add(TYPE_SERIES)
        "data size after VERTICAL_ITEM: ${data.size}".logs()
        return addingAtIndex
    }

    fun addHorizontalItme(): Int {
        val addingAtIndex = data.size
        "data size before HORIZONTAL_ITEM: ${data.size}".logs()
        data.add(TYPE_EPISODE)
        "data size after HORIZONTAL_ITEM: ${data.size}".logs()
        return addingAtIndex
    }

    fun addCarouselItme(): Int {
        val addingAtIndex = data.size
        "data size before CAROUSEL_ITEM: ${data.size}".logs()
        data.add(TYPE_CAROUSEL)
        "data size after CAROUSEL_ITEM: ${data.size}".logs()
        return addingAtIndex
    }

    fun showLoader(): Int {
        val addingAtIndex = data.size
        "data size before SHOW_LOADER: ${data.size}".logs()
        data.add(SHOW_LOADER)
        "data size after SHOW_LOADER: ${data.size}".logs()
        return addingAtIndex

    }

    fun hideLoader(index: Int) {
        if (data.size > 0) {

            data.removeAt(index)
            "notifyItemRemoved searchHomeAdapter: $index".logs()

            notifyItemRemoved(index)
        }

    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun replaceVerticalItem(homepageResponse: VerticalItemResponse?, index: Int) {
        if (data.size > 0) {

            if (homepageResponse != null) {
                data[index] = homepageResponse
            }
            "changing Cust.. index of: $index".logs()

            notifyItemChanged(index)
        }

    }

    fun replaceCarouselItem(carouselResponse: CarouselResponse?, index: Int) {
        if (data.size > 0) {

            if (carouselResponse != null) {
                data[index] = carouselResponse
            }
            "changing Cust.. index of: $index".logs()

            notifyItemChanged(index)
        }

    }

    fun replaceHorizontalItem(homepageResponse: HorizontalItemResponse, index: Int) {

        if (data.size > 0) {

            data[index] = homepageResponse
            "changing images.. index of: $index".logs()

            notifyItemChanged(index)
        }
    }


    override fun onItemClickListener(item: Any, position: Int,data:MutableList<Episode>) {
        "Episode Data: ${data}".logs1()
        listener?.onItemClickListener(item, position,data)
    }

    override fun onSeriesItemClick(item: Any, position: Int,data: MutableList<Series>) {
        listener?.onSeriesItemClick(item, position,data)
    }


    override fun onItemClick(item: Any, position: Int, action: String) {
        if (item is CarouselModel) {
            " MinisHomeAdapter item is CarouselModel...".logs1()
            when (action) {
                CarouselAdapter.CarouselItemListener.ACTION_SERIES -> {
                    " MinisHomeAdapter item is CarouselModel...".logs1()
                    carouselItemListener.onItemClick(item, position, action)

                }
            }

        }
    }
}

