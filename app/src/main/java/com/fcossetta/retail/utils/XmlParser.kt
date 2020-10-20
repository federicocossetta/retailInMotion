package com.fcossetta.retail.utils

import android.util.Log
import android.util.Xml
import com.fcossetta.retail.dao.Timetable
import com.fcossetta.retail.dao.Tram
import kotlinx.coroutines.channels.ticker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.StringReader
import java.sql.Time


class XmlParser {
    private val log: Logger = LoggerFactory.getLogger(XmlParser::class.java)

    companion object {
        val mInstance = XmlParser()
    }

    fun getTitle(message: String): String? {
        val parser: XmlPullParser = Xml.newPullParser()
        var title = String()
        parser.setInput(StringReader(message))
        log.error(message)
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        var messageFound = false
        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (!messageFound)
                messageFound = parser.name == "message" && parser.eventType == XmlPullParser.START_TAG
            if (messageFound && parser.eventType != XmlPullParser.END_TAG) {
                val text = parser.text
                log.error("text {}", text)
                if (text != null) {
                    title = text
                    break
                }
            }

        }
        return title
    }

    fun parseOutbound(message: String): Timetable {
        val parseTrains = parseTrains(message, "Outbound")
        val title = getTitle(message)
        return Timetable(title, parseTrains)
    }

    fun parseInbound(message: String): Timetable {
        val parseTrains = parseTrains(message, "Inbound")
        val title = getTitle(message)

        return Timetable(title, parseTrains)
    }

    private fun parseTrains(message: String, direction: String): ArrayList<Tram> {
        val trams: ArrayList<Tram> = ArrayList()
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            var addData = false
            var title: String
            parser.setInput(StringReader(message))
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.name.equals("message", false)) {
                    val text = parser.text
                    if (text != null) {
                        title = text
                        log.error("titlee {}", title)
                    }
                }
                val directionFound = parser.name.equals("direction", false)
                if (directionFound) {
                    val directionName = parser.getAttributeValue(
                        null,
                        "name"
                    )
                    addData = directionName != null && directionName.equals(direction, false)
                }
                if (addData && parser.eventType == XmlPullParser.START_TAG) {
                    if (parser.name == "tram") {
                        val tram: Tram = getTrain(parser)
                        trams.add(tram)
                    }
                }
            }
        } catch (c: java.lang.Exception) {
            log.error(Log.getStackTraceString(c))
        }
        return trams
    }


    private fun getTrain(parser: XmlPullParser): Tram {
        val dueMins = parser.getAttributeValue(null, "dueMins")
        val destination = parser.getAttributeValue(null, "destination")
        return Tram(dueMins, destination)
    }

}