package felixarpa.shamelessapp.utils;

import android.os.CountDownTimer;


public abstract class ExitAppTimer extends CountDownTimer {

    private ExitAppTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    private static final long TIME = 2000;

    protected ExitAppTimer() {
        super(TIME, TIME);
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }
}
