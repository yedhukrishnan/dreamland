package dreamlander.dreamland.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.toolbox.Volley
import dreamlander.dreamland.configurations.Config
import dreamlander.dreamland.helpers.Logger
import dreamlander.dreamland.models.Entry
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by yedhukrishnan on 19/07/17.
 */
class CreateEntryRequest(context: Context) {
    private var context: Context = context
    private var entriesList: ArrayList<Entry> = ArrayList()

    fun sendRequest(entry: Entry) {
        entriesList.add(entry)
        sendRequestToServer(getJsonPayload(entriesList))
    }

    fun sendRequest(entries: List<Entry>) {
        entriesList = entries as ArrayList<Entry>
        sendRequestToServer(getJsonPayload(entriesList))
    }

    private fun sendRequestToServer(entriesPayLoad: JSONObject) {
        val requestQueue = Volley.newRequestQueue(context)

        val request = ServerJsonObjectRequest(


                Request.Method.POST,
                Config.CREATE_ENTRY_URL,
                entriesPayLoad,
                Response.Listener<JSONObject>({ response ->
                    setEntriesAsSynced(entriesList)
                }),
                ErrorListener { error ->
                    Logger.error(error.message);
                    try {
                        var successfulEntriesUuid: MutableList<String> =
                                getUuids(JSONObject(String(error.networkResponse.data))["success"] as JSONArray)
                        setEntriesAsSynced(entriesList, successfulEntriesUuid)
                    } catch (e: Exception) {
                        Logger.error(e.message)
                    }
                },
                context
        )
        requestQueue.add(request)
    }

    private fun getJsonPayload(entries: ArrayList<Entry>): JSONObject {
        val entriesArray = JSONArray()
        val entryData = JSONObject()
        try {
            for(entry in entries) {
                val requestData = JSONObject()
                requestData.put("uuid", entry.uuid)
                requestData.put("text", entry.text)
                requestData.put("date", entry.date)
                requestData.put("address", entry.address)
                requestData.put("latitude", entry.latitude)
                requestData.put("longitude", entry.longitude)
                entriesArray.put(requestData)
            }
        } catch (e: Exception) {
            Log.d("dreamland", e.message)
        }
        entryData.put("entry", entriesArray)
        return entryData
    }

    private fun setEntriesAsSynced(
            entries: ArrayList<Entry>,
            successfulEntriesUuid: MutableList<String>) {

        for(entry in entries) {
            if(entry.uuid in successfulEntriesUuid) {
                setEntryAsSynced(entry)
            } else {
                Logger.error("Server sync failed for ${entry.uuid}. Will try again later.")
            }
        }
    }

    private fun setEntriesAsSynced(entries: ArrayList<Entry>) {
        for(entry in entries) {
            setEntryAsSynced(entry)
        }
    }

    private fun setEntryAsSynced(entry: Entry) {
        Logger.debug("Server sync success for ${entry.uuid}")
        entry.isSynced = true
        entry.save()
    }

    private fun getUuids(uuidJsonArray: JSONArray): MutableList<String> {
        var uuids: MutableList<String> = mutableListOf<String>()
        for(i in 0..(uuidJsonArray.length() - 1)) {
            uuids.add(uuidJsonArray.get(i).toString())
        }
        return uuids;
    }
}