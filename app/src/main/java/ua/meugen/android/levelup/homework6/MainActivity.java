package ua.meugen.android.levelup.homework6;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ua.meugen.android.levelup.homework6.providers.RssContent;
import ua.meugen.android.levelup.homework6.utils.SyncUtil;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, RssContent,
        AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final SyncStatusObserver syncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            MainActivity.this.runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    onSyncStatusChanged();
                }
            });
        }
    };

    private SimpleCursorAdapter adapter;
    private Menu optionsMenu;

    private Object syncObserverHandle;

    private void onSyncStatusChanged() {
        if (optionsMenu == null) {
            return;
        }
        final boolean syncActive = SyncUtil.isSyncActive();
        final boolean syncPending = SyncUtil.isSyncPending();

        final MenuItem refreshItem = optionsMenu.findItem(R.id.refresh);
        if (refreshItem != null) {
            if (syncActive || syncPending) {
                MenuItemCompat.setActionView(refreshItem, R.layout.refresh_progress);
            } else {
                MenuItemCompat.setActionView(refreshItem, null);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SyncUtil.createSyncAccount(this);
        SyncUtil.setupAlarm(this);
        final ListView listView = (ListView) findViewById(android.R.id.list);
        this.adapter = new SimpleCursorAdapter(this,
                R.layout.item, null,
                new String[] { "title", "creator" },
                new int[] { android.R.id.text1, android.R.id.text2 },
                0);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(this);

        SyncUtil.syncIfNeeded(this);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        syncObserverHandle = ContentResolver.addStatusChangeListener(mask, syncStatusObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (syncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(syncObserverHandle);
            syncObserverHandle = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.optionsMenu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);

        syncStatusObserver.onStatusChanged(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.refresh) {
            SyncUtil.syncManual(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view,
                            final int position, final long id) {
        final Intent intent = new Intent(this, DetailActivity.class);
        intent.setData(URL_ITEM_BASE.buildUpon().appendPath(Long.toString(id)).build());
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final CursorLoader loader = new CursorLoader(MainActivity.this);
        loader.setUri(URL_ITEMS);
        loader.setProjection(new String[] { "id _id", "title", "creator" });
        loader.setSortOrder("pub_date_millis desc");
        return loader;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        this.adapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        this.adapter.changeCursor(null);
    }
}
