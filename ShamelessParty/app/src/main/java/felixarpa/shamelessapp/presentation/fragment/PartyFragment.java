package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import felixarpa.shamelessapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPartyInitialFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link PartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyFragment extends ShamelessFragment {
    private static final String ARG_PARAM1 = "param1";

    private long partyDate;

    private OnPartyInitialFragmentInteraction mListener;

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
        return inflater.inflate(R.layout.fragment_party, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPartyInitialFragmentInteraction) {
            mListener = (OnPartyInitialFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPartyInitialFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean isCurrentFragment(int id) {
        return id == R.id.navigation_party;
    }

    public interface OnPartyInitialFragmentInteraction extends OnInitialFragmentInteractionListener {
        void tryToCancel();
        void showDistanceHome();
    }

}
