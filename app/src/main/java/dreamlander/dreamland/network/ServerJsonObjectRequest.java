package dreamlander.dreamland.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dreamlander.dreamland.R;

/**
 * Created by yedhukrishnan on 19/07/17.
 */

public class ServerJsonObjectRequest extends JsonObjectRequest {
    private Context context;

    public ServerJsonObjectRequest(int method, String url, JSONObject jsonData,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener, Context context) {
        super(method, url, jsonData, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String authToken = context.getSharedPreferences(context
                .getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .getString("auth_token", "");
        Map headers = new HashMap();
        headers.put("Authorization", "Token token=" + authToken);
        return headers;
    }
}
