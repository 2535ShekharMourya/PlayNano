package com.zen.videoplayertestapp.ui.fragments



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.brochill.minismodule.util.ClickListener
import com.zen.videoplayertestapp.R
import com.zen.videoplayertestapp.data.repo.Repository
import com.zen.videoplayertestapp.data.repo.RepositoryImp
import com.zen.videoplayertestapp.databinding.FragmentVideoRendringBinding
import com.zen.videoplayertestapp.ui.adapter.MinisPlayerAdapter
import com.zen.videoplayertestapp.ui.viewmodels.CentralizeViewmodel
import com.zen.videoplayertestapp.ui.viewmodels.CentralizedViewmodelFactory

class VideoRendringFragment : Fragment(), ClickListener {
    lateinit var adapter: MinisPlayerAdapter
    // lateinit var adapter: PlayerScrollPagerAdapter

    // 1. Get the concrete implementation instance
    private val minisRepo: Repository = RepositoryImp()

    // 2. Instantiate the Factory, providing the repo instance
    private val viewModelFactory = CentralizedViewmodelFactory(minisRepo)

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
                // Start playing after ViewPager is settled
                binding.viewpager.postDelayed({
                    adapter.playVideoAt(currentPosition)
                }, 1)
            }
        }

        binding.viewpager.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Pause all other videos
                adapter.pauseAllExcept(position)
                // Play the current video
                adapter.playVideoAt(position)
                currentPosition = position

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
        // Pause video when fragment is not visible
        adapter.pauseVideoAt(currentPosition)
    }

    override fun onResume() {
        super.onResume()
        adapter.playVideoAt(currentPosition)

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
}


