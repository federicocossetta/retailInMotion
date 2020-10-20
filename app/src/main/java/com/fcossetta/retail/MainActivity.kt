package com.fcossetta.retail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.retail.network.NetworkManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class MainActivity : AppCompatActivity() {
    private val log: Logger = LoggerFactory.getLogger(NetworkManager::class.simpleName)
    var fragment: FirstFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
            setSupportActionBar(findViewById(R.id.toolbar))
            findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
                reloadTimetable()
            }
            loadFragmentMain()
        } catch (c: Exception) {
            log.error(Log.getStackTraceString(c))
        }
    }

    private fun loadFragmentMain() {
        fragment = FirstFragment()
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flContent, fragment!!).commit()
    }

    private fun reloadTimetable() {
        fragment?.reloadData()
    }

}