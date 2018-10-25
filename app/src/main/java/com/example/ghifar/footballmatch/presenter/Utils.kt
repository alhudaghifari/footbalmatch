package com.example.ghifar.footballmatch.presenter

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Alhudaghifari on 14:28 20/10/18
 *
 */
class Utils {

    /**
     * method ini untuk mendapatkan waktu gmt + 7
     */
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

    fun getHtmlStringFormat(desc: String?): String {
        var description = ""
        val styleOpening = "<html><body bgcolor=\"#e9e9e9\">" +
                "<p style=\"" +
                "line-height: 1.5;" +
                "\" align=\"justify\">"
        val styleClosing = "</p></body></html>"

        description = styleOpening
        description += desc
        description += styleClosing
        return description
    }

    fun getIdLeague(leagueName: String): String {
        when(leagueName) {
            "English Premier League" -> return "4328"
            "English League Championship" -> return "4329"
            "German Bundesliga" -> return "4331"
            "Italian Serie A" -> return "4332"
            "French Ligue 1" -> return "4334"
            "Spanish La Liga" -> return "4335"
        }
        return ""
    }

}