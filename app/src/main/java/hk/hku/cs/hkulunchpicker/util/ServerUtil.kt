package hk.hku.cs.hkulunchpicker.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val DB_SERVER_IP = "192.168.1.35"

fun fetchData(
    context: Context,
    data: MutableList<MutableMap<String, Any>>,
    featured: MutableList<MutableMap<String, Any>>? = null
) {
    val url = "http://${DB_SERVER_IP}:5000/get"
    val queue = Volley.newRequestQueue(context)

    val request = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            data.clear()
            val jsonArray = response.getJSONArray("data")
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val entry = mutableMapOf<String, Any>()
                entry["id"] = jsonObject.getInt("restaurant_id")
                entry["name"] = jsonObject.getString("restaurant_name")
                entry["category"] = jsonObject.getString("category")
                entry["price"] = jsonObject.getString("price")
                entry["distance"] = jsonObject.getInt("distance")
                entry["time"] = jsonObject.getString("last_posted")
                data.add(entry)
            }
            if (featured != null) {
                sortByTime(data, featured)
            }
        },
        { error ->
            Log.e("Error", error.toString())
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
        }
    )

    queue.add(request)
}

private fun sortByTime(data: MutableList<MutableMap<String, Any>>, featured: MutableList<MutableMap<String, Any>>) {
    featured.clear()
    for (i in data.indices) {
        if (data[i]["time"] != "null") {
            featured.add(data[i])
        }
    }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val temp: MutableList<MutableMap<String, Any>> = featured.toMutableList()

    temp.sortBy { LocalDateTime.parse(it["time"].toString(), formatter) }
    temp.reverse()

    featured.clear()
    featured.addAll(temp)
}

fun updateDatabase(
    context: Context,
    selectedID: Int
) {
    val url:String = "http://${DB_SERVER_IP}:5000/feature" + "?id=" + selectedID
    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            Log.d("Update Success", response.toString())
        },
        { error ->
            Log.e("Error",error.toString())
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
        }
    )
    Volley.newRequestQueue(context).add(jsonObjectRequest)
}