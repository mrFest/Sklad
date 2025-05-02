package com.yourorg.aidwarehouse.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yourorg.aidwarehouse.data.Product

@Composable
fun ProductDetailScreen(
    product: Product,
    onAddToStock: (Int) -> Unit,
    onSend: (Int) -> Unit,
    onRequestMore: (Int) -> Unit,
    onPrint: (Int) -> Unit,
    onReject: (Int) -> Unit,
    onReset: () -> Unit,
    totalSent: Int
) {
    var amountText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Підтвердження") },
            text = { Text("Очистити всі лічильники?") },
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(product.name, style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("На складі: ${product.stock}", color = Color(0xFF4CAF50))
            Text("Запит: ${product.request}", color = Color(0xFF2196F3))
            Text("Друкується: ${product.printed}", color = Color(0xFF9C27B0))
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it.filter(Char::isDigit) },
            label = { Text("Кількість") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    amountText.toIntOrNull()?.let {
                        onAddToStock(it)
                        amountText = ""        // очищаємо поле
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
            ) { Text("На склад") }

            Button(
                onClick = {
                    amountText.toIntOrNull()?.let {
                        onSend(it)
                        amountText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF9800))
            ) { Text("Відправлено") }

            Button(
                onClick = {
                    amountText.toIntOrNull()?.let {
                        onRequestMore(it)
                        amountText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3))
            ) { Text("Ще запит") }

            Button(
                onClick = {
                    amountText.toIntOrNull()?.let {
                        onPrint(it)
                        amountText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9C27B0))
            ) { Text("Друкується") }

            Button(
                onClick = {
                    amountText.toIntOrNull()?.let {
                        onReject(it)
                        amountText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) { Text("Брак") }

            OutlinedButton(onClick = { showDialog = true }) {
                Text("Обнулити")
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = "Всього відправлено: $totalSent",
            style = MaterialTheme.typography.h6,
            color = Color(0xFFFF9800)
        )
    }
}
