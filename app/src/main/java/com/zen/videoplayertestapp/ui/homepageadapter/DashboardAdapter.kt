package com.zen.videoplayertestapp.ui.homepageadapter

import android.content.Context
import android.os.Handler
import android.util.TypedValue
import com.zen.videoplayertestapp.data.dummydata.MultiviewDataItem

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
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.util.LogStatus.Companion.logs
import com.brochill.minismodule.util.LogStatus.Companion.logs1
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zen.videoplayertestapp.R
import com.zen.videoplayertestapp.databinding.CarouselItemLayoutBinding
import com.zen.videoplayertestapp.databinding.CarouselItemviewBinding
import com.zen.videoplayertestapp.databinding.SeriesCollectionVerticalBinding
import com.zen.videoplayertestapp.ui.adapter.CarouselAdapter
import com.zen.videoplayertestapp.ui.adapter.MinisHomeAdapter.CarouselViewHolder
import com.zen.videoplayertestapp.ui.adapter.MinisHomeAdapter.SeriesVideoVerticalViewHolder
import com.zen.videoplayertestapp.ui.adapter.SeriesVideoVerticalAdapter


class DashboardAdapter(val context: Context, private val dataList: List<MultiviewDataItem>?, var listener: RecyclerviewClickListener, var carouselItemListener: CarouselAdapter.CarouselItemListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), CarouselAdapter.CarouselItemListener, RecyclerviewClickListener {

    val handler = Handler()
    lateinit var slideHandler: Runnable
    var isSliderHandlerStarted = false
    var numberOfItems: Int = 0
    var lastIndex: Int = 0
    var nextItem: Int = 0

    override fun getItemViewType(position: Int): Int {
        return when (dataList?.get(position)) {
            is MultiviewDataItem.Carousel -> DashboardDataItemType.CAROUSEL
            is MultiviewDataItem.RecentlyViewed -> DashboardDataItemType.RECENTLY_VIEWED
//            is MultiviewDataItem.Banner -> DashboardDataItemType.BANNER
//            is MultiviewDataItem.Recommended -> DashboardDataItemType.RECOMMENDED
//            is MultiviewDataItem.TopInIndia -> DashboardDataItemType.TOP_IN_INDIA
//            is MultiviewDataItem.BigBanner -> DashboardDataItemType.BIG_BANNER
//            is MultiviewDataItem.YouMayLike -> DashboardDataItemType.YOU_MAY_LIKE
//            is MultiviewDataItem.FullScreenBanner -> DashboardDataItemType.FULL_SCREEN_BANNER
//            is MultiviewDataItem.ExploreMore -> DashboardDataItemType.EXPLORE_MORE
            null -> {0}
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DashboardDataItemType.CAROUSEL -> {
                val binding =
                    CarouselItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CarouselItemViewHolder(binding)
            }
            DashboardDataItemType.RECENTLY_VIEWED -> {
                val binding =
                    SeriesCollectionVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SeriesItemViewHolder(binding)
            }
//            DashboardDataItemType.BANNER -> {
//                val binding =
//                    BannerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                BannerItemViewHolder(binding)
//            }
//            DashboardDataItemType.RECOMMENDED -> {
//                val binding =
//                    CollageItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                CollageItemViewHolder(binding)
//            }
//            DashboardDataItemType.TOP_IN_INDIA -> {
//                val binding =
//                    CollageItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                CollageItemViewHolder(binding)
//            }
//            DashboardDataItemType.BIG_BANNER -> {
//                val binding =
//                    FullBannerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                FullBannerItemViewHolder(binding)
//            }
//            DashboardDataItemType.YOU_MAY_LIKE -> {
//                val binding =
//                    CollageItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                CollageItemViewHolder(binding)
//            }
//            DashboardDataItemType.FULL_SCREEN_BANNER -> {
//                val binding =
//                    FullScreenBannerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                FullScreenBannerItemViewHolder(binding)
//            }
//            DashboardDataItemType.EXPLORE_MORE -> {
//                val binding =
//                    CollageItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                CollageItemViewHolder(binding)
//            }

            else -> {throw IllegalArgumentException("Invalid view type")}
        }

    }

