package hk.hku.cs.hkulunchpicker

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hk.hku.cs.hkulunchpicker.util.fetchData

@Composable
fun FeaturedScreen() {
    val context = LocalContext.current
    val data = remember { mutableStateListOf<MutableMap<String, Any>>() }
    val featured = remember { mutableStateListOf<MutableMap<String, Any>>() }
    LaunchedEffect(Unit) {
        fetchData(context, data, featured)
    }

    var clicked by remember { mutableStateOf(false) }
    if (!clicked) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.padding(10.dp))

            for (i in featured.indices) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "${featured[i]["name"]}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 5.dp, end = 20.dp),
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))

                    Text(
                        text = "Category: ${featured[i]["category"]}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp)
                    )
                    Text(
                        text = "Price: $ ${featured[i]["price"]}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp)
                    )
                    Text(
                        text = "Distance: ${featured[i]["distance"]} m",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 5.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))

                    Text(
                        text = "Last posted: ${featured[i]["time"]}",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(40.dp))
        }
    } else {
        val selectedID = remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(10.dp))

            for (i in data.indices) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    onClick = {
                        selectedID.intValue = i
                        val intent = Intent(context, CheckoutActivity::class.java).apply {
                            putExtra("restaurant_id", selectedID.intValue)
                            putExtra("restaurant_name", data[selectedID.intValue]["name"].toString())
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 1.dp, start = 20.dp, end = 20.dp, bottom = 1.dp)
                ) {
                    Text(
                        text = "${data[i]["name"]}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp, end = 20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.padding(50.dp))
        }

    }


    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(all = 20.dp)
                .align(alignment = Alignment.BottomEnd),
            onClick = {
                clicked = !clicked
            },
            shape = CircleShape
        ) {
            if (!clicked) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            } else {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun FeaturedScreenPreview() {
    FeaturedScreen()
}