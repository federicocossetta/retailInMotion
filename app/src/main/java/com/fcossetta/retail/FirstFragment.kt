package com.fcossetta.retail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.retail.dao.Timetable
import com.fcossetta.retail.network.NetworkManager
import com.fcossetta.retail.network.RetailService
import com.fcossetta.retail.ui.TrainAdapter
import com.fcossetta.retail.utils.DataDownloadCallback
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
    // VIEWS
    private var emptyView: TextView? = null
    var recyclerView: RecyclerView? = null
    var directionInfo: TextView? = null
    var fromStation: String? = null
    var stop: String = "sti"
    var seeOutbound = false
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
        emptyView = view.findViewById(R.id.empty_view)
        service = NetworkManager.mInstance.getService()
        fromStation = getString(R.string.stil)
        seeOutbound = seeOutbound()
        if (seeOutbound) {
            stop = "mar"
            fromStation = getString(R.string.mar)
        }
        loadData(object : DataDownloadCallback {
            override fun onDataDownloaded(stops: Timetable?) {
                updateViews(timetable)
            }

            override fun onError(t: Throwable) {
                log.error(Log.getStackTraceString(t))
            }

        })

    }

    private fun updateViews(timetable: Timetable?) {
        if (timetable != null) {
            directionInfo?.apply { text = timetable!!.message }
            if (timetable.tramsFound) {
                recyclerView?.visibility = View.VISIBLE
                emptyView?.visibility = View.INVISIBLE
                emptyView?.apply { text = null }
                recyclerView?.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = TrainAdapter(timetable!!.trams, fromStation!!)
                }
            } else {
                emptyView?.visibility = View.VISIBLE
                emptyView?.apply { text = timetable.emptyServiceMessage }
                recyclerView?.visibility = View.INVISIBLE
            }

        }

    }

    private fun loadData(dataDownloadCallback: DataDownloadCallback) {

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
                            dataDownloadCallback.onDataDownloaded(timetable)

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        dataDownloadCallback.onError(t)
                    }

                })
        } catch (c: Exception) {
            dataDownloadCallback.onError(c)
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


    override fun reloadData(view: View) {
        view.isEnabled = false
        loadData(object : DataDownloadCallback {
            override fun onDataDownloaded(stops: Timetable?) {
                updateViews(timetable)
                Toast.makeText(activity?.applicationContext, R.string.reloaded, Toast.LENGTH_SHORT)
                    .show()
                view.isEnabled = true
            }

            override fun onError(t: Throwable) {
                log.error(Log.getStackTraceString(t))
                view.isEnabled = true
            }
        })
    }

}