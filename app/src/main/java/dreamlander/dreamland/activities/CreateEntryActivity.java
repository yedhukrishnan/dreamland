package dreamlander.dreamland.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dreamlander.dreamland.R;
import dreamlander.dreamland.models.Entry;
import dreamlander.dreamland.network.CreateEntryRequest;
import dreamlander.dreamland.views.Typewriter;

public class CreateEntryActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Context context;
    private Entry entry;
    private boolean deleteEntry = false;
    private TextView entryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        entryTextView = findViewById(R.id.entry_text);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        context = this;
        entry = new Entry();

        requestAndSetLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestAndSetLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("dreamland", "Located successfully");
                            entry.setLatitude(location.getLatitude());
                            entry.setLongitude(location.getLongitude());
                            entry.setAddress(getAddress(location));

                            Typewriter locationView = findViewById(R.id.location_view);
                            locationView.setCharacterDelay(2);
                            locationView.animateText(getAddress(location));
                        }
                    }
                });
    }

    private String getAddress(Location location) {
        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList;
        String addressString = "";

        try {
            myList = myLocation.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = myList.get(0);
            addressString += address.getAddressLine(0) + ", ";
            addressString += address.getAddressLine(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressString;
    }

    private void saveEntry() {
        String text = entryTextView.getText().toString();
        if(!text.isEmpty()) {
            entry.setText(text);
            entry.setSynced(false);
            entry.save();
            new CreateEntryRequest(this).sendRequest(entry);
        }
    }

    private void showAlertDialogForDeletion() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.confirm_delete_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(entry.getId() != null) {
                            entry.delete();
                        }
                        deleteEntry = true;
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
        getMenuInflater().inflate(R.menu.menu_create_entry, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                this.showAlertDialogForDeletion();
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
        if(!deleteEntry) {
            this.saveEntry();
        }
    }
}
