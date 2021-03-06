package dreamlander.dreamland.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import dreamlander.dreamland.R;
import dreamlander.dreamland.helpers.NetworkManager;
import dreamlander.dreamland.models.Entry;
import dreamlander.dreamland.network.CreateEntryRequest;
import dreamlander.dreamland.views.Typewriter;

public class ViewEntryActivity extends AppCompatActivity {

    private Entry entry;
    private TextView entryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        entryTextView = findViewById(R.id.entry_text);

        entry = (Entry) getIntent().getSerializableExtra("entry");
        setEntryDetails();
    }

    private void setEntryDetails() {
        entryTextView.setText(entry.getText());
        Typewriter locationView = findViewById(R.id.location_view);
        locationView.setCharacterDelay(2);
        locationView.animateText(entry.getAddress());
    }

    private void saveEntry() {
        String text = entryTextView.getText().toString();
        if(!text.isEmpty()) {
            entry.setText(text);
            entry.setSynced(false);
            entry.save();
            if(NetworkManager.isNetWorkAvailable(this)) {
                new CreateEntryRequest(this).sendRequest(entry);
            }
        }
    }

    private void showAlertDialogForDeletion() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.confirm_delete_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        entry.setIdToEntryId();
                        entry.delete();
                        finish();
                    }
                })
                .setNegativeButton(R.string.confirm_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_entry, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                this.showAlertDialogForDeletion();
                break;
            case R.id.action_edit:
                entryTextView.setEnabled(true);
                break;
            case android.R.id.home:
                this.saveEntry();
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.saveEntry();
    }
}
