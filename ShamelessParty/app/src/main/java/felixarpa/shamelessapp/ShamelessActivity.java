package felixarpa.shamelessapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShamelessActivity extends AppCompatActivity {

    protected SharedPreferences getSharedPreferences() {
        return getSharedPreferences(C.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
