package net.xblacky.animexstream.ui.main.home.epoxy

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.recycler_anime_common.view.*
import kotlinx.android.synthetic.main.recycler_anime_recent_sub_dub.view.*
import kotlinx.android.synthetic.main.recycler_anime_mini_header.view.*
import kotlinx.android.synthetic.main.recycler_anime_popular.view.*
import kotlinx.android.synthetic.main.recycler_anime_recent_sub_dub.view.animeCardView
import kotlinx.android.synthetic.main.recycler_anime_recent_sub_dub.view.animeImage
import kotlinx.android.synthetic.main.recycler_anime_recent_sub_dub.view.animeTitle
import kotlinx.android.synthetic.main.recycler_anime_recent_sub_dub.view.episodeNumber
import kotlinx.android.synthetic.main.recycler_anime_recent_sub_dub_2.view.*
import net.xblacky.animexstream.R
import net.xblacky.animexstream.utils.Tags.GenreTags
import net.xblacky.animexstream.utils.model.AnimeMetaModel
import org.apmem.tools.layouts.FlowLayout

@EpoxyModelClass(layout = R.layout.recycler_anime_recent_sub_dub)
abstract class AnimeSubDubModel : EpoxyModelWithHolder<AnimeSubDubModel.SubDubHolder>(){

    @EpoxyAttribute
    lateinit var animeMetaModel: AnimeMetaModel
    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: SubDubHolder) {
        Glide.with(holder.animeImageView.context).load(animeMetaModel.imageUrl).into(holder.animeImageView)
        holder.animeTitle.text = animeMetaModel.title
        holder.animeEpisode.text = animeMetaModel.episodeNumber
        holder.animeImageView.setOnClickListener(clickListener)

    }
    class SubDubHolder : EpoxyHolder(){

        lateinit var animeImageView: AppCompatImageView
        lateinit var animeCardView: CardView
        lateinit var animeTitle: TextView
        lateinit var animeEpisode: TextView
        lateinit var animeType: TextView

        override fun bindView(itemView: View) {
            animeImageView = itemView.animeImage
            animeCardView = itemView.animeCardView
            animeTitle = itemView.animeTitle
            animeEpisode = itemView.episodeNumber
            animeType = itemView.animeType
        }

    }
}

@EpoxyModelClass(layout = R.layout.recycler_anime_recent_sub_dub_2)
abstract class AnimeSubDubModel2 : EpoxyModelWithHolder<AnimeSubDubModel2.SubDubHolder>(){

    @EpoxyAttribute
    lateinit var animeMetaModel: AnimeMetaModel
    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: SubDubHolder) {
        Glide.with(holder.animeImageView.context).load(animeMetaModel.imageUrl).into(holder.animeImageView)
        holder.animeTitle.text = animeMetaModel.title
        holder.animeEpisode.text = animeMetaModel.episodeNumber
        holder.background.setOnClickListener(clickListener)
        holder.animeTitle.setOnClickListener(clickListener)

    }
    class SubDubHolder : EpoxyHolder(){

        lateinit var animeImageView: AppCompatImageView
        lateinit var animeCardView: CardView
        lateinit var animeTitle: TextView
        lateinit var animeEpisode: TextView
        lateinit var background: AppCompatImageView

        override fun bindView(itemView: View) {
            animeImageView = itemView.animeImage
            animeCardView = itemView.animeCardView
            animeTitle = itemView.animeTitle
            animeEpisode = itemView.episodeNumber
            background = itemView.backgroundImage
        }

    }
}

@EpoxyModelClass(layout = R.layout.recycler_anime_popular)
abstract class AnimePopularModel : EpoxyModelWithHolder<AnimePopularModel.PopularHolder>(){

    @EpoxyAttribute
    lateinit var animeMetaModel: AnimeMetaModel
    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: PopularHolder) {
        Glide.with(holder.animeImageView.context).load(animeMetaModel.imageUrl).into(holder.animeImageView)
        holder.animeTitle.text = animeMetaModel.title
        holder.animeEpisode.text = animeMetaModel.episodeNumber

        holder.flowLayout.removeAllViews()

        animeMetaModel.genreList?.forEach {
            holder.flowLayout.addView(GenreTags(holder.flowLayout.context).getGenreTag(it.genreName,it.genreUrl))
        }
        holder.rootView.setOnClickListener(clickListener)

    }
    class PopularHolder : EpoxyHolder(){

        lateinit var animeImageView: AppCompatImageView
        lateinit var animeCardView: CardView
        lateinit var animeTitle: TextView
        lateinit var animeEpisode: TextView
        lateinit var flowLayout: FlowLayout
        lateinit var rootView: ConstraintLayout

        override fun bindView(itemView: View) {
            animeImageView = itemView.animeImage
            animeCardView = itemView.animeCardView
            animeTitle = itemView.animeTitle
            animeEpisode = itemView.episodeNumber
            flowLayout = itemView.flowLayout
            rootView = itemView.rootLayout
        }

    }
}



@EpoxyModelClass(layout = R.layout.recycler_anime_mini_header)
abstract class AnimeMiniHeaderModel : EpoxyModelWithHolder<AnimeMiniHeaderModel.AnimeMiniHeaderHolder>(){

    @EpoxyAttribute lateinit var typeName: String

    override fun bind(holder: AnimeMiniHeaderHolder) {
        super.bind(holder)
        holder.animeType.text = typeName
    }


    class AnimeMiniHeaderHolder : EpoxyHolder(){

        lateinit var animeType: TextView

        override fun bindView(itemView: View) {
            animeType = itemView.typeName
        }

    }

}

@EpoxyModelClass(layout = R.layout.recycler_home_header)
abstract class HomeHeaderModel : EpoxyModelWithHolder<HomeHeaderModel.HomeHeaderHolder>(){

    class HomeHeaderHolder : EpoxyHolder(){
        override fun bindView(itemView: View) {
        }
    }
}

