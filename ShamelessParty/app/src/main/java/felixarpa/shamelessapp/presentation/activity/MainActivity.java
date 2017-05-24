package felixarpa.shamelessapp.presentation.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import felixarpa.shamelessapp.presentation.fragment.PartyListFragment;
import felixarpa.shamelessapp.presentation.fragment.PlusOneFragment;
import felixarpa.shamelessapp.presentation.fragment.RankingFragment;
import felixarpa.shamelessapp.presentation.fragment.ShamelessFragment;
import felixarpa.shamelessapp.utils.ExitAppTimer;
import io.realm.Realm;

public class MainActivity extends ShamelessActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        PlusOneFragment.OnPlusOneFragmentInteractionListener,
        PartyFragment.OnPartyFragmentInteractionListener,
        CreatePartyFragment.OnCreateFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // region Activity

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
            fragment = PartyFragment.newInstance();
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
                        fragment = PartyFragment.newInstance();
                    } catch (NoSuchPartyGoingOnException e) {
                        fragment = new PlusOneFragment();
                    }
                    break;
                case R.id.navigation_list:
                    fragment = PartyListFragment.newInstance();
                    break;
                case R.id.navigation_stats:
                    fragment = RankingFragment.newInstance();
                    break;
            }
            replace();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onStart() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    // endregion

    // region Plus One Fragment
    @Override
    public void requestPartyCreation() {
        fragment = CreatePartyFragment.newInstance();
        replace();
    }

    // endregion

    // region Party Fragment
    @Override
    public void tryToCancel() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to cancel this party?")
                .setMessage("The world is going to end because of you")
                .setPositiveButton(
                        "Destroy the world",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToMemory();
                            }
                        }
                )
                .setNegativeButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                )
                .show();
    }

    private void goToMemory() {
        startActivity(new Intent(getApplicationContext(), Memory6x6.class));
        finish();
    }

    @Override
    public void finishParty() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to finish this party?")
                .setMessage("You will donate to " + party.getNgo())
                .setPositiveButton(
                        "Save the world",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    PartyControllerImpl.getInstance().commitParty();
                                    stopLocationService();
                                    fragment = new PlusOneFragment();
                                    replace();
                                } catch (NoSuchPartyGoingOnException e) {
                                    Log.wtf("try to finish", "NoSuchPartyGoingOnException: " + e.getMessage());
                                }
                            }
                        }
                )
                .setNegativeButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                )
                .show();
    }

    @Override
    public Party getParty() {
        if (party == null) {
            try {
                return PartyControllerImpl.getInstance().getParty();
            } catch (NoSuchPartyGoingOnException e) {
                fragment = new PlusOneFragment();
                replace();
                return null;
            }
        } else {
            return party;
        }
    }

    private GoogleApiClient googleApiClient;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 3;

    @Override
    public void startLocationService() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }

    @Override
    public void stopLocationService() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    // granted
                } else {
                    // denied
                }
                break;

            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    // granted
                } else {
                    // denied
                }
                break;
        }
    }

    private Location location = null;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        ((PartyFragment) fragment).displayDistanceFromLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    // endregion

    // region Create Party Fragment

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
            } else {
                builder.setView(input);
                builder.setPositiveButton(positiveStr, onPositiveListener);
            }
            builder.setTitle(title).setNegativeButton(negativeStr, onNegativeListener).show();
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

    // endregion

    // region activity methods

    private void replace() {
        googleApiClient = null;
        manager.beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // explanation needed
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // explanation needed
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
            }

            Location mockLocation = new Location("mock location");
            mockLocation.setLatitude(party.getLatitude() + 1.0);
            mockLocation.setLongitude(party.getLongitude());
            return mockLocation;
        } else {
            return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
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
