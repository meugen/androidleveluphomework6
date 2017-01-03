package ua.meugen.android.levelup.homework6;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String AUTHORIZATION = "ua.meugen.android.levelup.homework6";
    private static final String ACCOUNT_TYPE = "rsscontent.com";
    private static final String ACCOUNT = "meugen";

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.account = createSyncAccount();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getContentResolver().requestSync(account, AUTHORIZATION, new Bundle());
            }
        });
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
}
