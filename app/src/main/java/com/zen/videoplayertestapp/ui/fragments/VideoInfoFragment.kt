package com.zen.videoplayertestapp.ui.fragments


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.CarouselModel
import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.data.model.HorizontalItemResponse
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.data.model.VerticalItemResponse
import com.brochill.minismodule.util.LogStatus.Companion.logs
import com.brochill.minismodule.util.LogStatus.Companion.logs1
import com.brochill.minismodule.util.LogStatus.Companion.mLog
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zen.videoplayertestapp.R
import com.zen.videoplayertestapp.data.repo.Repository
import com.zen.videoplayertestapp.data.repo.RepositoryImp
import com.zen.videoplayertestapp.ui.adapter.CarouselAdapter
import com.zen.videoplayertestapp.ui.adapter.RecommendationsAdapter
import com.zen.videoplayertestapp.ui.viewmodels.CentralizeViewmodel
import com.zen.videoplayertestapp.ui.viewmodels.CentralizedViewmodelFactory


class VideoInfoFragment : Fragment(), RecyclerviewClickListener,
    CarouselAdapter.CarouselItemListener,
    MotionLayout.TransitionListener {
    lateinit var bannerImage: ImageView
    lateinit var back: ImageView
    lateinit var seriesName: TextView
    lateinit var recommendationsAdapter: RecommendationsAdapter

    // 1. Get the concrete implementation instance
    private val minisRepo: Repository = RepositoryImp()

    // 2. Instantiate the Factory, providing the repo instance
    private val viewModelFactory = CentralizedViewmodelFactory(minisRepo)

    // 3. Use the by viewModels delegate with the Factory
    private val viewModel: CentralizeViewmodel by activityViewModels { viewModelFactory }
    lateinit var playNow: ConstraintLayout
    lateinit var gridRecyclerView: RecyclerView
    private var start: Int = 0
    lateinit var shadow: View

    //  lateinit var seriesNameInfo:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewModel = ViewModelProvider(requireActivity())[MinisHomeViewModel::class.java]
        "viewmodel class name ${viewModel.javaClass.name}".mLog()

        // val motionLayout: MotionLayout = view.findViewById(R.id.motion_layout)
        bannerImage = view.findViewById(R.id.bannerImage)
        seriesName = view.findViewById(R.id.seriesName)
        playNow = view.findViewById(R.id.playNow)
        back = view.findViewById(R.id.back_iv)
        shadow = view.findViewById(R.id.shado)
        val motionLayout = view.findViewById<MotionLayout>(R.id.motion_layout)
        gridRecyclerView = view.findViewById(R.id.recycler_view)
        back.setOnClickListener {
            findNavController().popBackStack()
        }
        iniatRecyclerView()

        if (viewModel.keymatching.equals("1ck7fvu3l30001i3s6bcxves5l")) {
            if (viewModel.seriesEpisodesData2.value == null) {
                viewModel.fetchSeriesEpisodesData2()
            }
        } else {
            if (viewModel.seriesEpisodesData.value == null) {
                viewModel.fetchSeriesEpisodesData()
            }
        }

        if (viewModel.recommendationData.value == null) {
            viewModel.fetchRecommendationsData()
        }
        getData()

        playNow.setOnClickListener {
            val bundle = Bundle()
            bundle.apply {

            }
            findNavController().navigate(R.id.videoRendringFragment)

        }

        viewModel.fetchSeriesInfoData()

        getSeriesInfoData()

    }

    fun getSeriesInfoData() {
        viewModel.seriesInfoData.observe(viewLifecycleOwner) { response ->
            Glide.with(this)
                .asBitmap()
                .load(response?.data?.get(0)?.thumb_image_bs2_url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        // Set the loaded image to the ImageView
                        bannerImage.setImageBitmap(resource)

                        // Generate a Palette from the image
                        Palette.from(resource).generate { palette ->
                            val vibrantColor = palette?.vibrantSwatch?.rgb ?: 0x000000
                            val darkVibrantColor = palette?.darkVibrantSwatch?.rgb ?: 0x000000

                            // Add transparency (adjust alpha channel)
                            val vibrantColorTransparent =
                                vibrantColor and 0x00FFFFFF or (0xF2000000.toInt()) // 90% transparency
                            val darkVibrantColorTransparent =
                                darkVibrantColor and 0x00FFFFFF or (0x00000000) // Fully transparent

                            // Create a gradient drawable
                            val gradientDrawable = GradientDrawable(
                                GradientDrawable.Orientation.BOTTOM_TOP,
                                intArrayOf(vibrantColorTransparent, darkVibrantColorTransparent)
                            )

                            // Set the gradient as the background of the overlay
                            shadow.background = gradientDrawable
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle cleanup
                    }
                })
            seriesName.text = response?.data?.get(0)?.title
        }
    }

    fun getData() {
        if (viewModel.keymatching.equals("1ck7fvu3l30001i3s6bcxves5l")) {
            viewModel.seriesEpisodesData2.observe(viewLifecycleOwner) { response ->
                val index1 = recommendationsAdapter.addEpisodesItem()
                recommendationsAdapter.notifyItemChanged(index1)
                recommendationsAdapter.replaceEpisodesItem(response, index1)
                viewModel.recommendationData.observe(viewLifecycleOwner) { response ->
                    response?.data?.forEach { seriesItem ->
                        if (seriesItem.series_ui_type.equals("horizontal")) {
                            val list = mutableListOf<Episode>()
                            val index1 = recommendationsAdapter.addHorizontalItme()
                            recommendationsAdapter.notifyItemChanged(index1)
                            seriesItem.episodes.forEach { item ->
                                val data = Episode(
                                    access_type = item.access_type,
                                    created_at = item.created_at,
                                    dynamic_link = item.dynamic_link,
                                    media_file_url = item.media_file_url,
                                    is_active = item.is_active,
                                    is_archived = item.is_archived,
                                    language_id = item.language_id,
                                    languages_info = item.languages_info,
                                    episode_id = item.episode_id,
                                    series_ui_type = item.series_ui_type,
                                    show_title = item.show_title,
                                    status = item.status,
                                    sub_titles_info = item.sub_titles_info,
                                    thumb_image_bs2_url = item.thumb_image_bs2_url,
                                    vertical_thumb_bs2_url = item.vertical_thumb_bs2_url,
                                    title = item.title,
                                    total_shares = item.total_shares,
                                    total_views = item.total_views,
                                    type = item.type,
                                    updated_at = item.updated_at

                                )
                                list.add(data)


                            }
                            HorizontalItemResponse(
                                title = seriesItem.title,
                                episodes = list,
                                access_type = seriesItem.access_type
                            ).also {
                                recommendationsAdapter.replaceHorizontalItem(it, index1)
                                // showHorizontalVideoItems(it, index1)
                            }


                        } else {
//                        (seriesItem.series_ui_type.equals("vertical"))
                            val list = mutableListOf<Series>()
                            val index2 = recommendationsAdapter.addVerticalItem()
                            recommendationsAdapter.notifyItemChanged(index2)
                            seriesItem.series.forEach { item ->
                                val data = Series(
                                    access_type = item.access_type,
                                    created_at = item.created_at,
                                    dynamic_link = item.dynamic_link,
                                    is_active = item.is_active,
                                    is_archived = item.is_archived,
                                    language_id = item.language_id,
                                    languages_info = item.languages_info,
                                    series_id = item.series_id,
                                    series_ui_type = item.series_ui_type,
                                    show_title = item.show_title,
                                    status = item.status,
                                    sub_titles_info = item.sub_titles_info,
                                    thumb_image_bs2_url = item.thumb_image_bs2_url,
                                    vertical_thumb_bs2_url = item.vertical_thumb_bs2_url,
                                    title = item.title,
                                    total_shares = item.total_shares,
                                    total_views = item.total_views,
                                    type = item.type,
                                    updated_at = item.updated_at
                                )
                                list.add(data)
                            }
                            VerticalItemResponse(
                                title = seriesItem.title,
                                series = list,
                                access_type = seriesItem.access_type
                            ).also {
                                recommendationsAdapter.replaceVerticalItem(it, index2)
                                //showVerticalVideoItems(it, index2)
                            }
                        }


                    }
                }
            }
        } else {
            viewModel.seriesEpisodesData.observe(viewLifecycleOwner) { response ->
                val index1 = recommendationsAdapter.addEpisodesItem()
                recommendationsAdapter.notifyItemChanged(index1)
                recommendationsAdapter.replaceEpisodesItem(response, index1)

                viewModel.recommendationData.observe(viewLifecycleOwner) { response ->
                    response?.data?.forEach { seriesItem ->
                        if (seriesItem.series_ui_type.equals("horizontal")) {
                            val list = mutableListOf<Episode>()
                            val index1 = recommendationsAdapter.addHorizontalItme()
                            recommendationsAdapter.notifyItemChanged(index1)
                            seriesItem.episodes.forEach { item ->
                                val data = Episode(
                                    access_type = item.access_type,
                                    created_at = item.created_at,
                                    dynamic_link = item.dynamic_link,
                                    media_file_url = item.media_file_url,
                                    is_active = item.is_active,
                                    is_archived = item.is_archived,
                                    language_id = item.language_id,
                                    languages_info = item.languages_info,
                                    episode_id = item.episode_id,
                                    series_ui_type = item.series_ui_type,
                                    show_title = item.show_title,
                                    status = item.status,
                                    sub_titles_info = item.sub_titles_info,
                                    thumb_image_bs2_url = item.thumb_image_bs2_url,
                                    vertical_thumb_bs2_url = item.vertical_thumb_bs2_url,
                                    title = item.title,
                                    total_shares = item.total_shares,
                                    total_views = item.total_views,
                                    type = item.type,
                                    updated_at = item.updated_at

                                )
                                list.add(data)


                            }
                            HorizontalItemResponse(
                                title = seriesItem.title,
                                episodes = list,
                                access_type = seriesItem.access_type
                            ).also {
                                recommendationsAdapter.replaceHorizontalItem(it, index1)
                                // showHorizontalVideoItems(it, index1)
                            }


                        } else {
//                        (seriesItem.series_ui_type.equals("vertical"))
                            val list = mutableListOf<Series>()
                            val index2 = recommendationsAdapter.addVerticalItem()
                            recommendationsAdapter.notifyItemChanged(index2)
                            seriesItem.series.forEach { item ->
                                val data = Series(
                                    access_type = item.access_type,
                                    created_at = item.created_at,
                                    dynamic_link = item.dynamic_link,
                                    is_active = item.is_active,
                                    is_archived = item.is_archived,
                                    language_id = item.language_id,
                                    languages_info = item.languages_info,
                                    series_id = item.series_id,
                                    series_ui_type = item.series_ui_type,
                                    show_title = item.show_title,
                                    status = item.status,
                                    sub_titles_info = item.sub_titles_info,
                                    thumb_image_bs2_url = item.thumb_image_bs2_url,
                                    vertical_thumb_bs2_url = item.vertical_thumb_bs2_url,
                                    title = item.title,
                                    total_shares = item.total_shares,
                                    total_views = item.total_views,
                                    type = item.type,
                                    updated_at = item.updated_at
                                )
                                list.add(data)
                            }
                            VerticalItemResponse(
                                title = seriesItem.title,
                                series = list,
                                access_type = seriesItem.access_type
                            ).also {
                                recommendationsAdapter.replaceVerticalItem(it, index2)
                                //showVerticalVideoItems(it, index2)
                            }
                        }


                    }
                }
            }
        }

    }

    override fun onSeriesItemClick(item: Any, position: Int, data: MutableList<Series>) {
        if (item is VerticalItemResponse) {
            viewModel.seriesCollectionList.value = data

            viewModel.seriesCollectionTitle.value = item.title
            findNavController().navigate(R.id.allCollectionsFragment)
        }
        if (item is Series) {
            if (item.access_type.equals("series")) {
                viewModel.seriesDataList.value = data
                viewModel.seriesItem.value = item
                "onSeriesItemClick".logs1()
                findNavController().navigate(R.id.videoInfoFragment)
            } else if (item.access_type.equals("episode")) {
                findNavController().navigate(R.id.videoRendringFragment)
            } else {
                findNavController().navigate(R.id.allCollectionsFragment)
            }

        }
    }


    override fun onItemClickListener(item: Any, position: Int, data: MutableList<Episode>) {
        "SeriesInfoFragment: RecyclerviewClickListener".logs()
        if (item is Episode) {
            "SeriesInfoFragment: RecyclerviewClickListener Episode clicked".mLog()
            viewModel.setPosition(position)
            viewModel.episodeDataList.value = data

            "SeriesInfoFragment: RecyclerviewClickListener Episode data ${viewModel.episodeDataList.value} ".mLog()


            val bundle = Bundle()
            bundle.apply {

            }
            findNavController().navigate(R.id.videoRendringFragment)
        }
        if (item is Series) {
            findNavController().navigate(R.id.videoInfoFragment)

        }


    }

    fun iniatRecyclerView() {
        recommendationsAdapter = RecommendationsAdapter(requireContext(), this)
        gridRecyclerView.adapter = recommendationsAdapter
    }

    override fun onItemClick(item: Any, position: Int, action: String) {
        if (item is CarouselModel) {
            val bundle = Bundle()
            bundle.apply {
                putString("BANNER_IMAGE", item.thumbImageBs2Url)
                putString("SERIES_NAME", item.title)
            }
            findNavController().navigate(R.id.videoInfoFragment, bundle)
        }
    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        start = startId
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        TODO("Not yet implemented")
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        TODO("Not yet implemented")
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
        TODO("Not yet implemented")
    }
}
