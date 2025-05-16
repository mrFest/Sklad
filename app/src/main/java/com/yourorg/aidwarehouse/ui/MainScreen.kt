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
        TabRow(selectedTabIndex = selectedTab) {
            products.forEachIndexed { idx, product ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx }
                ) {
                    Text(product.name, modifier = Modifier.padding(16.dp))
                }
            }
            // вкладка "+"
            Tab(
                selected = selectedTab == products.size,
                onClick = { selectedTab = products.size }
            ) {
                Text("+", modifier = Modifier.padding(16.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        if (selectedTab == products.size) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Назва виробу") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    if (newName.isNotBlank()) {
                        val idx = products.size
                        viewModel.addProduct(newName.trim())
                        newName = ""
                        selectedTab = idx
                    }
                }) {
                    Text("Додати продукт")
                }
            }
        } else {
            ProductDetailScreen(
                product       = products[selectedTab],
                onAddToStock  = { amt -> viewModel.addToStock(selectedTab, amt) },
                onSend        = { amt -> viewModel.sendProduct(selectedTab, amt) },
                onRequestMore = { amt -> viewModel.increaseRequest(selectedTab, amt) },
                onReset       = { viewModel.resetProduct(selectedTab) },
                totalSent     = viewModel.totalSent()
            )
        }
    }
}
