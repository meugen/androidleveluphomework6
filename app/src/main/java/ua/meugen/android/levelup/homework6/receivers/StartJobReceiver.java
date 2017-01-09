package ua.meugen.android.levelup.homework6.receivers;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ua.meugen.android.levelup.homework6.services.SyncJobService;
import ua.meugen.android.levelup.homework6.utils.SyncUtil;

/**
 * @author meugen
 */

public class StartJobReceiver extends BroadcastReceiver {

    private static int jobId = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Context app = context.getApplicationContext();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SyncUtil.syncManual(app);
        } else {
            final JobInfo.Builder builder = new JobInfo.Builder(++jobId,
                    new ComponentName(app, SyncJobService.class));
            builder.setMinimumLatency(0L);
            builder.setOverrideDeadline(2L * 60L * 60L * 1000L); // 2h
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresCharging(false);
            builder.setRequiresDeviceIdle(false);
            builder.setPersisted(true);

            final JobScheduler scheduler = (JobScheduler) app
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        }
    }
}
