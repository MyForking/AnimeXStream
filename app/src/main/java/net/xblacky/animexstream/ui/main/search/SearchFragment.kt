package net.xblacky.animexstream.ui.main.search

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search_placeholder.*
import kotlinx.android.synthetic.main.fragment_search_placeholder.view.*
import net.xblacky.animexstream.R
import net.xblacky.animexstream.ui.main.search.epoxy.SearchController
import net.xblacky.animexstream.utils.ItemOffsetDecoration
import net.xblacky.animexstream.utils.model.AnimeMetaModel


class SearchFragment : Fragment(), View.OnClickListener, SearchController.EpoxySearchAdapterCallbacks{

    private lateinit var rootView: View
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchController: SearchController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_search, container, false)
        setOnClickListeners()
        setAdapters()
        setRecyclerViewScroll()
        setEditTextListener()
        showKeyBoard()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        setObserver()
    }

    private fun setEditTextListener(){
        rootView.searchEditText.requestFocus()
        rootView.searchEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyBoard()
                rootView.searchEditText.clearFocus()
                viewModel.fetchSearchList(v.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            searchController.spanCount =5
            (rootView.searchRecyclerView.layoutManager as GridLayoutManager).spanCount = 5
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            searchController.spanCount = 3
            (rootView.searchRecyclerView.layoutManager as GridLayoutManager).spanCount = 3
        }

    }

    private fun setOnClickListeners(){
        rootView.backButton.setOnClickListener(this)
    }

    private fun setAdapters(){
        searchController = SearchController(this)
        searchController.spanCount = 3
        rootView.searchRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = searchController.adapter
            (layoutManager as GridLayoutManager).spanSizeLookup = searchController.spanSizeLookup
        }
        rootView.searchRecyclerView.addItemDecoration(ItemOffsetDecoration(context,R.dimen.episode_offset_left))

    }

    private fun setObserver(){
        viewModel.searchList.observe(viewLifecycleOwner, Observer {
            searchController.setData(it ,viewModel.isLoading.value?.isLoading ?: false)
            if(!it.isNullOrEmpty()){
                hideKeyBoard()
            }
        })


        viewModel.isLoading.observe( viewLifecycleOwner, Observer {
            if(it.isLoading){
                if(it.isListEmpty){
                    rootView.shimmerLayout.startShimmer()
                    shimmerLayout.visibility = View.VISIBLE
                }else{
                    rootView.shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                }
            }else{
                rootView.shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
            searchController.setData(viewModel.searchList.value, it.isLoading)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backButton ->{
                hideKeyBoard()
                findNavController().popBackStack()

            }
        }
    }

    private fun setRecyclerViewScroll(){
        rootView.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManger = rootView.searchRecyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManger.childCount
                val totalItemCount = layoutManger.itemCount
                val firstVisibleItemPosition = layoutManger.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                        viewModel.fetchNextPage()
                    }
            }
        })
    }

    private fun hideKeyBoard(){
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun showKeyBoard(){
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(activity?.currentFocus, 0)
    }

    override fun animeTitleClick(model: AnimeMetaModel) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToAnimeInfoFragment(categoryUrl = model.categoryUrl))
    }

}