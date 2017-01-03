package ua.meugen.android.levelup.homework6.helpers;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

import ua.meugen.android.levelup.homework6.data.Entry;

/**
 * @author meugen
 */

public final class DouFeedStoreHelper {

    private static final String TAG = DouFeedStoreHelper.class.getSimpleName();

    private final ContentProviderClient client;
    private final Uri url;

    public DouFeedStoreHelper(final ContentProviderClient client) {
        this.client = client;
        this.url = Uri.parse("content://ua.meugen.android.levelup.homework6/items");
    }

    public void store(final List<Entry> entries, final SyncResult result) {
        for (Entry entry : entries) {
            insertOrUpdate(entry, result);
        }
    }

    private void insertOrUpdate(final Entry entry, final SyncResult result) {
        Cursor cursor = null;
        try {
            cursor = this.client.query(this.url, new String[] { "id" }, "guid=?",
                    new String[] { entry.getGuid() }, null);
            if (cursor.moveToFirst()) {
                this.client.update(this.url, toContentValues(entry), "guid=?",
                        new String[] { entry.getGuid() });
                result.stats.numUpdates++;
            } else {
                this.client.insert(this.url, toContentValues(entry));
                result.stats.numInserts++;
            }
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
            result.stats.numIoExceptions++;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private ContentValues toContentValues(final Entry entry) {
        final ContentValues values = new ContentValues();
        values.put("title", entry.getTitle());
        values.put("link", entry.getLink());
        values.put("description", entry.getDescription());
        values.put("pub_date", entry.getPubDate());
        values.put("creator", entry.getCreator());
        values.put("guid", entry.getGuid());
        return values;
    }
}
