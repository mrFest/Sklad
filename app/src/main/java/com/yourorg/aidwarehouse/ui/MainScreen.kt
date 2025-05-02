package com.yourorg.aidwarehouse.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourorg.aidwarehouse.viewmodel.ProductViewModel

@Composable
fun MainScreen(viewModel: ProductViewModel) {
    val products by viewModel.products.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var newName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Вкладки продуктів та додавання
        TabRow(selectedTabIndex = selectedTab) {
            products.forEachIndexed { idx, product ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx }
                ) {
                    Text(product.name, modifier = Modifier.padding(16.dp))
                }
            }
            Tab(
                selected = selectedTab == products.size,
                onClick = { selectedTab = products.size }
            ) {
                Text("+", modifier = Modifier.padding(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == products.size) {
            // Форма додавання нового продукту
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Назва виробу") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    if (newName.isNotBlank()) {
                        // Обчислюємо індекс для нової вкладки до додавання
                        val addIndex = products.size
                        viewModel.addProduct(newName.trim())
                        newName = ""
                        // Переходимо на вкладку з щойно створеним продуктом
                        selectedTab = addIndex
                    }
                }) {
                    Text("Додати продукт")
                }
            }
        } else {
            // Детальний екран обраного продукту
            ProductDetailScreen(
                product       = products[selectedTab],
                onAddToStock  = { amt -> viewModel.addToStock(selectedTab, amt) },
                onSend        = { amt -> viewModel.sendProduct(selectedTab, amt) },
                onRequestMore = { amt -> viewModel.increaseRequest(selectedTab, amt) },
                onPrint       = { amt -> viewModel.printProduct(selectedTab, amt) },
                onReject      = { amt -> viewModel.rejectPrinted(selectedTab, amt) },
                onReset       = { viewModel.resetProduct(selectedTab) },
                totalSent     = viewModel.totalPrinted()
            )
        }
    }
}
