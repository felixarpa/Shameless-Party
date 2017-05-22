package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

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
public class PartyFragment extends ShamelessFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    private OnPartyFragmentInteractionListener listener;
    private Party party;
    private TextView distanceTextView;
    private TextView timeTextView;
    private TextView moneyTextView;
    private TextView finishButton;
    private CountDownTimer countDownTimer;
    private boolean paying;

    public PartyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PartyFragment.
     */
    public static PartyFragment newInstance() {
        return new PartyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_party, container, false);

        party = listener.getParty();

        TextView titleTextView = (TextView) view.findViewById(R.id.party_title);
        distanceTextView = (TextView) view.findViewById(R.id.party_distance);
        timeTextView = (TextView) view.findViewById(R.id.party_remaining_time);
        moneyTextView = (TextView) view.findViewById(R.id.party_money);
        TextView cancelButton = (TextView) view.findViewById(R.id.cancel);
        finishButton = (TextView) view.findViewById(R.id.finish);

        titleTextView.setText(party.getTitle());
        distanceTextView.setOnClickListener(this);
        listener.startLocationService();
        startCountdownTimer();
        cancelButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);

        return view;
    }

    private void countMoney(long minutes) {
        int money = (int) (party.getMoneyAmount() * minutes / party.getMinutes());
        moneyTextView.setText(money + " €");
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisTilEnd) {
                long time = party.getHour() - System.currentTimeMillis();
                long absTime = Math.abs(time);
                long hours = TimeUnit.MILLISECONDS.toHours(absTime);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(absTime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(absTime));
                long seconds = TimeUnit.MILLISECONDS.toSeconds(absTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(absTime));

                String timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                if (time < 0) {
                    timeTextView.setTextColor(0xffff2222);
                    timeTextView.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_timer_off_white_48dp, 0, 0, 0);
                    if (minutes % party.getMinutes() == 0) {
                        countMoney(minutes);
                    }
                }
                timeTextView.setText(timeStr);
            }

            @Override
            public void onFinish() {}
        }.start();
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
        countDownTimer.cancel();
    }

    @Override
    public boolean isCurrentFragment(int id) {
        return id == R.id.navigation_party;
    }

    public void displayDistanceFromLocation() {
        Location currentLocation = listener.getLocation();
        if (currentLocation != null) {
            Location partyLocation = new Location("Party location");
            partyLocation.setLatitude(party.getLatitude());
            partyLocation.setLongitude(party.getLongitude());
            double distance = currentLocation.distanceTo(partyLocation);

            if (distance < 20.0) {
                finishButton.setVisibility(View.VISIBLE);
            } else {
                finishButton.setVisibility(View.GONE);
            }
            String distanceStr = String.format("%,d m from home", (int) distance);
            distanceTextView.setText(distanceStr.replace(",", "."));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.party_distance:
                displayDistanceFromLocation();
                break;

            case R.id.cancel:
                listener.tryToCancel();
                break;

            case R.id.finish:
                listener.finishParty();
                break;
        }
    }

    public interface OnPartyFragmentInteractionListener {
        void startLocationService();
        Location getLocation();
        void stopLocationService();
        void tryToCancel();
        void finishParty();
        Party getParty();
    }

}
