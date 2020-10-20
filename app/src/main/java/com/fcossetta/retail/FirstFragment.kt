package com.fcossetta.retail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.retail.dao.Timetable
import com.fcossetta.retail.network.NetworkManager
import com.fcossetta.retail.network.RetailService
import com.fcossetta.retail.ui.TrainAdapter
import com.fcossetta.retail.utils.DataHandlerInterface
import com.fcossetta.retail.utils.XmlParser
import okhttp3.ResponseBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Response
import java.util.*


class FirstFragment() : Fragment(), DataHandlerInterface {
    private val log: Logger = LoggerFactory.getLogger(FirstFragment::class.java)
    private var timetable: Timetable? = null
    var recyclerView: RecyclerView? = null
    var directionInfo: TextView? = null
    var title: TextView? = null
    private var service: RetailService? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_timetable)
        directionInfo = view.findViewById(R.id.direction_info)
        title = view.findViewById(R.id.direction_title)
        service = NetworkManager.mInstance.getService()
        loadData()
        if(seeOutbound())
            title?.apply { text = getText(R.string.outbound) }
        else
            title?.apply { text = getText(R.string.inbound) }

    }

    private fun loadData() {
        val rightNow: Calendar =
            Calendar.getInstance() // return the hour in 24 hrs format (ranging from 0-23)
        var stop = "sti"
        val seeOutbound = seeOutbound()
        if (seeOutbound) {
            stop = "mar"
        }
        try {
            service?.getForecast("forecast", stop, false)
                ?.enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            val string = body.string()
                            if (seeOutbound) {
                                timetable = XmlParser.mInstance.parseOutbound(
                                    string
                                )
                            } else
                                timetable =
                                    XmlParser.mInstance.parseInbound(string)


                            directionInfo?.apply { text = timetable!!.message }
                            recyclerView?.apply {
                                layoutManager = LinearLayoutManager(activity)
                                adapter = TrainAdapter(timetable!!.trams)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        log.error(Log.getStackTraceString(t))
                    }

                })
        } catch (c: Exception) {
            log.error(Log.getStackTraceString(c))
        }
    }


    private fun seeOutbound(): Boolean {

        val rightNow: Calendar =
            Calendar.getInstance()
        val currentHourIn24Format: Int =
            rightNow.get(Calendar.HOUR_OF_DAY)
        val curentMinute: Int =
            rightNow.get(Calendar.MINUTE)
        return (currentHourIn24Format in 0..11) || (currentHourIn24Format == 12 && curentMinute == 0)
    }


    override fun reloadData() {
        loadData()
    }


}