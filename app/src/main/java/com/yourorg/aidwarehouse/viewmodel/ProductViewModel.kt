package com.yourorg.aidwarehouse.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.yourorg.aidwarehouse.data.Product

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun addProduct(name: String) {
        _products.value = _products.value + Product(name = name)
    }

    fun addToStock(index: Int, amount: Int) {
        changeProduct(index) { it.copy(stock = it.stock + amount, printed = it.printed - amount) }
    }

    fun sendProduct(index: Int, amount: Int) {
        changeProduct(index) {
            it.copy(
                stock = it.stock - amount,
                request = (it.request - amount).coerceAtLeast(0)
            )
        }
    }

    fun increaseRequest(index: Int, amount: Int) {
        changeProduct(index) { it.copy(request = it.request + amount) }
    }

    fun printProduct(index: Int, amount: Int) {
        changeProduct(index) { it.copy(printed = it.printed + amount) }
    }

    /** Позначити [amount] як брак — відняти від printed */
    fun rejectPrinted(index: Int, amount: Int) {
        changeProduct(index) {
            it.copy(printed = (it.printed - amount).coerceAtLeast(0))
        }
    }

    fun resetProduct(index: Int) {
        changeProduct(index) { Product(name = it.name) }
    }

    fun totalPrinted(): Int = _products.value.sumOf { it.printed }

    private fun changeProduct(index: Int, block: (Product) -> Product) {
        val list = _products.value.toMutableList()
        list[index] = block(list[index])
        _products.value = list
    }
}
