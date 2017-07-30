package dreamlander.dreamland.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

/**
 * Created by yedhukrishnan on 10/07/17.
 */

public class Typewriter extends android.support.v7.widget.AppCompatTextView {

    private CharSequence text;
    private int index;
    private long delay = 500; //Default 500ms delay
    private Handler handler = new Handler();

    public Typewriter(Context context) {
        super(context);
    }

    public Typewriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0, index++));
            if(index <= text.length()) {
                handler.postDelayed(characterAdder, delay);
            }
        }
    };

    public void animateText(CharSequence text) {
        if(text == null) {
            return;
        }
        this.text = text;
        index = 0;

        setText("");
        handler.removeCallbacks(characterAdder);
        handler.postDelayed(characterAdder, delay);
    }

    public void setCharacterDelay(long millis) {
        delay = millis;
    }
}
