package org.vsu.pt.team2.utilitatemmetrisapp.dateutils

import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

object DateFormatter {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH)
//        .apply {
//        timeZone = TimeZone.getTimeZone("UTC")
//    }

    val networkDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
//        .apply {
//        timeZone = TimeZone.getTimeZone("UTC")
//    }

    //2021-05-27T14:04:15.988Z
    /*val networkDateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    } else {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }*/

    fun toString(date: Date): String {
        return dateFormat.format(date)
    }

    @Throws(ParseException::class)
    fun fromString(dateStr: String): Date {
        return dateFormat.parse(dateStr)
    }

    fun toNetworkString(date: Date): String {
        return networkDateFormat.format(date)
    }

    @Throws(ParseException::class)
    fun fromNetworkString(dateStr: String): Date {
        val res = networkDateFormat.parse(dateStr)
        return res
    }

    @Throws(ParseException::class)
    fun fromNetworkStringToString(dateStr: String): String {
        val fns = fromNetworkString(dateStr)
        return toString(fns)
    }
}