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
        // 1) Вкладки
        TabRow(selectedTabIndex = selectedTab) {
            products.forEachIndexed { idx, p ->
                Tab(selected = selectedTab == idx,
                    onClick  = { selectedTab = idx }) {
                    Text(p.name, modifier = Modifier.padding(16.dp))
                }
            }
            // "+" вкладка
            Tab(selected = selectedTab == products.size,
                onClick  = { selectedTab = products.size }) {
                Text("+", modifier = Modifier.padding(16.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        // 2) Вміст вкладки
        if (selectedTab == products.size) {
            // Додаємо новий продукт
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
                        viewModel.addProduct(newName.trim())
                        newName = ""
                        selectedTab = products.lastIndex  // переходимо до новоствореної вкладки
                    }
                }) {
                    Text("Додати продукт")
                }
            }
        } else {
            // Тимчасово – просто показати назву обраного
            Text(
                text = "Продукт: ${products[selectedTab].name}",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6
            )
            // Тут пізніше додамо ProductDetailScreen(...)
        }
    }
}
