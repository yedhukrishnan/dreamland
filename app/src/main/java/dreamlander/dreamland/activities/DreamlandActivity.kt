package dreamlander.dreamland.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import dreamlander.dreamland.R
import dreamlander.dreamland.adapters.EntryListAdapter
import dreamlander.dreamland.models.Entry

import kotlinx.android.synthetic.main.activity_dreamland.*

class DreamlandActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dreamland)
        setSupportActionBar(toolbar)

        requestLocationPerission()

        fab.setOnClickListener { view ->
            val intent = Intent(this, CreateEntryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        createEntryListView()

    }

    private fun createEntryListView() {
        var entryListRecyclerView = findViewById<RecyclerView>(R.id.entry_list_recycler_view) as RecyclerView

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        entryListRecyclerView.setHasFixedSize(true);

        var layoutManager = LinearLayoutManager(this);
        entryListRecyclerView.setLayoutManager(layoutManager);

        var entries = Entry.listAll(Entry::class.java)

        var entryListAdapter = EntryListAdapter(entries)
        entryListRecyclerView.setAdapter(entryListAdapter);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_dreamland, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun requestLocationPerission(): Unit {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }
}
