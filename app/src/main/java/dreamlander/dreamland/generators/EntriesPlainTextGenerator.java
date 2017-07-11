package dreamlander.dreamland.generators;

import java.util.List;

import dreamlander.dreamland.models.Entry;

/**
 * Created by yedhukrishnan on 11/07/17.
 */

public class EntriesPlainTextGenerator {

    private final List<Entry> entries;

    public EntriesPlainTextGenerator() {
        entries = Entry.listAll(Entry.class);
    }

    public String generate() {
        String entriesText = "";

        for(Entry entry: entries) {
            entriesText += "Date : " + entry.getDate().toString() + "\n";
            entriesText += "Address : " + entry.getAddress() + "\n";
            entriesText += "Latitude : " + String.valueOf(entry.getLatitude()) + "\n";
            entriesText += "Longitude : " + String.valueOf(entry.getLongitude()) + "\n\n";
            entriesText += entry.getText() + "\n\n---\n\n";
        }

        return entriesText;
    }
}
