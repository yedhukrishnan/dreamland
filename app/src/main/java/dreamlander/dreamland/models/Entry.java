package dreamlander.dreamland.models;

import android.location.Location;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yedhukrishnan on 06/07/17.
 */

public class Entry extends SugarRecord<Entry> {
    private Date date;
    @Ignore
    private Location location;
    private String address;
    private String text;

    public Entry() {
        this.date = new Date();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(date);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
