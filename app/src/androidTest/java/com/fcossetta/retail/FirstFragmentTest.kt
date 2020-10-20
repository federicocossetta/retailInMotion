package com.fcossetta.retail

import androidx.test.platform.app.InstrumentationRegistry
import com.fcossetta.retail.dao.Timetable
import com.fcossetta.retail.dao.Tram
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class FirstFragmentTest {
    private val log: Logger = LoggerFactory.getLogger(FirstFragmentTest::class.java)
    var fragment: FirstFragment = FirstFragment()

    @Test
    fun reloadData() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        fragment.updateViews(null)
    }

    @Test
    fun emptyTrain() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var list = ArrayList<Tram>()
        list.add(Tram("2", "Test tram"))
        fragment.updateViews(Timetable("Test timetable", list, false, "This is a empty test"))
    }

    @Test
    fun timeTable() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var list = ArrayList<Tram>()
        list.add(Tram("DUE", "Test tram"))
        list.add(Tram("3", "Test tram  4"))
        list.add(Tram("12", "Test tram 3"))
        fragment.updateViews(Timetable("Test timetable", list, true, null))
    }

}