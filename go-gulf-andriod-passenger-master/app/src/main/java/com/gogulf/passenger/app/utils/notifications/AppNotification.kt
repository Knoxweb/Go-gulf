package com.gogulf.passenger.app.utils.notifications

import java.io.Serializable

class AppNotification : Serializable {
    var smallIconUrl: String? = null
    var largeIconUrl: String? = null
    var title: String? = null
    var body: String? = null
    var type: String? = null
    var target: String? = null
}