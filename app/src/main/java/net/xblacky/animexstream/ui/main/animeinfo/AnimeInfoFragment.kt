package net.xblacky.animexstream.ui.main.animeinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_animeinfo.*
import kotlinx.android.synthetic.main.fragment_animeinfo.view.*
import kotlinx.android.synthetic.main.fragment_animeinfo.view.animeInfoRoot
import kotlinx.android.synthetic.main.loading.view.*
import net.xblacky.animexstream.R
import net.xblacky.animexstream.ui.main.animeinfo.epoxy.AnimeInfoController
import net.xblacky.animexstream.ui.main.home.HomeFragment
import net.xblacky.animexstream.utils.ItemOffsetDecoration
import net.xblacky.animexstream.utils.Tags.GenreTags
import net.xblacky.animexstream.utils.model.AnimeInfoModel
import timber.log.Timber

class AnimeInfoFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var viewModel: AnimeInfoViewModel
    private lateinit var episodeController: AnimeInfoController

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_animeinfo, container, false)
        viewModel = ViewModelProvider(this).get(AnimeInfoViewModel::class.java)
        getBundleData()
        setupRecyclerView()
        setObserver()
        transitionListener()
        setOnClickListeners()
        viewModel.fetchAnimeInfo()
        return rootView
    }


    private fun getBundleData(){
        val args =AnimeInfoFragmentArgs.fromBundle(bundle = arguments!!)
        viewModel.setUrl(args.categoryUrl!!)
    }

    private fun setObserver() {
        viewModel.animeInfoModel.observe(viewLifecycleOwner, Observer {
            it?.let {
                updateViews(it)
            }
        })

        viewModel.episodeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                rootView.animeInfoRoot.visibility = View.VISIBLE
                episodeController.setData(it)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {

            if(it.isLoading){
                rootView.loading.visibility = View.VISIBLE
            }else{
                rootView.loading.visibility = View.GONE
            }
        })



        viewModel.isFavourite.observe(viewLifecycleOwner, Observer {
            if(it){
                favourite.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite , null))
            }else{
                favourite.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_unfavorite , null))
            }
        })
    }

    private fun updateViews(animeInfoModel: AnimeInfoModel) {

        Glide.with(this).load(animeInfoModel.imageUrl).into(rootView.animeInfoImage)
        animeInfoReleased.text = animeInfoModel.releasedTime
        animeInfoStatus.text = animeInfoModel.status
        animeInfoType.text = animeInfoModel.type
        animeInfoTitle.text = animeInfoModel.animeTitle
        toolbarText.text = animeInfoModel.animeTitle
        flowLayout.removeAllViews()
        animeInfoModel.genre.forEach {
            flowLayout.addView(GenreTags(context!!).getGenreTag(genreName = it.genreName, genreUrl = it.genreUrl))
        }
       episodeController.setAnime(animeInfoModel.animeTitle)
        animeInfoSummary.text = animeInfoModel.plotSummary
        rootView.favourite.visibility = View.VISIBLE
        rootView.animeInfoRoot.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        episodeController = AnimeInfoController()
        episodeController.spanCount = 3
        rootView.animeInfoRecyclerView.adapter = episodeController.adapter
        val itemOffsetDecoration = ItemOffsetDecoration(context, R.dimen.episode_offset_left)
        rootView.animeInfoRecyclerView.addItemDecoration(itemOffsetDecoration)
        rootView.animeInfoRecyclerView.apply {
            layoutManager = GridLayoutManager(context,3)
            (layoutManager as GridLayoutManager).spanSizeLookup = episodeController.spanSizeLookup

        }
    }

    private fun transitionListener(){
        rootView.motionLayout.setTransitionListener(
            object: MotionLayout.TransitionListener{
                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {

                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                    rootView.topView.cardElevation = 0F
                }

                override fun onTransitionChange(p0: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                    if(startId == R.id.start){
                        rootView.topView.cardElevation = 20F * progress
                        rootView.toolbarText.alpha = progress
                    }
                    else{
                        rootView.topView.cardElevation = 10F * (1 - progress)
                        rootView.toolbarText.alpha = (1-progress)
                    }
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                }

            }
        )
    }

    private fun setOnClickListeners(){
        rootView.favourite.setOnClickListener {
            onFavouriteClick()
        }

        rootView.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onFavouriteClick(){
        if(viewModel.isFavourite.value!!){
            Snackbar.make(rootView, getText(R.string.removed_from_favourites), Snackbar.LENGTH_SHORT).show()
        }else{
            Snackbar.make(rootView, getText(R.string.added_to_favourites), Snackbar.LENGTH_SHORT).show()
        }
        viewModel.toggleFavourite()
    }

    override fun onResume() {
        super.onResume()
        if(episodeController.isWatchedHelperUpdated()){
            episodeController.setData(viewModel.episodeList.value)
        }
    }

}