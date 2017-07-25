package dreamlander.dreamland.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import dreamlander.dreamland.configurations.Config;
import dreamlander.dreamland.models.Entry;
import dreamlander.dreamland.helpers.Logger;

/**
 * Created by yedhukrishnan on 25/07/17.
 */

public class GetEntriesRequest {
    private Context context;

    public GetEntriesRequest(Context context) {
        this.context = context;
    }

    public void sentRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Request request = new ServerJsonObjectRequest(
                Request.Method.GET,
                Config.ENTRIES_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Type type = new TypeToken<List<Entry>>() {}.getType();
                        new Gson().fromJson(response.toString(), type);
                        Logger.debug("lol");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                context
        );
        requestQueue.add(request);
    }
}