    override fun getItemCount(): Int = dataList?.size?:0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = dataList?.get(position)) {
            is MultiviewDataItem.Carousel -> { (holder as CarouselItemViewHolder).bindCarouselItems(item.carousels) }
            is MultiviewDataItem.RecentlyViewed -> { (holder as SeriesItemViewHolder).bingCollage(item.recentlyViewed) }
//            is MultiviewDataItem.Banner -> {(holder as BannerItemViewHolder).bindBannerView(item.bannerItem) }
//            is MultiviewDataItem.Recommended -> { (holder as CollageItemViewHolder).bingCollage(item.recommended) }
//            is MultiviewDataItem.TopInIndia ->{ (holder as CollageItemViewHolder).bingCollage(item.topInIndia)}
//            is MultiviewDataItem.BigBanner ->{ (holder as FullBannerItemViewHolder).bindFullBanner(item.bigBannerItem)}
//            is MultiviewDataItem.YouMayLike ->{ (holder as CollageItemViewHolder).bingCollage(item.youMayLike)}
//            is MultiviewDataItem.FullScreenBanner ->{ (holder as FullScreenBannerItemViewHolder).bindFullScreenBannerView(item.fullScreenBanner)}
//            is MultiviewDataItem.ExploreMore ->{ (holder as CollageItemViewHolder).bingCollage(item.exploreMore)}
            null -> {}
        }
    }
    inner class CarouselItemViewHolder(private val binding: CarouselItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val carouselViewPager2 = view.findViewById<ViewPager2>(R.id.carousel_vp)
//        val carouselTablayout = view.findViewById<TabLayout>(R.id.carousel_tablayout)
//
//        var carouselHolderLayout = view.findViewById<LinearLayout>(R.id.carousel_layout)
//        val carouselShimmerLayout = view.findViewById<LinearLayout>(R.id.carousel_shimmer_layout)
//        val carouselShimmer = view.findViewById<ShimmerFrameLayout>(R.id.carousel_shimmer)

        fun bindCarouselItems(carouselItemList: List<CarouselModel>) {

             val carouselAdapter = CarouselAdapter(context, carouselItemListener)
                if (carouselItemList.isNullOrEmpty()) {
                    "list of carousel is empty/null".logs()

                    binding.carouselShimmerLayout.visibility = View.GONE
                    binding.carouselShimmer.stopShimmer()
                    "carousel layout visible.".logs()
                    binding.carouselLayout.visibility = View.GONE
//                        carouselHolder.carouselViewPager2.visibility = View.GONE
                }
                else {
                    binding.carouselShimmerLayout.visibility = View.GONE
                    binding.carouselShimmer.stopShimmer()
                    "carousel layout visible.".logs()
                    binding.carouselLayout.visibility = View.VISIBLE
                    val newHeightInDp = 242 // Replace with your desired height in dp
                    val newHeightInPixels = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        newHeightInDp.toFloat(),
                        context.resources.displayMetrics
                    ).toInt()

                    val layoutParams = binding.carouselVp.layoutParams
                    layoutParams.height = newHeightInPixels
                    binding.carouselVp.layoutParams = layoutParams
//                        carouselHolder.carouselViewPager2.visibility = View.VISIBLE

                    carouselAdapter!!.additems(carouselItemList)
                    binding.carouselVp.adapter = carouselAdapter

                    TabLayoutMediator(
                        binding.carouselTablayout,
                        binding.carouselVp
                    ) { tab, pos ->
                        //Some implementation
                    }.attach()

                    binding.carouselVp.clipToPadding = false
                    binding.carouselVp.setPadding(0,0,0,0)
                    binding.carouselVp.clipChildren = false
                    binding.carouselVp.offscreenPageLimit = 3
                    binding.carouselVp.getChildAt(0).overScrollMode =
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
                                binding.carouselVp.adapter?.itemCount ?: 0
                            lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
                            nextItem =
                                if ( binding.carouselVp.currentItem == lastIndex) 0 else  binding.carouselVp.currentItem + 1
                            binding.carouselVp.setCurrentItem(nextItem, true)
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
                                binding.carouselVp.adapter?.itemCount ?: 0
                            lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
                            nextItem =
                                if (binding.carouselVp.currentItem == lastIndex) 0 else binding.carouselVp.currentItem + 1
                            binding.carouselVp.setCurrentItem(nextItem, true)
//                                Utils.log("inside carousel handler nextItem: " + nextItem)
                        }
                    }

                    binding.carouselVp.setPageTransformer(
                        compositePageTransformer
                    )


                    binding.carouselVp.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)

                            if (carouselAdapter != null) {
                                binding.carouselTablayout.visibility = View.VISIBLE
                            }
                            handler.removeCallbacks(slideHandler)
                            handler.postDelayed(slideHandler, 3000)
                        }
                    })

                }

            }


    }

    inner class SeriesItemViewHolder(private val binding: SeriesCollectionVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val title = view.findViewById<TextView>(R.id.title)
//        val viewAll = view.findViewById<TextView>(R.id.view_all)
//        val verticalVideosRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        init {
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        }

        val childSeriesAdapter = SeriesVideoVerticalAdapter(context, listener)
        fun bingCollage(SeriesItemList: List<Series>) {
            binding.title.text = "Series 1" // listItem.title

            //  seriesVerticalHolder.viewAll
//            seriesVerticalHolder.viewAll.setOnClickListener {
//                seriesVerticalVideoList?.toMutableList()
//                    ?.let { it1 -> listener.onSeriesItemClick(listItem,position, it1) }
//
//            }
            if (SeriesItemList.isNullOrEmpty()) {
                "seriesVerticalVideoList is Empty".logs()
            } else {
                "HomepageResponse : ${SeriesItemList}".logs()
                binding.recyclerView.adapter = childSeriesAdapter
                childSeriesAdapter.updateItems(SeriesItemList)
            }

        }
    }



