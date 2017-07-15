package dreamlander.dreamland.helpers;

import android.util.Log;

import dreamlander.dreamland.configurations.Config;

/**
 * Created by yedhukrishnan on 15/07/17.
 */

public class Logger {
    public static void debug(String message) {
        Log.d(Config.LOG_TAG, message);
    }

    public static void error(String message) {
        Log.e(Config.LOG_TAG, message);
    }
}
