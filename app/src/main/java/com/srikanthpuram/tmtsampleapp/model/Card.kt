package com.srikanthpuram.tmtsampleapp.model

import java.io.Serializable

data class Card(
    val card: CardX,
    val card_type: String
) : Serializable