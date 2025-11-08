package com.zen.videoplayertestapp.ui.fragments



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.zen.videoplayertestapp.R
import com.zen.videoplayertestapp.data.repo.Repository
import com.zen.videoplayertestapp.data.repo.RepositoryImp
import com.zen.videoplayertestapp.ui.adapter.SeriesCollectionAdapter
import com.zen.videoplayertestapp.ui.viewmodels.CentralizeViewmodel
import com.zen.videoplayertestapp.ui.viewmodels.CentralizedViewmodelFactory

class AllCollectionsFragment : Fragment(),RecyclerviewClickListener {
    // 1. Get the concrete implementation instance
    private val minisRepo: Repository = RepositoryImp()

    // 2. Instantiate the Factory, providing the repo instance
    private val viewModelFactory = CentralizedViewmodelFactory(minisRepo)

    // 3. Use the by viewModels delegate with the Factory
    private val viewModel: CentralizeViewmodel by activityViewModels { viewModelFactory }
    lateinit var adapter: SeriesCollectionAdapter
    lateinit var listener: RecyclerviewClickListener
    lateinit var recyclerView: RecyclerView
    lateinit var seriesCollectionTitle:TextView
    lateinit var backButton :ImageView
    var dataList:MutableList<Series>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_collections, container, false)
        return view
    }
    fun initRV(){
        adapter = SeriesCollectionAdapter(requireContext(),this)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter
        adapter.updateItem(dataList)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seriesCollectionTitle = view.findViewById(R.id.appBarTitle)
        recyclerView = view.findViewById(R.id.serieCollectionRV)
        backButton = view.findViewById(R.id.backButton)
        initRV()
        backButton.setOnClickListener {
            findNavController().navigate(R.id.homeFragment, null, NavOptions.Builder()
                .setPopUpTo(R.id.allCollectionsFragment, true) // Replace `currentFragment` with the fragment ID you want to clear up to
                .build())
        }
        viewModel.seriesCollectionList.observe(viewLifecycleOwner){ data ->
            adapter.updateItem(data)

        }
        viewModel.seriesCollectionTitle.observe(viewLifecycleOwner){ title ->
            seriesCollectionTitle.text= title

        }


        //  viewModel.fetchRecommendationsData()
        /* viewModel.recommendationData.observe(viewLifecycleOwner){ response ->
             val dataItem = response?.data?.data
             if (dataItem != null) {
                 adapter.updateItem(dataItem)
             }

         }*/
    }

    override fun onItemClickListener(item: Any, position: Int, data: MutableList<Episode>) {
        TODO("Not yet implemented")
    }

    override fun onSeriesItemClick(item: Any, position: Int, data: MutableList<Series>) {
        findNavController().navigate(R.id.videoInfoFragment)

    }

    /* override fun onItemClickListener(item: Any, position: Int) {
         if (item is SeriesCollection){
             findNavController().navigate(R.id.seriesInfoFragment)
         }
     }

     override fun onSeriesItemClick(seriesId: String, position: Int) {
         TODO("Not yet implemented")
     }*/

}