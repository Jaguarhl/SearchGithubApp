package com.kartsev.dmitry.searchgithubrepos.binding

import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kartsev.dmitry.searchgithubrepos.R

@BindingAdapter(
    value = ["app:search_text", "app:search_phrase", "app:search_highlight_messages"],
    requireAll = false
)
fun adapterHighlightPhrase(
    view: TextView,
    text: String?,
    highlight: String = "",
    highlightText: Boolean = false
) {
    if (text.isNullOrEmpty()) return

    val spannableString = SpannableString(text).apply {
        val firstChar = text.indexOf(highlight, ignoreCase = true)

        if (firstChar < 0) return@apply

        var finalChar = firstChar + highlight.length

        if (finalChar > text.length) finalChar = text.length

        val highlightColor =
            if (highlightText) R.color.text_search_highlight_color else android.R.color.transparent
        val backgroundColor =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                BackgroundColorSpan(view.context.resources.getColor(highlightColor, null))
            } else {
                BackgroundColorSpan(view.context.resources.getColor(highlightColor))
            }

        setSpan(
            backgroundColor,
            firstChar,
            finalChar,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    view.text = spannableString
}

@BindingAdapter("app:image_url")
fun displayImage(view: ImageView, uri: String?) {
    if (uri.isNullOrEmpty() || !Patterns.WEB_URL.matcher(uri).matches()) return

    Glide.with(view.context).load(uri)
        .circleCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view)
}