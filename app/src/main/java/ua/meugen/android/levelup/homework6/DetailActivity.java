package ua.meugen.android.levelup.homework6;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final CursorLoader loader = new CursorLoader(this);
        loader.setUri(getIntent().getData());
        return loader;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        if (cursor.moveToFirst()) {
            setTitle(cursor.getString(0));
            final WebView webView = (WebView) findViewById(R.id.web);
            webView.loadData(cursor.getString(1), "text/html", "UTF-8");
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }
}
