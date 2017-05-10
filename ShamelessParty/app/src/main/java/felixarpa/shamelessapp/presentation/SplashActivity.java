package felixarpa.shamelessapp.presentation;

import android.content.SharedPreferences;
import android.os.Bundle;

import felixarpa.shamelessapp.utils.C;
import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.data.RealmManager;

public class SplashActivity extends ShamelessActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences.getBoolean(C.NTI, true)) {
            SharedPreferences.Editor editor = getEditor();
            editor.putBoolean(C.NTI, false);
            editor.apply();
            RealmManager manager = RealmManager.getInstance();
            manager.initialize();
        } else {
            String currentPartyId = sharedPreferences.getString(C.CPI, null);
            Bundle bundle = new Bundle();
            bundle.putString(C.CPI, currentPartyId);
            startActivity(bundle);
        }
    }
}
