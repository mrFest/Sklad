package com.yourorg.aidwarehouse.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yourorg.aidwarehouse.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(app: Application) : AndroidViewModel(app) {
    private val gson = Gson()
    private val prefs = app.getSharedPreferences("sklad_prefs", Application.MODE_PRIVATE)

    private val _products = MutableStateFlow(loadProducts())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        // автозбереження при кожній зміні списку
        viewModelScope.launch {
            _products.collect { list ->
                saveProducts(list)
            }
        }
    }

    private fun loadProducts(): List<Product> {
        val json = prefs.getString("products", null) ?: return emptyList()
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveProducts(list: List<Product>) {
        prefs.edit()
            .putString("products", gson.toJson(list))
            .apply()
    }

    fun addProduct(name: String) {
        val newList = _products.value + Product(name = name)
        _products.value = newList
    }

    fun addToStock(index: Int, amount: Int) {
        change(index) { it.copy(stock = it.stock + amount, printed = it.printed - amount) }
    }

    fun sendProduct(index: Int, amount: Int) {
        change(index) {
            it.copy(
                stock = it.stock - amount,
                request = (it.request - amount).coerceAtLeast(0),
                sent = it.sent + amount         // збільшуємо лічильник відправлених
            )
        }
    }

    fun increaseRequest(index: Int, amount: Int) {
        change(index) { it.copy(request = it.request + amount) }
    }

    fun printProduct(index: Int, amount: Int) {
        change(index) { it.copy(printed = it.printed + amount) }
    }

    fun rejectPrinted(index: Int, amount: Int) {
        change(index) { it.copy(printed = (it.printed - amount).coerceAtLeast(0)) }
    }

    fun resetProduct(index: Int) {
        change(index) { Product(name = it.name) }
    }

    /** Підсумок: всього відправлено (sum of sent) */
    fun totalSent(): Int = _products.value.sumOf { it.sent }

    private fun change(index: Int, block: (Product) -> Product) {
        val temp = _products.value.toMutableList()
        temp[index] = block(temp[index])
        _products.value = temp
    }
}
