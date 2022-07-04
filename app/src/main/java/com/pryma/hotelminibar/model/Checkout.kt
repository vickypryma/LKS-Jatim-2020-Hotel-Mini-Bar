package com.pryma.hotelminibar.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Checkout(
    @JsonProperty("id") var id: Int? = null,
    @JsonProperty("reservationRoomID") var reservationRoomID: Int?,
    @JsonProperty("fdid") var fdid: Int?,
    @JsonProperty("qty") var qty: Int,
    @JsonProperty("totalPrice") var totalPrice: Int,
    @JsonProperty("employeeID") var employeeID: Int? = 2
)
