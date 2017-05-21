package felixarpa.shamelessapp.presentation.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.Date;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.controller.exception.AlreadyPartyingException;
import felixarpa.shamelessapp.domain.controller.exception.InvalidAmountException;
import felixarpa.shamelessapp.domain.controller.exception.InvalidLocationException;
import felixarpa.shamelessapp.domain.controller.exception.NoSuchPartyGoingOnException;
import felixarpa.shamelessapp.domain.controller.exception.PastPartyException;
import felixarpa.shamelessapp.domain.data.PartyControllerImpl;
import felixarpa.shamelessapp.domain.model.Party;
import felixarpa.shamelessapp.presentation.fragment.CreatePartyFragment;
import felixarpa.shamelessapp.presentation.fragment.PartyFragment;
import felixarpa.shamelessapp.presentation.fragment.PlusOneFragment;
import felixarpa.shamelessapp.presentation.fragment.ShamelessFragment;
import felixarpa.shamelessapp.utils.ExitAppTimer;
import io.realm.Realm;

public class MainActivity extends ShamelessActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        PlusOneFragment.OnPlusOneFragmentInteractionListener,
        PartyFragment.OnPartyFragmentInteractionListener,
        CreatePartyFragment.OnCreateFragmentInteractionListener {

    public static final int PLACE_PICKER_REQUEST = 1;
    private FragmentManager manager;
    private ShamelessFragment fragment;
    private Party party;
    private Calendar calendar;
    private boolean secondBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(getApplicationContext());

        calendar = Calendar.getInstance();

        try {
            party = PartyControllerImpl.getInstance().getParty();
            fragment = PartyFragment.newInstance(party.getHour());
        } catch (NoSuchPartyGoingOnException e) {
            fragment = new PlusOneFragment();
        }

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (secondBack) {
            finish();
        } else {
            secondBack = true;
            Toast.makeText(this, R.string.first_back_message, Toast.LENGTH_SHORT).show();
            new ExitAppTimer() {
                @Override
                public void onFinish() {
                    secondBack = false;
                }
            }.start();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (!fragment.isCurrentFragment(id)) {
            switch (id) {
                case R.id.navigation_party:
                    try {
                        party = PartyControllerImpl.getInstance().getParty();
                        fragment = PartyFragment.newInstance(party.getHour());
                    } catch (NoSuchPartyGoingOnException e) {
                        fragment = new PlusOneFragment();
                    }
                    break;
                case R.id.navigation_list:
                    // fragment = PartyListFragment.newInstance();
                    break;
                case R.id.navigation_stats:

                    break;
            }
            replace();
            return true;
        } else {
            return false;
        }
    }

    // Plus One Fragment
    @Override
    public void requestPartyCreation() {
        fragment = CreatePartyFragment.newInstance();
        replace();
    }

    // Party Fragment
    @Override
    public void tryToCancel() {
        Toast.makeText(this, "tryToCancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDistanceHome() {
        Toast.makeText(this, "showDistanceHome", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Party getParty() throws NoSuchPartyGoingOnException {
        if (party == null) {
            party = PartyControllerImpl.getInstance().getParty();
        }
        return party;
    }


    @Override
    public void onDateClick(DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog.newInstance(
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeClick(TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialog.newInstance(
                listener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show(getFragmentManager(), "timePicker");
    }

    private CreatePartyFragment listener;

    @Override
    public void requestLocation() {
        listener = (CreatePartyFragment) fragment;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this, R.string.private_software_location_error_message,
                    Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, R.string.location_error, Toast.LENGTH_SHORT).show();
        }
    }

    private EditText input = null;

    @Override
    public void party(final Date completeDate, final String ngo, final double latitude,
                      final double longitude, final float amount, final int period) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        int title = R.string.party_title_dialog;
        int message = R.string.app_name;
        int positiveStr = R.string.continue_str;
        int negativeStr = R.string.cancel;

        input = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        int dpValue = 16; // margin in dips
        float d = getResources().getDisplayMetrics().density;
        int margin = (int)(dpValue * d);
        layoutParams.setMarginStart(margin);
        layoutParams.setMarginEnd(margin);
        input.setLayoutParams(layoutParams);

        DialogInterface.OnClickListener onPositiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int message = R.string.party_create_success_message;
                try {
                    String title = input.getText().toString();
                    PartyControllerImpl.getInstance().createNewParty(title, completeDate, amount,
                            period, ngo, latitude, longitude);
                } catch (Exception e) {
                    message = R.string.party_create_failure_message;
                } finally {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        };
        DialogInterface.OnClickListener onNegativeListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };

        try {
            PartyControllerImpl.getInstance().checkNewPartyValues(completeDate, amount, period,
                    latitude, longitude);
        } catch (AlreadyPartyingException e) {
            input = null;
            title = R.string.already_partying_creation_title;
            message = R.string.already_partying_creation_message;
        } catch (InvalidAmountException e) {
            input = null;
            title = R.string.invalid_amount_creation_title;
            message = R.string.invalid_amount_creation_message;
        } catch (PastPartyException e) {
            input = null;
            title = R.string.past_party_creation_title;
            message = R.string.past_party_creation_message;
        } catch (InvalidLocationException e) {
            input = null;
            title = R.string.wierd_location_creation_title;
            message = R.string.wierd_location_creation_message;
        } finally {
            if (input == null) {
                builder.setMessage(message);
                onPositiveListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(getApplicationContext(), MemoryActivity.class));
//                        finish();
                    }
                };
            } else {
                builder.setView(input);
            }
            builder.setTitle(title).setPositiveButton(positiveStr, onPositiveListener)
                    .setNegativeButton(negativeStr, onNegativeListener).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                listener.onLocationSet(latitude, longitude);
            }
        }
    }


    // region private methods

    private void replace() {
        manager.beginTransaction().replace(R.id.content, fragment).commit();
    }

    // endregion
}


/*
<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from
<a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by
<a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0"
target="_blank">CC 3.0 BY</a></div>

<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from
<a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by
<a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0"
target="_blank">CC 3.0 BY</a></div>
 */
