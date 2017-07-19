package dreamlander.dreamland.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import dreamlander.dreamland.configurations.Config
import dreamlander.dreamland.helpers.Logger
import dreamlander.dreamland.models.Entry
import org.json.JSONObject

/**
 * Created by yedhukrishnan on 19/07/17.
 */
class CreateEntryRequest(context: Context) {
    private var context: Context = context

    fun sendRequest(entry: Entry) {
        var requestQueue = Volley.newRequestQueue(context)

        val request = ServerJsonObjectRequest(
                Request.Method.POST,
                Config.CREATE_ENTRY_URL,
                getJsonPayload(entry),
                Response.Listener<JSONObject>({ response ->
                    Logger.debug("Server sync success for ${entry.uuid}")
                    setEntryAsSynced(entry)
                }),
                Response.ErrorListener { error ->
                    Logger.error(error.message);
                    Logger.error("Server sync failed for ${entry.uuid}. Will try again later.")
                },
                context
        )
        requestQueue.add(request)
    }

    private fun  getJsonPayload(entry: Entry): JSONObject {
        val entryData = JSONObject()
        val requestData = JSONObject()
        try {
            requestData.put("uuid", entry.uuid)
            requestData.put("text", entry.text)
            requestData.put("date", entry.date)
            requestData.put("address", entry.address)
            requestData.put("latitude", entry.latitude)
            requestData.put("longitude", entry.longitude)
        } catch (e: Exception) {
            Log.d("dreamland", e.message)
        }
        entryData.put("entry", requestData)
        return entryData
    }

    private fun setEntryAsSynced(entry: Entry) {
        entry.isSynced = true
        entry.save()
    }
}