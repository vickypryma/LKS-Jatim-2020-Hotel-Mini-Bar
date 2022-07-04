package com.pryma.hotelminibar.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Room(
    @JsonProperty("id") var id: Int,
    @JsonProperty("roomID") var roomID: Int,
    @JsonProperty("checkinDateTime") var checkinDate: Date,
    @JsonProperty("checkoutDateTime") var chekoutDate: Date,
    @JsonProperty("roomNumber") var roomNumber: Int
)
