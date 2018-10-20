package com.example.ghifar.footballmatch.presenter

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Alhudaghifari on 14:28 20/10/18
 *
 */
class Utils {

    fun getClock(strTime: String?): String {
        // clock parser
        val clock = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        clock.setTimeZone(TimeZone.getTimeZone("UTC"))
        val strTimeParsed = clock.parse(strTime)
        clock.setTimeZone(TimeZone.getDefault())
        val formattedClock = clock.format(strTimeParsed)
        return formattedClock
    }

    fun getDate(date: String?): String {
        // date parser
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        df.setTimeZone(TimeZone.getTimeZone("UTC"))
        val date = df.parse(date)
        df.setTimeZone(TimeZone.getDefault())
        val daterr = SimpleDateFormat("yyyy-M-dd").parse(df.format(date))
        val dateIndo = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).format(daterr)
        return dateIndo
    }

}