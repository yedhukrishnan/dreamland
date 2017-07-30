package dreamlander.dreamland.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import dreamlander.dreamland.R;
import dreamlander.dreamland.activities.CreateEntryActivity;
import dreamlander.dreamland.adapters.EntryListAdapter;
import dreamlander.dreamland.models.Entry;
import dreamlander.dreamland.network.GetEntriesRequest;

public class EntryListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public EntryListFragment() {
        // Required empty public constructor
    }

    public static EntryListFragment newInstance() {
        EntryListFragment fragment = new EntryListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setFloatingActionButton();

        if(isFirstTimeLogin()) {
            saveEntriesFromServer();
        } else {
            List<Entry> entries = Entry.listAll(Entry.class);
            Collections.reverse(entries);
            createEntryListView(entries);
        }
    }

    private void createEntryListView(List<Entry> entries) {
        RecyclerView entryListRecyclerView = getActivity().findViewById(R.id.entry_list_recycler_view);

        entryListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        entryListRecyclerView.setLayoutManager(layoutManager);



        RecyclerView.Adapter entryListAdapter = new EntryListAdapter(entries, getActivity());
        entryListRecyclerView.setAdapter(entryListAdapter);
    }

    private void saveEntriesFromServer() {
        new GetEntriesRequest(getActivity(), new GetEntriesRequest.ResponseListener() {
            @Override
            public void onSuccess(List<Entry> entries) {
                for(Entry entry: entries) {
                    entry.setId(null);
                    entry.setSynced(true);
                    entry.save();
                }
                setFirstTimeLoginAsFalse();

                entries = Entry.listAll(Entry.class);
                createEntryListView(entries);
            }

            @Override
            public void onFailure(String message) {

            }
        }).sendRequest();
    }

    private boolean isFirstTimeLogin() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(getString(R.string.first_time_login), true);
    }

    private void setFirstTimeLoginAsFalse() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.first_time_login), false);
        editor.commit();
    }

    private void setFloatingActionButton() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_create_entry);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CreateEntryActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
