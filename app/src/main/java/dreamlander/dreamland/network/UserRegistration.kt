package dreamlander.dreamland.network

import android.content.Context

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.Response
import dreamlander.dreamland.configurations.Config
import dreamlander.dreamland.helpers.Logger;


/**
 * Created by yedhukrishnan on 15/07/17.
 */
class UserRegistration(context: Context, responseListener: ResponseListener) {

    private var context: Context = context
    private var responseListener: ResponseListener = responseListener

    fun sendRegistrationRequest(name: String, email: String) {
        var requestQueue = Volley.newRequestQueue(context)

        val registrationRequest = JsonObjectRequest(
                Request.Method.POST,
                Config.REGISTRATION_URL,
                getJsonPayload(name, email),
                Response.Listener<JSONObject> { response ->
                    responseListener.onRegistrationSuccess(response)
                },
                Response.ErrorListener { error ->
                    responseListener.onRegistrationFailure(error.message)
                }
        )
        requestQueue.add(registrationRequest)
    }

    private fun getJsonPayload(name: String, email: String): JSONObject {
        val userData = JSONObject()
        val registrationData = JSONObject()
        try {
            registrationData.put("name", name)
            registrationData.put("email", email)
        } catch (e: Exception) {
            Logger.error(e.message);
        }
        userData.put("user", registrationData)
        return registrationData
    }

    interface ResponseListener {
        fun onRegistrationSuccess(response: JSONObject)
        fun onRegistrationFailure(message: String?)
    }
}