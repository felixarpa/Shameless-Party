package felixarpa.shamelessapp.presentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import felixarpa.shamelessapp.utils.C;

public class ShamelessActivity extends AppCompatActivity {

    protected SharedPreferences getSharedPreferences() {
        return getSharedPreferences(C.SPN, MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public void startActivity(Bundle options) {
        Intent intent = new Intent(getApplicationContext(), null); // TODO main activity
        intent.putExtras(options);
        startActivity(intent);
        finish();
    }


}
