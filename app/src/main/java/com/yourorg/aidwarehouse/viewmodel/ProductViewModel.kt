package com.yourorg.aidwarehouse.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.yourorg.aidwarehouse.data.Product

class ProductViewModel : ViewModel() {
    // Список усіх продуктів
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    /** Додає новий продукт з назвою [name] */
    fun addProduct(name: String) {
        val newList = _products.value + Product(name = name)
        _products.value = newList
    }

    /** Додає [amount] на склад (stock) */
    fun addToStock(index: Int, amount: Int) {
        changeProduct(index) { it.copy(stock = it.stock + amount, printed = it.printed - amount) }
    }

    /** Відправляє [amount]: зменшує stock і request */
    fun sendProduct(index: Int, amount: Int) {
        changeProduct(index) {
            it.copy(
                stock = it.stock - amount,
                request = (it.request - amount).coerceAtLeast(0)
            )
        }
    }

    /** Збільшує запит (request) на [amount] */
    fun increaseRequest(index: Int, amount: Int) {
        changeProduct(index) { it.copy(request = it.request + amount) }
    }

    /** Відправляє на друк/виробництво — збільшує printed */
    fun printProduct(index: Int, amount: Int) {
        changeProduct(index) { it.copy(printed = it.printed + amount) }
    }

    /** Скидає всі лічильники цього продукту */
    fun resetProduct(index: Int) {
        changeProduct(index) { Product(name = it.name) }
    }

    /** Підсумок: всього відправлено на друк усіх продуктів */
    fun totalPrinted(): Int =
        _products.value.sumOf { it.printed }

    // Допоміжна функція для оновлення одного елемента
    private fun changeProduct(index: Int, block: (Product) -> Product) {
        val list = _products.value.toMutableList()
        list[index] = block(list[index])
        _products.value = list
    }
}
