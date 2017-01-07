package ua.meugen.android.levelup.homework6.services;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;

public class SyncJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        return false;
    }
}
