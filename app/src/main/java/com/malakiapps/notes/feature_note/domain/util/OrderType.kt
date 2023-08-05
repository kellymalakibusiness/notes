package com.malakiapps.notes.feature_note.domain.util

sealed class OrderType(val orderMode: OrderMode){
    class Date(orderMode: OrderMode): OrderType(orderMode)
    class Title(orderMode: OrderMode): OrderType(orderMode)
    class Body(orderMode: OrderMode): OrderType(orderMode)

    fun copy(orderMode: OrderMode): OrderType{
        return when(this){
            is Body -> Body(orderMode)
            is Date -> Date(orderMode)
            is Title -> Title(orderMode)
        }
    }
}
