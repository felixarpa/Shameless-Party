package felixarpa.shamelessapp.presentation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import felixarpa.shamelessapp.utils.C;

public abstract class ShamelessActivity extends AppCompatActivity {

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(C.SPN, MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public void startActivity(Class<?> activity) {
        Intent intent = new Intent(getApplicationContext(), activity); // TODO main activity
        startActivity(intent);
        finish();
    }


}
