package hk.hku.cs.hkulunchpicker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hk.hku.cs.hkulunchpicker.util.fetchData
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val data = remember { mutableStateListOf<MutableMap<String, Any>>() }
    LaunchedEffect(Unit) {
        fetchData(context, data)
    }

    var clicked by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.padding(10.dp))

        for (i in data.indices) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "${data[i]["name"]}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 20.dp,
                        bottom = 5.dp,
                        end = 20.dp
                    ),
                )

                HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))

                Text(
                    text = "Category: ${data[i]["category"]}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp)
                )
                Text(
                    text = "Price: $ ${data[i]["price"]}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp)
                )
                Text(
                    text = "Distance: ${data[i]["distance"]} m",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.padding(40.dp))
    }

    val radioOptions = listOf("Name: Ascending", "Name: Descending", "Category: Ascending", "Category: Descending", "Price: Ascending", "Price: Descending", "Distance: Ascending", "Distance: Descending", "Default")
    val selectedOption = remember { mutableStateOf(radioOptions[8]) }

    if (clicked) {
        val (tempSelectedOption, tempOnOptionSelected) = remember { mutableStateOf(selectedOption.value) }
        AlertDialog(
            title = { Text(text = "Sorting Options") },
            text = {
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == tempSelectedOption),
                                    onClick = { tempOnOptionSelected(text) },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == tempSelectedOption),
                                onClick = { }
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            },
            onDismissRequest = { clicked = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        clicked = false
                        selectedOption.value = tempSelectedOption
                        sortData(radioOptions, selectedOption.value, data)
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                ) {
                    Text("Confirm")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(all = 20.dp)
                .align(alignment = Alignment.BottomEnd),
            onClick = {
                clicked = true
            },
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Sorting Options")
        }
    }
    
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

private fun sortData(radioOptions: List<String>, selectedOption: String, data: MutableList<MutableMap<String, Any>>) {
    when (selectedOption) {
        radioOptions[0] -> {
            sortByNameAsc(data)
        }
        radioOptions[1] -> {
            sortByNameDesc(data)
        }
        radioOptions[2] -> {
            sortByCatAsc(data)
        }
        radioOptions[3] -> {
            sortByCatDesc(data)
        }
        radioOptions[4] -> {
            sortByPriceAsc(data)
        }
        radioOptions[5] -> {
            sortByPriceDesc(data)
        }
        radioOptions[6] -> {
            sortByDistAsc(data)
        }
        radioOptions[7] -> {
            sortByDistDesc(data)
        }
        radioOptions[8] -> {
            sortByIdAsc(data)
        }
    }
}

private fun sortByNameAsc(data: MutableList<MutableMap<String, Any>>) {
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy { it["name"].toString() }
    data.clear()
    data.addAll(temp)
}

private fun sortByNameDesc(data: MutableList<MutableMap<String, Any>>) {
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy { it["name"].toString() }
    temp.reverse()
    data.clear()
    data.addAll(temp)
}

private fun sortByCatAsc(data: MutableList<MutableMap<String, Any>>) {
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy { it["category"].toString() }
    data.clear()
    data.addAll(temp)
}

private fun sortByCatDesc(data: MutableList<MutableMap<String, Any>>) {
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy { it["category"].toString() }
    temp.reverse()
    data.clear()
    data.addAll(temp)
}

private fun sortByPriceAsc(data: MutableList<MutableMap<String, Any>>) {
    val priceMap: Map<String, Int> = mapOf("<=50" to 0, "50-100" to 1, "100-200" to 2, "200-400" to 3)
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy{ priceMap[it["price"]] }
    data.clear()
    data.addAll(temp)
}

private fun sortByPriceDesc(data: MutableList<MutableMap<String, Any>>) {
    val priceMap: Map<String, Int> = mapOf("<=50" to 0, "50-100" to 1, "100-200" to 2, "200-400" to 3)
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy{ priceMap[it["price"]] }
    temp.reverse()
    data.clear()
    data.addAll(temp)
}

private fun sortByDistAsc(data: MutableList<MutableMap<String, Any>>) {
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy { it["distance"].toString().toInt() }
    data.clear()
    data.addAll(temp)
}

private fun sortByDistDesc(data: MutableList<MutableMap<String, Any>>) {
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy { it["distance"].toString().toInt() }
    temp.reverse()
    data.clear()
    data.addAll(temp)
}

private fun sortByIdAsc(data: MutableList<MutableMap<String, Any>>) {
    val temp: MutableList<MutableMap<String, Any>> = data.toMutableList()
    temp.sortBy { it["id"].toString().toInt() }
    data.clear()
    data.addAll(temp)
}