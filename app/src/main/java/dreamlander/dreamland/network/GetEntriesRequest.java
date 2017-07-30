package dreamlander.dreamland.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dreamlander.dreamland.configurations.Config;
import dreamlander.dreamland.helpers.Logger;
import dreamlander.dreamland.models.Entry;

/**
 * Created by yedhukrishnan on 25/07/17.
 */

public class GetEntriesRequest {
    private Context context;
    private ResponseListener responseListener;

    public GetEntriesRequest(Context context, ResponseListener responseListener) {
        this.context = context;
        this.responseListener = responseListener;
    }

    public void sendRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Request request = new ServerJsonObjectRequest(
                Request.Method.GET,
                Config.ENTRIES_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Type type = new TypeToken<List<Entry>>() {}.getType();
                        ArrayList<Entry> entries;
                        try {
                            entries = new Gson().fromJson(response.getString("entries").toString(), type);
                            responseListener.onSuccess(entries);
                        } catch (JSONException e) {
                            Logger.error(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.debug("na");
                    }
                },
                context
        );
        requestQueue.add(request);
    }

    public interface ResponseListener {
        void onSuccess(List<Entry> entries);
        void onFailure(String message);
    }
}
