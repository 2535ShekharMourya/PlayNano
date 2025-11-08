package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.data.model.HorizontalItemResponse
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.data.model.SeriesepisodesResponse
import com.brochill.minismodule.data.model.VerticalItemResponse
import com.brochill.minismodule.util.LogStatus.Companion.logs
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.zen.videoplayertestapp.R

class RecommendationsAdapter(val context: Context,val listener: RecyclerviewClickListener) :
    RecyclerView.Adapter<RecommendationsAdapter.RecommendationsViewholder>(),RecyclerviewClickListener {
    companion object {
        const val TYPE_VERTICAL = "type_vertical"
        const val TYPE_HORIZONTAL = "type_horizontal"
        const val TYPE_EPISODES = "type_episodes"
        const val SHOW_LOADER = "show_loader"
    }

    private var seriesVideoVerticalAdapter: RecommendationVerticalItemsAdapter? = null
    private var seriesCollectionHorizontalAdapter: RecommendationHorizontalItemsAdapter? = null
    private var seriesEpisodesAdapter: SeriesEpisodesAdapter? = null
    private var data = mutableListOf<Any>()


    override fun getItemViewType(position: Int): Int {
        val dataItem = data[position]
        return when (dataItem) {
            is String -> {
                if (dataItem.equals(TYPE_EPISODES)){
                    return 0
                }
                else if (dataItem.equals(TYPE_HORIZONTAL)) {
                    return 1
                } else if (dataItem.equals(TYPE_VERTICAL)) {
                    return 2
                } else return -1

            }
            is SeriesepisodesResponse -> {
                return 0
            }

            is HorizontalItemResponse -> {
                1
            }

            is VerticalItemResponse -> {
                2
            }

            else -> {
                -1
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationsViewholder {
        return when (viewType) {
            0 ->{
                val episodesLayout = LayoutInflater.from(context)
                    .inflate(R.layout.series_episode_collection, parent, false)
                return EpisodesViewHolder(episodesLayout)
            }
            1 -> {
                "onCreateViewHolder: seriesCollectionHorizontal".logs()
                val seriesCollectionHorizontalLayout = LayoutInflater.from(context)
                    .inflate(R.layout.series_collection_horizontal, parent, false)
                return SeriesCollectionHorizontalViewHolder(seriesCollectionHorizontalLayout)

            }

            2 -> {
                "onCreateViewHolder: seriesCollectionVertical".logs()
                val seriesVideoVerticalLayout = LayoutInflater.from(context)
                    .inflate(R.layout.series_collection_vertical, parent, false)
                return SeriesVideoVerticalViewHolder(seriesVideoVerticalLayout)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(
        holder: RecommendationsViewholder,
        position: Int
    ) {
        val listItem = data[position]
        when (holder.itemViewType) {
            0 ->{
                if (listItem is SeriesepisodesResponse){

                        "HomepageResponse 1 : ${listItem}".logs()
                        val seriesEpisodesHolder = holder as EpisodesViewHolder
                      //  seriesEpisodesHolder.title.text = listItem.
                        val seriesEpisodesResponse = listItem
                        val seriesEpisodesList = seriesEpisodesResponse.data
                        if (seriesEpisodesList.isNullOrEmpty()) {
                            "seriesVerticalVideoList is Empty".logs()
                        } else {
                            "HomepageResponse : ${seriesEpisodesList}".logs()
                            val layoutManager =
                                GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
                            seriesEpisodesAdapter = SeriesEpisodesAdapter(context, this)
                            seriesEpisodesHolder.verticalVideosRecyclerView.layoutManager =
                                layoutManager
                            seriesEpisodesHolder.verticalVideosRecyclerView.adapter =
                                seriesEpisodesAdapter
                            seriesEpisodesAdapter?.updateItems(seriesEpisodesList)



                    }

                }
            }
            2 -> {
                if (listItem is VerticalItemResponse) {
                    "HomepageResponse 1 : ${listItem}".logs()
                    val seriesVerticalHolder = holder as SeriesVideoVerticalViewHolder
                    seriesVerticalHolder.title.text = listItem.title
                    seriesVerticalHolder.viewAll.visibility = View.VISIBLE

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
                        seriesVideoVerticalAdapter = RecommendationVerticalItemsAdapter(context, this)
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
                            RecommendationHorizontalItemsAdapter(context, this)
                        seriesVerticalHolder.HorizontalCollectionRecyclerView.layoutManager =
                            layoutManager
                        seriesVerticalHolder.HorizontalCollectionRecyclerView.adapter =
                            seriesCollectionHorizontalAdapter
                        seriesCollectionHorizontalAdapter?.additems(seriesHorizontalVideoList)

                    }

                }
            }

        }
    }

    override fun getItemCount(): Int {
      return data.size
    }

    open class RecommendationsViewholder(view: View) : RecyclerView.ViewHolder(view) {}
    inner class EpisodesViewHolder(view: View) : RecommendationsViewholder(view) {
        val verticalVideosRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)


    }
    inner class SeriesVideoVerticalViewHolder(view: View) : RecommendationsViewholder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val viewAll = view.findViewById<TextView>(R.id.view_all)
        val verticalVideosRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)


    }

    inner class SeriesCollectionHorizontalViewHolder(view: View) : RecommendationsViewholder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val viewAll = view.findViewById<TextView>(R.id.view_all)
        val HorizontalCollectionRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

    }
    fun addEpisodesItem(): Int {
        val addingAtIndex = data.size
        "data size before VERTICAL_ITEM: ${data.size}".logs()
        data.add(TYPE_EPISODES)
        "data size after VERTICAL_ITEM: ${data.size}".logs()
        return addingAtIndex
    }
    fun addVerticalItem(): Int {
        val addingAtIndex = data.size
        "data size before VERTICAL_ITEM: ${data.size}".logs()
        data.add(MinisHomeAdapter.TYPE_SERIES)
        "data size after VERTICAL_ITEM: ${data.size}".logs()
        return addingAtIndex
    }

    fun addHorizontalItme(): Int {
        val addingAtIndex = data.size
        "data size before HORIZONTAL_ITEM: ${data.size}".logs()
        data.add(MinisHomeAdapter.TYPE_EPISODE)
        "data size after HORIZONTAL_ITEM: ${data.size}".logs()
        return addingAtIndex
    }
    fun replaceEpisodesItem(homepageResponse: SeriesepisodesResponse?, index: Int) {
        if (data.size > 0) {

            if (homepageResponse != null) {
                data[index] = homepageResponse
            }

            notifyItemChanged(index)
        }

    }
    fun replaceVerticalItem(homepageResponse: VerticalItemResponse?, index: Int) {
        if (data.size > 0) {

            if (homepageResponse != null) {
                data[index] = homepageResponse
            }

            notifyItemChanged(index)
        }

    }
    fun replaceHorizontalItem(homepageResponse: HorizontalItemResponse, index: Int) {

        if (data.size > 0) {

            data[index] = homepageResponse

            notifyItemChanged(index)
        }
    }

    override fun onItemClickListener(item: Any, position: Int,data:MutableList<Episode>) {
        listener?.onItemClickListener(item, position,data)
    }

    override fun onSeriesItemClick(item: Any, position: Int,data:MutableList<Series>) {
        listener.onSeriesItemClick(item,position,data)

    }


}