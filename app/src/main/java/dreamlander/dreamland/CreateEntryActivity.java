package dreamlander.dreamland;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateEntryActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        context = this;

        requestLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("dreamland", "Location Success");
                            Log.d("dreamland", getAddress(location));
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
        addressString += address.getAddressLine(1) + ", ";
        addressString += address.getAddressLine(2);
        return addressString;
    }
}
