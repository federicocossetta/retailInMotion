package com.fcossetta.retail.utils

import com.fcossetta.retail.dao.Timetable

interface DataDownloadCallback {
    fun onDataDownloaded(data: Timetable?)
    fun onError(exception: Throwable)
}