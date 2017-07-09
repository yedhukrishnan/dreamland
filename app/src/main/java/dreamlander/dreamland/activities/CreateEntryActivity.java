package dreamlander.dreamland.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dreamlander.dreamland.R;
import dreamlander.dreamland.models.Entry;

public class CreateEntryActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Context context;
    private Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
                            TextView locationView = findViewById(R.id.location_view);
                            locationView.setText(getAddress(location));
                         }
                    }
                });
    }

    private String getAddress(Location location) {
        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList = null;
        try {
            myList = myLocation.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = (Address) myList.get(0);
        String addressString = "";
        addressString += address.getAddressLine(0) + ", ";
        addressString += address.getAddressLine(1);
        return addressString;
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
