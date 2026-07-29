package com.jrprofessor.mindolist.utils

import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_t
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_t
import platform.darwin.dispatch_get_main_queue

class IosNetworkConnectivityManager : NetworkConnectivityManager {
    private var isConnected: Boolean = true
    private val monitor: nw_path_monitor_t = nw_path_monitor_create()

    init {
        nw_path_monitor_set_update_handler(monitor) { path: nw_path_t? ->
            isConnected = nw_path_get_status(path) == nw_path_status_satisfied
        }
        nw_path_monitor_start(monitor)
    }

    override fun isNetworkAvailable(): Boolean {
        return isConnected
    }
}
