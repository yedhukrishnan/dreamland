package dreamlander.dreamland.models;

import android.location.Location;

import java.util.Date;

/**
 * Created by yedhukrishnan on 06/07/17.
 */

public class Entry {
    private Date date;
    private Location location;

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
}
