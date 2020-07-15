package com.srikanthpuram.tmtsampleapp.model

import java.io.Serializable

data class CardX(
    val attributes: Attributes?,
    val image: Image?,
    val title: Title?,
    val description: Description?,
    val value: String?
) : Serializable