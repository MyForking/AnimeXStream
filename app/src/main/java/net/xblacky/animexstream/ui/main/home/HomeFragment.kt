package net.xblacky.animexstream.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.recycler_home_header.view.*
import net.xblacky.animexstream.R
import net.xblacky.animexstream.ui.main.home.epoxy.HomeController
import net.xblacky.animexstream.ui.main.search.SearchActivity

class HomeFragment : Fragment(), View.OnClickListener{


    private lateinit var rootView:View
    private lateinit var homeController: HomeController
    private var doubleClickLastTime = 0L
    companion object {
        fun newInstance() = HomeFragment()
    }
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
        viewModel.fetchHomeList()
        viewModelObserver()
    }

    private fun setAdapter(){
        homeController = HomeController()

        homeController.isDebugLoggingEnabled = true
        val  homeRecyclerView = rootView.homeRecyclerView
        homeRecyclerView.layoutManager = LinearLayoutManager(context)
        homeRecyclerView.adapter = homeController.adapter
    }

    private fun viewModelObserver(){
        viewModel.animeList.observe(viewLifecycleOwner,  Observer {
            homeController.setData(it)
            homeRecyclerView.smoothScrollToPosition(0)
        })
    }

    private fun setClickListeners(){
        rootView.header.setOnClickListener(this)
        rootView.search.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.header -> {
                doubleClickLastTime = if (System.currentTimeMillis() - doubleClickLastTime < 300) {
                    rootView.homeRecyclerView.smoothScrollToPosition(0)
                    0L
                } else {
                    System.currentTimeMillis()
                }

            }
            R.id.search->{
                val intent = Intent(context, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

}