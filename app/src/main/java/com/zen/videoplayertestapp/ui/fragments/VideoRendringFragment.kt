package com.zen.videoplayertestapp.ui.fragments



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.brochill.minismodule.util.ClickListener
import com.zen.videoplayertestapp.R
import com.zen.videoplayertestapp.core.networkmodule.RetrofitAPIClient
import com.zen.videoplayertestapp.data.local.room.ShortsSeriesDatabase
import com.zen.videoplayertestapp.data.local.room.ShortsSeriesLocalDataSourceImp
import com.zen.videoplayertestapp.data.remote.ShortsSeriesRemoteDataSource
import com.zen.videoplayertestapp.data.remote.ShortsSeriesRemoteDataSourceImp
import com.zen.videoplayertestapp.data.repo.Repository
import com.zen.videoplayertestapp.data.repo.RepositoryImp
import com.zen.videoplayertestapp.databinding.FragmentVideoRendringBinding
import com.zen.videoplayertestapp.ui.adapter.MinisPlayerAdapter
import com.zen.videoplayertestapp.ui.viewmodels.CentralizeViewmodel
import com.zen.videoplayertestapp.ui.viewmodels.CentralizedViewmodelFactory

class VideoRendringFragment : Fragment(), ClickListener {
    lateinit var adapter: MinisPlayerAdapter

    // 1. Create a Repository instance
    private val shortsSeriesRepo: Repository by lazy {
        val remoteDataSource: ShortsSeriesRemoteDataSource = ShortsSeriesRemoteDataSourceImp(
            RetrofitAPIClient.getInstance())
        val localDataSource = ShortsSeriesLocalDataSourceImp(ShortsSeriesDatabase.getInstance(requireContext()).shortsSeriesDao())
        RepositoryImp(remoteDataSource, localDataSource)
    }

    // 2. Create a Factory instance
    private val viewModelFactory by lazy {
        CentralizedViewmodelFactory(shortsSeriesRepo)
    }

    // 3. Use the by viewModels delegate with the Factory
    private val viewModel: CentralizeViewmodel by activityViewModels { viewModelFactory }

    private var currentPosition = 0

    lateinit var binding: FragmentVideoRendringBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoRendringBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        viewModel.episodeDataList.observe(viewLifecycleOwner) { data ->
            adapter.addItem(data)
            viewModel.position?.let {
                currentPosition = it
                binding.viewpager.setCurrentItem(it, false)
            }
        }

        binding.viewpager.registerOnPageChangeCallback( object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
                val rv = binding.viewpager.getChildAt(0) as? RecyclerView
                var holder = rv?.findViewHolderForAdapterPosition(position) as? MinisPlayerAdapter.VideoViewHolder

                if (holder != null) {
                    holder.playVideo()
                } else {
                    rv?.post {
                        holder = rv.findViewHolderForAdapterPosition(position) as? MinisPlayerAdapter.VideoViewHolder
                        holder?.playVideo()
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when(state){
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        playVideo()
                    }
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        pauseVideo()
                    }
                    ViewPager2.SCROLL_STATE_SETTLING -> {
                    }
                }
            }

        }
        )
    }

    private fun setUpViewPager() {
        adapter = MinisPlayerAdapter(requireContext(), this)
        binding.viewpager.adapter = adapter
        // Important: Set offscreen page limit to keep adjacent pages in memory

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up all players
        binding.viewpager.adapter = null
    }

    override fun backClick() {
        findNavController().navigate(
            R.id.videoInfoFragment, null,
            NavOptions.Builder()
                .setPopUpTo(
                    R.id.videoRendringFragment,
                    true
                ) // Replace `currentFragment` with the fragment ID you want to clear up to
                .build()
        )
        findNavController().popBackStack()
    }

    fun playVideo(){
        val recyclerView = binding.viewpager.getChildAt(0) as? RecyclerView
        val holder = recyclerView?.findViewHolderForAdapterPosition(currentPosition) as? MinisPlayerAdapter.VideoViewHolder
        holder?.playVideo()
    }
    fun pauseVideo(){
        val recyclerView = binding.viewpager.getChildAt(0) as? RecyclerView
        val holder = recyclerView?.findViewHolderForAdapterPosition(currentPosition) as? MinisPlayerAdapter.VideoViewHolder
        holder?.pauseVideo()
    }
}


