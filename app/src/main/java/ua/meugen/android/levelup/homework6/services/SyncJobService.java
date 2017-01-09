package ua.meugen.android.levelup.homework6.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;

import ua.meugen.android.levelup.homework6.utils.SyncUtil;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SyncJobService extends JobService {

    private static final long FINISH_DELAY = 5L * 1000L;

    private final Handler handler = new Handler();

    @Override
    public boolean onStartJob(final JobParameters params) {
        SyncUtil.syncManual(this);

        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jobFinished(params, false);
            }
        }, FINISH_DELAY);
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        return false;
    }
}
