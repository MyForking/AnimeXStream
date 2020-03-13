package net.xblacky.animexstream.utils.Tags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.tags_genre.view.*
import net.xblacky.animexstream.R

class GenreTags(var context: Context){

    public fun getGenreTag(genreName: String, genreUrl: String): View{
        var view =LayoutInflater.from(context).inflate(R.layout.tags_genre, null)
        var button = view.genre
        button.text = genreName
        button.maxLines  = 1
        val rel_button1 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        rel_button1.setMargins(8, 8, 8, 8)
        button.layoutParams = rel_button1
        return view
    }

}