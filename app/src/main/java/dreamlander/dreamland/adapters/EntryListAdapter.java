package dreamlander.dreamland.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dreamlander.dreamland.R;
import dreamlander.dreamland.models.Entry;

/**
 * Created by yedhukrishnan on 08/07/17.
 */

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ViewHolder> {
    private List<Entry> entries;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView entryTextView, dateTextView;

        public ViewHolder(View view) {
            super(view);
            entryTextView = view.findViewById(R.id.entry_text_view);
            dateTextView = view.findViewById(R.id.date_text_view);
        }

        List<Entry> entries = Entry.listAll(Entry.class);
    }

    public EntryListAdapter(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View entryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(entryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Entry entry = entries.get(position);
        Log.d("dreamland", entry.getText());
        holder.entryTextView.setText(entry.getText());
        holder.dateTextView.setText(entry.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
