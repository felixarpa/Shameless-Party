package felixarpa.shamelessapp.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        AdapterView.OnItemSelectedListener
{

    private OnCreateFragmentInteractionListener listener;

    private static final String TIME_PATTERN = "HH:mm";
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private ImageView ngoLogo;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView locationTextView;
    private Spinner ngoSpinner;

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

        ngoLogo = (ImageView) view.findViewById(R.id.ngo_logo);
        dateTextView = (TextView) view.findViewById(R.id.date_text);
        timeTextView = (TextView) view.findViewById(R.id.time_text);
        locationTextView = (TextView) view.findViewById(R.id.location_text);
        ngoSpinner = (Spinner) view.findViewById(R.id.ngo_spinner);
        LinearLayout dateLayout = (LinearLayout) view.findViewById(R.id.date_layout);
        LinearLayout timeLayout = (LinearLayout) view.findViewById(R.id.time_layout);
        LinearLayout locationLayout = (LinearLayout) view.findViewById(R.id.location_layout);

        ngoLogo.setImageResource(R.mipmap.greenpeace);

        dateLayout.setOnClickListener(this);
        timeLayout.setOnClickListener(this);
        locationLayout.setOnClickListener(this);

        ArrayAdapter<String> ngoAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, NGO.NAME_ARRAY);
        ngoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ngoSpinner.setAdapter(ngoAdapter);
        ngoSpinner.setOnItemSelectedListener(this);

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
            ngoLogo.setImageResource(NGO.LOGO_RES_IDs[position]);
        } else {
            ngoLogo.setImageResource(R.mipmap.earth);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onLocationSet(double latitude, double longitude) {
        locationTextView.setText(String.format(Locale.US, "%.6f, %.6f", latitude, latitude));
    }

    public interface OnCreateFragmentInteractionListener {
        void onDateClick(DatePickerDialog.OnDateSetListener listener);
        void onTimeClick(TimePickerDialog.OnTimeSetListener listener);
        void requestLocation();
    }
}
