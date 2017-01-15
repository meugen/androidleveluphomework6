package ua.meugen.android.levelup.homework6.services;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import ua.meugen.android.levelup.homework6.data.Entry;
import ua.meugen.android.levelup.homework6.helpers.DouFeedFetchHelper;
import ua.meugen.android.levelup.homework6.helpers.DouFeedStoreHelper;
import ua.meugen.android.levelup.homework6.utils.SyncUtil;

public class SyncService extends Service {

    private static final String TAG = SyncService.class.getSimpleName();

    private static final Object OBJ = new Object();
    private static SyncAdapter syncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate()");
        synchronized (OBJ) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(this);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}

class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();
    private static final String URL_STRING = "https://dou.ua/feed/";

    SyncAdapter(final Context context) {
        super(context, true);
    }

    @Override
    public void onPerformSync(
            final Account account,
            final Bundle extras,
            final String authority,
            final ContentProviderClient client,
            final SyncResult result) {
        try {
            final DouFeedFetchHelper fetchHelper
                    = new DouFeedFetchHelper(URL_STRING);
            final List<Entry> items = fetchHelper.fetch();
            Log.i(TAG, items.toString());

            final DouFeedStoreHelper storeHelper
                    = new DouFeedStoreHelper(client);
            storeHelper.store(items, result);

            SyncUtil.updateLastSyncDate(getContext());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            result.stats.numIoExceptions++;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            result.stats.numParseExceptions++;
        }
    }
}
