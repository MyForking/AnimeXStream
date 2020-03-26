package net.xblacky.animexstream.ui.main.favourites.epoxy

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.recycler_anime_common.view.*
import net.xblacky.animexstream.R
import net.xblacky.animexstream.utils.model.AnimeMetaModel

@EpoxyModelClass(layout = R.layout.recycler_anime_common)
abstract class FavouriteModel : EpoxyModelWithHolder<FavouriteModel.MovieHolder>(){

    @EpoxyAttribute
    lateinit var favouriteModel: net.xblacky.animexstream.utils.model.FavouriteModel
    @EpoxyAttribute
    var clickListener: View.OnClickListener? = null

    override fun bind(holder: MovieHolder) {
        Glide.with(holder.animeImageView.context).load(favouriteModel.imageUrl).transition(
            DrawableTransitionOptions.withCrossFade()).into(holder.animeImageView)
        holder.animeTitle.text = favouriteModel.animeName
        favouriteModel.releasedDate?.let {
            val text = "Released: $it"
            holder.releasedDate.text = text
        }
        holder.root.setOnClickListener(clickListener)

    }
    class MovieHolder : EpoxyHolder(){

        lateinit var animeImageView: AppCompatImageView
        lateinit var animeTitle: TextView
        lateinit var releasedDate: TextView
        lateinit var root: ConstraintLayout

        override fun bindView(itemView: View) {
            animeImageView = itemView.animeImage
            animeTitle = itemView.animeTitle
            releasedDate = itemView.releasedDate
            root = itemView.root
        }

    }
}