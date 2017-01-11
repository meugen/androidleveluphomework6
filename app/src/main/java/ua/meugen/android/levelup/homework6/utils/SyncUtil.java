package ua.meugen.android.levelup.homework6.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ua.meugen.android.levelup.homework6.providers.RssContent;
import ua.meugen.android.levelup.homework6.receivers.StartJobReceiver;

/**
 * @author meugen
 */

public final class SyncUtil implements RssContent {

    private static final String TAG = SyncUtil.class.getSimpleName();

    private static final String PREF_NAME = "SyncPrefs";
    private static final String LAST_SYNC_DATE_KEY = "last_sync_date";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyyMMdd", Locale.ENGLISH);

    private static final String ACCOUNT_TYPE = "rsscontent.com";
    private static final String ACCOUNT = "meugen";

    private static final Object OBJ = new Object();

    private static Account account;

    private SyncUtil() {}

    public static void syncManual(final Context context) {
        createSyncAccount(context);

        final Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        context.getContentResolver().requestSync(account, AUTHORITY, bundle);
    }

    public static void syncIfNeeded(final Context context) {
        String lastSyncDate;
        synchronized (OBJ) {
            final SharedPreferences prefs = context.getSharedPreferences(
                    PREF_NAME, Context.MODE_PRIVATE);
            lastSyncDate = prefs.getString(LAST_SYNC_DATE_KEY, "");
        }

        final String syncDate = FORMAT.format(new Date());
        if (!syncDate.equals(lastSyncDate)) {
            syncManual(context);
        }
    }

    public static void createSyncAccount(final Context context) {
        if (account != null) {
            return;
        }
        account = new Account(ACCOUNT, ACCOUNT_TYPE);
        final AccountManager manager = (AccountManager) context
                .getSystemService(Context.ACCOUNT_SERVICE);
        boolean res = manager.addAccountExplicitly(account, null, null);
        Log.i(TAG, "" + res);
        if (res) {
            ContentResolver.setIsSyncable(account, AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, AUTHORITY, false);
        }
    }

    public static void setupAlarm(final Context context) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        while (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        final Intent intent = new Intent(context, StartJobReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, 0);

        final AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    public static boolean isSyncActive() {
        return ContentResolver.isSyncActive(account, AUTHORITY);
    }

    public static boolean isSyncPending() {
        return ContentResolver.isSyncActive(account, AUTHORITY);
    }

    public static void updateLastSyncDate(final Context context) {
        synchronized (OBJ) {
            final SharedPreferences prefs = context.getSharedPreferences(
                    PREF_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(LAST_SYNC_DATE_KEY, FORMAT.format(
                    new Date())).apply();
        }
    }
}
