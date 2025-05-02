package com.yourorg.aidwarehouse.data

/**
 * Одна позиція гуманітарної допомоги
 * @param name – назва виробу
 * @param stock – скільки зараз на складі
 * @param request – скільки ще треба
 * @param printed – скільки відправлено на друк/виробництво
 */
data class Product(
    val name: String,
    var stock: Int = 0,
    var request: Int = 0,
    var printed: Int = 0
)
