package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.model.NGO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreatePartyFragment.OnCreateFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreatePartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePartyFragment extends ShamelessFragment implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        AdapterView.OnItemSelectedListener {

    private OnCreateFragmentInteractionListener listener;

    private static final String TIME_PATTERN = "HH:mm";
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private String selectedNgo;
    private double latitude;
    private double longitude;

    private ImageView ngoLogo;
    private TextView dateTextView;
    private TextView timeTextView;
    private Spinner ngoSpinner;
    private TextView locationTextView;
    private EditText amountEditText;
    private EditText periodEditText;

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
        selectedNgo = NGO.GREENPEACE;
        latitude = 0.0;
        longitude = 0.0;

        // NGO image logo
        ngoLogo = (ImageView) view.findViewById(R.id.ngo_logo);
        ngoLogo.setImageResource(R.mipmap.greenpeace);

        // Date input layout
        LinearLayout dateLayout = (LinearLayout) view.findViewById(R.id.date_layout);
        dateTextView = (TextView) view.findViewById(R.id.date_text);
        dateLayout.setOnClickListener(this);

        // Time input layout
        LinearLayout timeLayout = (LinearLayout) view.findViewById(R.id.time_layout);
        timeTextView = (TextView) view.findViewById(R.id.time_text);
        timeLayout.setOnClickListener(this);

        // NGO dropdown list
        ngoSpinner = (Spinner) view.findViewById(R.id.ngo_spinner);
        ArrayAdapter<String> ngoAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, NGO.NAME_ARRAY);
        ngoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ngoSpinner.setAdapter(ngoAdapter);
        ngoSpinner.setOnItemSelectedListener(this);

        // Location input layout with PlacePicker (Google)
        LinearLayout locationLayout = (LinearLayout) view.findViewById(R.id.location_layout);
        locationTextView = (TextView) view.findViewById(R.id.location_text);
        locationLayout.setOnClickListener(this);

        // Money-amount-per-time input
        amountEditText = (EditText) view.findViewById(R.id.amount_text);
        periodEditText = (EditText) view.findViewById(R.id.period_text);

        // Party button
        Button partyButton = (Button) view.findViewById(R.id.party_button);
        partyButton.setOnClickListener(this);

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
            case R.id.date_layout:
                listener.onDateClick(this);
                break;

            case R.id.time_layout:
                listener.onTimeClick(this);
                break;

            case R.id.location_layout:
                listener.requestLocation();
                break;

            case R.id.party_button:
                listener.party(calendar.getTime(), selectedNgo, latitude, longitude,
                        getAmount(), getPeriod());
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >= 0 && position < NGO.LOGO_RES_IDs.length) {
            selectedNgo = NGO.NAME_ARRAY[position];
            ngoLogo.setImageResource(NGO.LOGO_RES_IDs[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onLocationSet(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        locationTextView.setText(String.format(Locale.US, "%.6f, %.6f", latitude, latitude));
    }

    private float getAmount() {
        try {
            return Float.parseFloat(amountEditText.getText().toString());
        } catch (NumberFormatException e) {
            return -1.0f;
        }
    }

    private int getPeriod() {
        try {
            return Integer.parseInt(periodEditText.getText().toString());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public interface OnCreateFragmentInteractionListener {
        void onDateClick(DatePickerDialog.OnDateSetListener listener);
        void onTimeClick(TimePickerDialog.OnTimeSetListener listener);
        void requestLocation();
        void party(Date completeDate, String ngo, double latitude, double longitude, float amount,
                   int period);
    }
}
