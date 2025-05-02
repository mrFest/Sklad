package com.yourorg.aidwarehouse.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourorg.aidwarehouse.data.Product
import com.yourorg.aidwarehouse.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen(
    product: Product,
    onAddToStock: (Int) -> Unit,
    onSend: (Int) -> Unit,
    onRequestMore: (Int) -> Unit,
    onPrint: (Int) -> Unit,
    onReset: () -> Unit
) {
    var amountText by remember { mutableStateOf("0") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Підтвердження") },
            text = { Text("Дійсно очистити всі лічильники?") },
            confirmButton = {
                TextButton(onClick = {
                    onReset()
                    showDialog = false
                }) { Text("Так") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Ні") }
            }
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = product.name, style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        // Показники
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("На складі: ${product.stock}")
            Text("Запит: ${product.request}")
            Text("В друк: ${product.printed}")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Поле для кількості
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
            label = { Text("Кількість") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки дій
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { amountText.toIntOrNull()?.let(onAddToStock) }) {
                Text("На склад")
            }
            Button(onClick = { amountText.toIntOrNull()?.let(onSend) }) {
                Text("Відправлено")
            }
            Button(onClick = { amountText.toIntOrNull()?.let(onRequestMore) }) {
                Text("Ще запит")
            }
            Button(onClick = { amountText.toIntOrNull()?.let(onPrint) }) {
                Text("В друк")
            }
            OutlinedButton(onClick = { showDialog = true }) {
                Text("Обнулити")
            }
        }
    }
}
