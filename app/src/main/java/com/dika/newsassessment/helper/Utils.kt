package com.dika.newsassessment.helper

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    const val API_KEY = "f01f7b8be2fd46d8bf72727495dfaf3e"

    private var vibrantLightColorList = arrayOf(
        ColorDrawable(Color.parseColor("#39add1")),
        ColorDrawable(Color.parseColor("#3079ab")),
        ColorDrawable(Color.parseColor("#c25975")),
        ColorDrawable(Color.parseColor("#e15258")),
        ColorDrawable(Color.parseColor("#f9845b")),
        ColorDrawable(Color.parseColor("#838cc7")),
        ColorDrawable(Color.parseColor("#7d669e")),
        ColorDrawable(Color.parseColor("#53bbb4"))
    )
    val randomDrawableColor: ColorDrawable
        get() {
            val randomNumber = Random().nextInt(vibrantLightColorList.size)
            return vibrantLightColorList[randomNumber]
        }

    fun datetotimeFormat(existingStringDate: String): String? {
        val prettyTime = PrettyTime(Locale.ENGLISH)
        var time: String? = null
        try {
            val simpleDateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH
            )
            val date = simpleDateFormat.parse(existingStringDate)
            time = prettyTime.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time
    }

    fun dateFormat(existingStringDate: String): String? {
        var newDate: String? = ""
        val simpleDateFormat = SimpleDateFormat("E, d MMM yyyy", Locale.ENGLISH)
        try {
            @SuppressLint("SimpleDateFormat") val date =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(existingStringDate)
            if (date != null) {
                newDate = simpleDateFormat.format(date)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            newDate = existingStringDate
        }
        return newDate
    }
}
