package com.yourorg.aidwarehouse.data

/**
 * Модель одного виробу зі статистикою.
 */
data class Product(
    val name: String,
    var stock: Int = 0,
    var request: Int = 0,
    var sent: Int = 0        // тепер відправлені рахуються окремо
)
