package felixarpa.shamelessapp.presentation.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.data.AlreadyInitializedException;
import felixarpa.shamelessapp.domain.data.RealmManager;
import io.realm.Realm;

public class SplashActivity extends ShamelessActivity implements Realm.Transaction.OnSuccess,
        Realm.Transaction.OnError, Dialog.OnClickListener, DialogInterface.OnCancelListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            RealmManager.getInstance().initialize(this, this, this);
        } catch (AlreadyInitializedException e) {
            onSuccess();
        }
    }

    @Override
    public void onSuccess() {
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void onError(Throwable error) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle(R.string.init_error_title)
                .setMessage(R.string.init_error_message)
                .setPositiveButton(R.string.init_error_ok, this)
                .setOnCancelListener(this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        finish();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        finish();
    }
}
