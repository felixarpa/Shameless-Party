package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.data.PartyControllerImpl;
import felixarpa.shamelessapp.domain.model.Party;
import felixarpa.shamelessapp.presentation.adapter.PartyRecyclerViewAdapter;

public class PartyListFragment extends ShamelessFragment {

    public PartyListFragment() {
    }

    public static PartyListFragment newInstance() {
        return new PartyListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.party_layout, container, false);
        ArrayList<Party> parties = PartyControllerImpl.getInstance().getAllParties();
        for (int i = 0; i < parties.size(); ++i) {
            if (parties.get(i).isGoingOn()) {
                parties.remove(i);
            }
        }
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new PartyRecyclerViewAdapter(parties));
        return view;
    }

    @Override
    public boolean isCurrentFragment(int id) {
        return id == R.id.navigation_list;
    }
}
