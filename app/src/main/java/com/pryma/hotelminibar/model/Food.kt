package com.pryma.hotelminibar.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Food(
    @JsonProperty("id") var id: Int,
    @JsonProperty("name") var name: String,
    @JsonProperty("type") var type: Char,
    @JsonProperty("price") var price: Int
)
