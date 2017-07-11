package dreamlander.dreamland.models;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yedhukrishnan on 06/07/17.
 */

public class Entry extends SugarRecord<Entry> implements Serializable {
    @Expose
    private Long entryId;
    @Expose
    private Date date;
    @Expose
    private String address;
    @Expose
    private String text;
    @Expose
    private double latitude;
    @Expose
    private double longitude;

    private boolean synced = false;

    public Entry() {
        this.date = new Date();
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void save() {
        // Bypassing serialization issue where id (from SugarRecord) is not serialized
        if(entryId == null) {
            entryId = id;
        } else if(id == null) {
            id = entryId;
        }
        super.save();
    }

    public void setEntryIdToId() {
        entryId = id;
    }

    public void setIdToEntryId() {
        id = entryId;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
