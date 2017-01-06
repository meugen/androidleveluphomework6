package ua.meugen.android.levelup.homework6;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.SyncRequest;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String AUTHORIZATION = "ua.meugen.android.levelup.homework6";
    private static final String ACCOUNT_TYPE = "rsscontent.com";
    private static final String ACCOUNT = "meugen";

    private final static Uri URL_ITEMS
            = Uri.parse("content://ua.meugen.android.levelup.homework6/items");

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.account = createSyncAccount();
        final ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new SimpleCursorAdapter(this,
                R.layout.item,
                getContentResolver().query(URL_ITEMS, new String[] { "id _id", "title" }, null, null, null),
                new String[] { "title" },
                new int[] { android.R.id.text1 },
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
    }

    private Account createSyncAccount() {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        final AccountManager manager = (AccountManager) this
                .getSystemService(ACCOUNT_SERVICE);
        boolean res = manager.addAccountExplicitly(newAccount, null, null);
        Log.i(TAG, "" + res);
        if (res) {
            ContentResolver.setIsSyncable(newAccount, AUTHORIZATION, 1);
            ContentResolver.setSyncAutomatically(newAccount, AUTHORIZATION, true);
        }
        return newAccount;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        getContentResolver().requestSync(account, AUTHORIZATION, new Bundle());
    }
}
