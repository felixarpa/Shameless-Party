package felixarpa.shamelessapp.presentation.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.controller.exception.NoSuchPartyGoingOnException;
import felixarpa.shamelessapp.domain.data.PartyControllerImpl;
import felixarpa.shamelessapp.domain.model.Party;
import felixarpa.shamelessapp.presentation.fragment.PartyFragment;
import felixarpa.shamelessapp.presentation.fragment.PlusOneFragment;
import felixarpa.shamelessapp.presentation.fragment.ShamelessFragment;
import felixarpa.shamelessapp.utils.ExitAppTimer;

public class MainActivity extends ShamelessActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        PlusOneFragment.OnPlusOneFragmentInteractionListener,
        PartyFragment.OnPartyInitialFragmentInteraction {

    private FragmentManager manager;
    private ShamelessFragment fragment;
    private Party party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private boolean secondBack = false;

    @Override
    public void onBack() {
        if (secondBack) {
            finish();
        } else {
            secondBack = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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

    @Override
    public void requestPartyCreation() {
        Toast.makeText(this, "requestPartyCreation", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tryToCancel() {

    }

    @Override
    public void showDistanceHome() {

    }



    // region private methods

    private void replace() {
        manager.beginTransaction().replace(R.id.content, fragment).commit();
    }

    // endregion
}
