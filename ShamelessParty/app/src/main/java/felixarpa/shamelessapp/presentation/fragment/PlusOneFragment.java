package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import felixarpa.shamelessapp.R;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link OnInitialFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PlusOneFragment extends ShamelessFragment implements View.OnClickListener {
    private ImageView addPartyButtonImageView;

    private OnPlusOneFragmentInteractionListener listener;

    public PlusOneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plus_one, container, false);

        ImageView plusOneImageView = (ImageView) view.findViewById(R.id.plus_one_button);
        plusOneImageView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlusOneFragmentInteractionListener) {
            listener = (OnPlusOneFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPlusOneFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        listener.requestPartyCreation();
    }

    public interface OnPlusOneFragmentInteractionListener extends OnInitialFragmentInteractionListener {
        void requestPartyCreation();
    }

}
