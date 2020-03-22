package net.xblacky.animexstream.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import net.xblacky.animexstream.R
import net.xblacky.animexstream.ui.main.home.epoxy.HomeController
import net.xblacky.animexstream.utils.model.AnimeMetaModel

class HomeFragment : Fragment(), View.OnClickListener, HomeController.EpoxyAdapterCallbacks{


    private lateinit var rootView:View
    private lateinit var homeController: HomeController
    private var doubleClickLastTime = 0L
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootView =  inflater.inflate(R.layout.fragment_home, container, false)
        setAdapter()
        setClickListeners()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModelObserver()
    }

    private fun setAdapter(){
        homeController = HomeController(this)

        homeController.isDebugLoggingEnabled = true
        val  homeRecyclerView = rootView.recyclerView
        homeRecyclerView.layoutManager = LinearLayoutManager(context)
        homeRecyclerView.adapter = homeController.adapter
    }

    private fun viewModelObserver(){
        viewModel.animeList.observe(viewLifecycleOwner,  Observer {
            homeController.setData(it)
            recyclerView.smoothScrollToPosition(0)
        })
    }

    private fun setClickListeners(){
        rootView.header.setOnClickListener(this)
        rootView.search.setOnClickListener(this)
        rootView.favorite.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.header -> {
                doubleClickLastTime = if (System.currentTimeMillis() - doubleClickLastTime < 300) {
                    rootView.recyclerView.smoothScrollToPosition(0)
                    0L
                } else {
                    System.currentTimeMillis()
                }

            }
            R.id.search->{
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
            R.id.favorite->{
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavouriteFragment())
            }
        }
    }

    override fun recentSubDubEpisodeClick(model: AnimeMetaModel) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToVideoPlayerActivity(episodeUrl = model.episodeUrl, animeName = model.title, episodeNumber = model.episodeNumber))
    }

    override fun animeTitleClick(model: AnimeMetaModel) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAnimeInfoFragment(categoryUrl = model.categoryUrl))
    }

}