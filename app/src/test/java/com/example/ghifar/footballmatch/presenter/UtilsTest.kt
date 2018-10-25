package com.example.ghifar.footballmatch.presenter

import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Alhudaghifari on 9:08 24/10/18
 */
class UtilsTest {

    private lateinit var utils: Utils

    @Before
    internal fun setUp() {
        utils = Utils()
    }

    @Test
    fun getClock() {
        var clock = "16:30:00"
        var clock1 = "1:25:00"
        var clock2 = "7:01:00"
        var clock3 = "23:00:00"
        Assert.assertEquals("23:30:00", utils.getClock(clock))
        Assert.assertEquals("08:25:00", utils.getClock(clock1))
        Assert.assertEquals("14:01:00", utils.getClock(clock2))
        Assert.assertEquals("06:00:00", utils.getClock(clock3))
    }

    @Test
    fun getDate() {
        var date = "2018-10-27"
        Assert.assertEquals("Sat, 27 Oct 2018", utils.getDate(date))
    }

    @Test
    fun getIdLeague() {
        var league = "English Premier League"
        var league1 ="English League Championship"
        var league2 = "German Bundesliga"
        var league3 = "Italian Serie A"
        var league4 = "French Ligue 1"
        var league5 = "Spanish La Liga"

        Assert.assertEquals("4328", utils.getIdLeague(league))
        Assert.assertEquals("4329", utils.getIdLeague(league1))
        Assert.assertEquals("4331", utils.getIdLeague(league2))
        Assert.assertEquals("4332", utils.getIdLeague(league3))
        Assert.assertEquals("4334", utils.getIdLeague(league4))
        Assert.assertEquals("4335", utils.getIdLeague(league5))
    }
}