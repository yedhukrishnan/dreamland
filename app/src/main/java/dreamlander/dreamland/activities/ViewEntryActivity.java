package dreamlander.dreamland.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import dreamlander.dreamland.R;
import dreamlander.dreamland.models.Entry;
import dreamlander.dreamland.views.Typewriter;

public class ViewEntryActivity extends AppCompatActivity {

    private Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        entry = (Entry) getIntent().getSerializableExtra("entry");
        setEntryDetails();
    }

    private void setEntryDetails() {
        TextView entryTextView = findViewById(R.id.entry_text);
        entryTextView.setText(entry.getText());
        Typewriter locationView = findViewById(R.id.location_view);
        locationView.setCharacterDelay(2);
        locationView.animateText(entry.getAddress());
    }

    private void saveEntry() {
        EditText entryText = findViewById(R.id.entry_text);
        entry.setText(entryText.getText().toString());
        entry.save();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.saveEntry();
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.saveEntry();
    }
}
