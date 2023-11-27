package hk.hku.cs.hkulunchpicker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hk.hku.cs.hkulunchpicker.util.fetchData
import java.util.Random

@Composable
fun RandomScreen() {
    val context = LocalContext.current
    val data = remember { mutableStateListOf<MutableMap<String, Any>>() }
    LaunchedEffect(Unit) {
        fetchData(context, data)
    }

    val random = Random()
    val randomNumber = remember { mutableIntStateOf(-1) }

    var clicked by remember { mutableStateOf(false) }

    if (data.size > 0) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (randomNumber.intValue != -1) {
                ClickableText(
                    modifier = Modifier.padding(30.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        ) {
                            append(data[randomNumber.intValue]["name"].toString())
                        }
                    },
                    onClick = {
                        clicked = true
                    }
                )
            }

            Button(
                onClick = {
                    randomNumber.intValue = random.nextInt(data.size)
                },
                shape = CircleShape,
                modifier = Modifier.size(150.dp),
                contentPadding = PaddingValues(5.dp)
            ) {
                Text(
                    text = "Draw",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,
                        lineHeight = 30.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }

        }
    }


    if (clicked) {
        Dialog(onDismissRequest = { clicked = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "${data[randomNumber.intValue]["name"]}",
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
                    text = "Category: ${data[randomNumber.intValue]["category"]}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp)
                )
                Text(
                    text = "Price: $ ${data[randomNumber.intValue]["price"]}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp)
                )
                Text(
                    text = "Distance: ${data[randomNumber.intValue]["distance"]} m",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 20.dp)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RandomScreenPreview() {
    RandomScreen()
}