//    inner class BannerItemViewHolder(private val binding: BannerItemLayoutBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bindBannerView(banner:TemplateItem?) {
//            Glide.with(binding.root.context)
//                .load(banner?.image_url)
//                .into(binding.bannerIv)
//            //  binding.bannerIv.setImageResource(banner.image_url)
//        }
//
//    }
//
//
//    inner class FullScreenBannerItemViewHolder(private val binding: FullScreenBannerItemLayoutBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bindFullScreenBannerView(fullScreenBanner: TemplateItem?) {
//            Glide.with(binding.root.context)
//                .load(fullScreenBanner?.image_url)
//                .into(binding.fullscreenBannerIv)
//            // binding.fullscreenBannerIv.setImageResource(fullScreenBanner.image)
//        }
//
//    }
//    inner class FullBannerItemViewHolder(private val binding: FullBannerItemLayoutBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bindFullBanner(fullBanner: TemplateItem?) {
//            Glide.with(binding.root.context)
//                .load(fullBanner?.image_url)
//                .into(binding.fullBannerIv)
////            val adapter = TopShowsAdapter(DataItemType.CLOTHING, topShowsItemList)
////            binding.topShowsRecyclerView.adapter = adapter
//        }
//
//    }
//
//    inner class CollageItemViewHolder(private val binding: CollageItemListLayoutBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        init {
//            binding.collageListRecyclerView.setHasFixedSize(true)
//            binding.collageListRecyclerView.layoutManager =
//                LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
//        }
//
//        fun bingCollage(collageItemList: List<TemplateItem>) {
//            val adapter = CollageChildAdapter( collageItemList)
//            binding.collageListRecyclerView.adapter = adapter
//        }
//
//    }

        override fun onItemClick(item: Any, position: Int, action: String) {
            TODO("Not yet implemented")
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

        override fun onItemClickListener(item: Any, position: Int, data: MutableList<Episode>) {
            listener?.onItemClickListener(item, position,data)
        }


        override fun onSeriesItemClick(item: Any, position: Int, data: MutableList<Series>) {
            listener?.onSeriesItemClick(item, position,data)
        }

    }

