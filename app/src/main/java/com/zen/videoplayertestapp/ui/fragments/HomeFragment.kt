package com.zen.videoplayertestapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.CarouselModel
import com.brochill.minismodule.CarouselResponse
import com.zen.videoplayertestapp.R
import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.data.model.HorizontalItemResponse
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.data.model.VerticalItemResponse
import com.brochill.minismodule.util.LogStatus.Companion.logs
import com.brochill.minismodule.util.LogStatus.Companion.logs1
import com.brochill.minismodule.util.LogStatus.Companion.mLog
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.zen.videoplayertestapp.core.util.Utils
import com.zen.videoplayertestapp.data.repo.Repository
import com.zen.videoplayertestapp.data.repo.RepositoryImp
import com.zen.videoplayertestapp.ui.adapter.CarouselAdapter
import com.zen.videoplayertestapp.ui.adapter.MinisHomeAdapter
import com.zen.videoplayertestapp.ui.viewmodels.CentralizeViewmodel
import com.zen.videoplayertestapp.ui.viewmodels.CentralizedViewmodelFactory

class HomeFragment : Fragment(), RecyclerviewClickListener, CarouselAdapter.CarouselItemListener {
    lateinit var minisHomeAdapter: MinisHomeAdapter
    // 1. Get the concrete implementation instance
    private val minisRepo: Repository = RepositoryImp()

    // 2. Instantiate the Factory, providing the repo instance
    private val viewModelFactory = CentralizedViewmodelFactory(minisRepo)

    // 3. Use the by viewModels delegate with the Factory
    private val viewModel: CentralizeViewmodel by activityViewModels { viewModelFactory }
    lateinit var homeRecyclerview: RecyclerView
    lateinit var noInternet: LinearLayout
    lateinit var tryAgain:AppCompatButton
    var loaderIndex: Int = 0
    var carouselIndex: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "viewmodel class name ${viewModel.javaClass.name}".mLog()

        //   viewModel = ViewModelProvider(requireActivity())[MinisHomeViewModel::class.java]
        homeRecyclerview = view.findViewById<RecyclerView>(R.id.minis_home_rv)
        noInternet = view.findViewById(R.id.noInternet)
        tryAgain = view.findViewById(R.id.no_internet_tryagain_bt)

        initRecyclerView()

        tryAgain.setOnClickListener {
            if (Utils.isInternetAvailable(requireContext())){
                //
                noInternet.visibility = View.GONE
                callData()
                // Fetch recommendations only if not already fetched
                if (viewModel.carouselData.value == null) {
                    viewModel.fetchCarouselData()
                }
                if (viewModel.homepageData.value == null) {
                    viewModel.fetchHomepageData()
                }
            }else{
                noInternet.visibility = View.VISIBLE
            }
        }

        if (Utils.isInternetAvailable(requireContext())){
            //
            noInternet.visibility = View.GONE
            callData()
            // Fetch recommendations only if not already fetched
            if (viewModel.carouselData.value == null) {
                viewModel.fetchCarouselData()
            }
            if (viewModel.homepageData.value == null) {
                viewModel.fetchHomepageData()
            }
        }else{
            noInternet.visibility = View.VISIBLE
        }


    }

    fun callData() {
        carouselIndex = minisHomeAdapter.addCarouselItme()
        getCarouseldata()
        getHomePagedata()
        //  loaderIndex = minisHomeAdapter.showLoader()

    }

    fun getHomePagedata() {
        viewModel.homepageData.observe(viewLifecycleOwner) { response ->
            if (response?.data.isNullOrEmpty()) {
                "data is null".logs()
            } else {
                // minisHomeAdapter.hideLoader(loaderIndex)
                response?.data?.forEach { seriesItem ->
                    if (seriesItem.series_ui_type.equals("horizontal")) {
                        val list = mutableListOf<Episode>()
                        val index1 = minisHomeAdapter.addHorizontalItme()
                        minisHomeAdapter.notifyItemChanged(index1)
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
                            showHorizontalVideoItems(it, index1)
                        }


                    } else {
//                        (seriesItem.series_ui_type.equals("vertical"))
                        val list = mutableListOf<Series>()
                        val index2 = minisHomeAdapter.addVerticalItem()
                        minisHomeAdapter.notifyItemChanged(index2)
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
                            showVerticalVideoItems(it, index2)
                        }
                    }


                }
            }

        }
    }

    fun getCarouseldata() {
        viewModel.carouselData.observe(viewLifecycleOwner) { response ->
            "Carousel Res ${response}".logs1()
            if (response?.data.isNullOrEmpty()) {
                "data is null".logs()
            } else {
                minisHomeAdapter.notifyItemChanged(carouselIndex)
                showCarouselItem(response, carouselIndex)
            }
        }
    }

    fun initRecyclerView() {
        minisHomeAdapter = MinisHomeAdapter(requireContext(), this, this)
        homeRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        homeRecyclerview.adapter = minisHomeAdapter
    }

    fun showCarouselItem(response: CarouselResponse?, index: Int) {
        minisHomeAdapter.replaceCarouselItem(response, index)

    }

    fun showVerticalVideoItems(response: VerticalItemResponse?, index: Int) {
        "show vertical".logs()
        minisHomeAdapter.replaceVerticalItem(response, index)
    }

    fun showHorizontalVideoItems(response: HorizontalItemResponse, index: Int) {
        minisHomeAdapter.replaceHorizontalItem(response, index)
    }

    fun showRecyclerView() {
        homeRecyclerview.visibility = View.VISIBLE
    }

    fun hideRecyclerView() {
        homeRecyclerview.visibility = View.GONE
    }

    override fun onItemClickListener(item: Any, position: Int, data: MutableList<Episode>) {
        if (item is Episode) {
            if (item.access_type.equals("series")) {
                findNavController().navigate(R.id.videoInfoFragment)
            } else if (item.access_type.equals("episode")) {
                // viewModel.episodeList.value =data
                viewModel.setPosition(position)
                viewModel.episodeDataList.value = data
                "Episode Data: ${data}".logs1()
                val bundle = Bundle()
                bundle.apply {
                    //  putString("id",item.series_id)
                    putInt("position", position)

                }
                findNavController().navigate(R.id.videoRendringFragment, bundle)
            } else {
                findNavController().navigate(R.id.allCollectionsFragment)
            }

            viewModel.setData(item)
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
                if (data[0].series_id.equals("1ck7fvu3l30001i3s6bcxves5l")) {

                    viewModel.setKey(item.series_id)
                }
                "onSeriesItemClick".logs1()
                viewModel.seriesDataList.value = data
                viewModel.seriesItem.value = item
                findNavController().navigate(R.id.videoInfoFragment)
            } else if (item.access_type.equals("episode")) {
                findNavController().navigate(R.id.videoRendringFragment)
            } else {
                findNavController().navigate(R.id.allCollectionsFragment)
            }

        }
    }


    override fun onItemClick(item: Any, position: Int, action: String) {
        if (item is CarouselModel) {
            if (item.accessType.equals("series")) {
                viewModel.setCarouselData(item)
                findNavController().navigate(R.id.videoInfoFragment)
            } else if (item.accessType.equals("episode")) {
                val data = mutableListOf<Episode>()

                viewModel.episodeDataList.value = data

                viewModel.episodeItem.value = item
                viewModel.setCarouselData(item)
                findNavController().navigate(R.id.videoRendringFragment)
            } else {
                findNavController().navigate(R.id.allCollectionsFragment)
            }
        }


    }


}

