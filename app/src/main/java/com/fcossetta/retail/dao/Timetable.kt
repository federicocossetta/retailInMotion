package com.fcossetta.retail.dao

class Timetable(
    var stopname: String?,
    var message: String?,
    var trams: ArrayList<Tram>,
    var tramsFound: Boolean,
    var emptyServiceMessage: String?

)
