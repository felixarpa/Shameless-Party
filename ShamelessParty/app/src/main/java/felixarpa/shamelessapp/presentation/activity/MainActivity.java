package felixarpa.shamelessapp.presentation.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.model.Party;
import felixarpa.shamelessapp.presentation.fragment.PartyFragment;
import felixarpa.shamelessapp.presentation.fragment.PartyListFragment;
import felixarpa.shamelessapp.presentation.fragment.ShamelessFragment;

public class MainActivity extends ShamelessActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ShamelessFragment fragment;
    private Party party;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        boolean result = false;
        if (!fragment.isCurrentFragment(id)) {
            switch (id) {
                case R.id.navigation_party:
                    fragment = PartyFragment.newInstance(party.getHour());
                    break;
                case R.id.navigation_list:
                    fragment = PartyListFragment.newInstance(1);
                    break;
                case R.id.navigation_stats:

                    break;
            }
            // TODO swap fragment
        }
            return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

}
