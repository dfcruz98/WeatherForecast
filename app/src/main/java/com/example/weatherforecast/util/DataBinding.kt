package com.example.weatherforecast.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter(
    "setDate"
)
fun TextView.setDate(
    pattern: String
) {
    val calendar = Calendar.getInstance()
    val format = SimpleDateFormat(pattern, Locale("pt"))
    val dateFormatted = format.format(calendar.time).toString()
    this.text = dateFormatted
}

@BindingAdapter(
    "weatherIcon"
)
fun ImageView.weatherIcon(
    id: String?
) {
    if (id == null)
        return

    when (id) {
        "01d", "01n" -> R.drawable.ic_1d
        "02d", "02n" -> R.drawable.ic_2d
        "03d", "03n" -> R.drawable.ic_3d
        "04d", "04n" -> R.drawable.ic_3d
        "09d", "09n" -> R.drawable.ic_9d
        "10d", "10n" -> R.drawable.ic_10d
        "11d", "11n" -> R.drawable.ic_11d
        "13d", "13n" -> R.drawable.ic_13d
        "50d", "50n" -> R.drawable.ic_50d
        else -> null
    }.also {
        it?.let { res ->
            this.setImageResource(res)
        }
    }
}