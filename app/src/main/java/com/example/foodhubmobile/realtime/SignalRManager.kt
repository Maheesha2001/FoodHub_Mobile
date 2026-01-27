package com.example.foodhubmobile.realtime

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder

object SignalRManager {

    private var hubConnection: HubConnection? = null

    fun connect(
        driverId: String,
        onOrderAssigned: (String) -> Unit
    ) {
        hubConnection = HubConnectionBuilder
            .create("http://192.168.8.134:5187/deliveryHub")
            .build()

        hubConnection?.on(
            "OrderAssigned",
            { orderCode ->
                onOrderAssigned(orderCode)
            },
            String::class.java
        )

        hubConnection?.start()
    }

    fun disconnect() {
        hubConnection?.stop()
        hubConnection = null
    }
}
