package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import felixarpa.shamelessapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreatePartyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreatePartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePartyFragment extends ShamelessFragment implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private OnCreateFragmentInteractionListener listener;

    private static final String TIME_PATTERN = "HH:mm";
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private TextView dateTextView;
    private TextView timeTextView;
    private Button locationButton;

    public CreatePartyFragment() {
        // Required empty public constructor
    }

    public static CreatePartyFragment newInstance() {
        return new CreatePartyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_party, container, false);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        dateTextView = (TextView) view.findViewById(R.id.date);
        timeTextView = (TextView) view.findViewById(R.id.time);
        locationButton = (Button) view.findViewById(R.id.location_button);

        dateTextView.setOnClickListener(this);
        timeTextView.setOnClickListener(this);
        locationButton.setOnClickListener(this);

        update();

        return view;
    }

    private void update() {
        dateTextView.setText(dateFormat.format(calendar.getTime()));
        timeTextView.setText(timeFormat.format(calendar.getTime()));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateFragmentInteractionListener) {
            listener = (OnCreateFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                listener.onDateClick(this);
                break;

            case R.id.time:
                listener.onTimeClick(this);
                break;

            case R.id.location_button:
                listener.requestLocation();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }

    public interface OnCreateFragmentInteractionListener {
        void onDateClick(DatePickerDialog.OnDateSetListener listener);
        void onTimeClick(TimePickerDialog.OnTimeSetListener listener);
        void requestLocation();
    }
}
