package ua.meugen.android.levelup.homework6.helpers;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ua.meugen.android.levelup.homework6.data.Entry;

/**
 * @author meugen
 */

public final class DouFeedStoreHelper {

    private static final String TAG = DouFeedStoreHelper.class.getSimpleName();

    private final static Uri URL_ITEMS
            = Uri.parse("content://ua.meugen.android.levelup.homework6/items");
    private final static Uri URL_GUIDS
            = Uri.parse("content://ua.meugen.android.levelup.homework6/guids");

    private final ContentProviderClient client;

    public DouFeedStoreHelper(final ContentProviderClient client) {
        this.client = client;
    }

    public void store(final List<Entry> entries, final SyncResult result) {
        try {
            final Set<String> guids = fetchGuids();

            final ArrayList<ContentProviderOperation> operations = new ArrayList<>(entries.size());
            for (Entry entry : entries) {
                ContentProviderOperation op;
                if (guids.contains(entry.getGuid())) {
                    op = ContentProviderOperation.newUpdate(URL_ITEMS)
                            .withValues(toContentValues(entry))
                            .withSelection("guid=?", new String[] { entry.getGuid() })
                            .build();
                    result.stats.numUpdates++;
                } else {
                    op = ContentProviderOperation.newInsert(URL_ITEMS)
                            .withValues(toContentValues(entry))
                            .build();
                    result.stats.numInserts++;
                }
                operations.add(op);
            }
            this.client.applyBatch(operations);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            result.stats.numIoExceptions++;
        }
    }

    private Set<String> fetchGuids() throws RemoteException {
        Cursor cursor = null;
        try {
            cursor = this.client.query(URL_GUIDS, null, null, null, null);

            final Set<String> result = new HashSet<>();
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0));
            }
            return result;
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
