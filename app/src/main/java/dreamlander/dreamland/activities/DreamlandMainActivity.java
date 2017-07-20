package dreamlander.dreamland.activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import dreamlander.dreamland.R;
import dreamlander.dreamland.fragments.CalendarFragment;
import dreamlander.dreamland.fragments.EntryListFragment;
import dreamlander.dreamland.generators.EntriesPlainTextGenerator;
import dreamlander.dreamland.helpers.NetworkManager;
import dreamlander.dreamland.models.Entry;
import dreamlander.dreamland.network.CreateEntryRequest;

public class DreamlandMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            EntryListFragment.OnFragmentInteractionListener,
            CalendarFragment.OnFragmentInteractionListener {

    private List<Entry> entries;
    private NavigationView navigationView;
    private View headerView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if(!isUserLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_dreamland_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setNavigationDrawer(toolbar);
        setNameAndEmail();

        setEntryListFragment();
        requestLocationPermission();

        if(NetworkManager.isNetWorkAvailable(this)) {
            syncPendingEntries();
        }
    }

    private void syncPendingEntries() {
        List<Entry> entries = Entry.find(Entry.class, "synced = ?", "0");
        if(entries.size() > 0) {
            new CreateEntryRequest(this).sendRequest(entries);
        }
    }

    private boolean isUserLoggedIn() {
        String name = sharedPreferences.getString("name", "Dreamlander");
        return !name.equals("Dreamlander");
    }

    private void setNameAndEmail() {
        String name = sharedPreferences.getString("name", "Dreamlander");
        String email = sharedPreferences.getString("email", "dreamlandapp@gmail.com");

        setTextOnView(R.id.username_view, name);
        setTextOnView(R.id.email_view, email);

//        ImageView imageView = headerView.findViewById(R.id.profile_pic_view);
//        imageView.setImageBitmap(getProfileImage());
    }

    private void setTextOnView(int id, String text) {
        TextView view = headerView.findViewById(id);
        view.setText(text);
    }

    @Nullable
    private Bitmap getProfileImage() {
        File file =new File(getFilesDir(), "profile_picture.jpg");
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();

        entries = Entry.listAll(Entry.class);
        Collections.reverse(entries);
    }

    private void setNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
    }

    private void setEntryListFragment() {
        Fragment fragment = EntryListFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.dreamland_main_container, fragment);
        fragmentTransaction.commit();
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        }
    }

    private void requestWriteExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.dreamland_main_container, fragment);
        fragmentTransaction.commit();
    }

    private void manageShareBackup() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.share_backup_message)
                .setNegativeButton(R.string.share_json_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        String entriesData = gson.toJson(entries);
                        showShareOptions(entriesData, "text/json");
                    }
                })
                .setPositiveButton(R.string.share_plain_text_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String entriesData = new EntriesPlainTextGenerator().generate();
                        showShareOptions(entriesData, "text/plain");
                    }
                })
                .create();
        dialog.show();


    }

    private void showShareOptions(String entriesData, String type) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, entriesData);
        sendIntent.setType(type);
        startActivity(sendIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dreamland_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.entry_list_drawer_item) {
            setFragment(EntryListFragment.newInstance());
        } else if (id == R.id.calendar_view_drawer_item) {
            setFragment(CalendarFragment.newInstance("", ""));
        } else if (id == R.id.share_backup_drawer_item) {
            manageShareBackup();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(@NotNull Uri uri) {

    }
}
