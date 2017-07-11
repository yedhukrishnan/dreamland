package dreamlander.dreamland.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

import dreamlander.dreamland.R;
import dreamlander.dreamland.activities.CreateEntryActivity;
import dreamlander.dreamland.adapters.EntryListAdapter;
import dreamlander.dreamland.models.Entry;

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
        createEntryListView();
    }

    private void createEntryListView() {
        RecyclerView entryListRecyclerView = getActivity().findViewById(R.id.entry_list_recycler_view);

        entryListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        entryListRecyclerView.setLayoutManager(layoutManager);

        List<Entry> entries = Entry.listAll(Entry.class);
        Collections.reverse(entries);

        RecyclerView.Adapter entryListAdapter = new EntryListAdapter(entries, getActivity());
        entryListRecyclerView.setAdapter(entryListAdapter);
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
