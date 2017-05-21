package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.model.Party;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPartyFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyFragment extends ShamelessFragment {
    private static final String ARG_PARAM1 = "param1";

    private long partyDate;

    private OnPartyFragmentInteractionListener listener;
    private Party party;
    private TextView distanceText;

    public PartyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param partyDate Parameter 1.
     * @return A new instance of fragment PartyFragment.
     */
    public static PartyFragment newInstance(long partyDate) {
        PartyFragment fragment = new PartyFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, partyDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partyDate = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_party, container, false);

        party = listener.getParty();
        distanceText = (TextView) view.findViewById(R.id.distance_text);

        listener.startLocationService();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPartyFragmentInteractionListener) {
            listener = (OnPartyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPartyFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public boolean isCurrentFragment(int id) {
        return id == R.id.navigation_party;
    }

    public void displayDistanceFrom(Location currentLocation) {
        Location partyLocation = new Location("Party location");
        partyLocation.setLatitude(party.getLatitude());
        partyLocation.setLongitude(party.getLongitude());
        double distance = currentLocation.distanceTo(partyLocation);
        distanceText.setText(String.format("%f", distance));
    }

    public interface OnPartyFragmentInteractionListener {
        void startLocationService();
        void tryToCancel();
        void showDistanceHome();
        Party getParty();
    }

}
