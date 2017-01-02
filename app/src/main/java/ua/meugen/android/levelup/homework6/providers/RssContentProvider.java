package ua.meugen.android.levelup.homework6.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ua.meugen.android.levelup.homework6.helpers.RssContentOpenHelper;

/**
 * @author meugen
 */

public class RssContentProvider extends ContentProvider {

    private static final String TABLE = "rss_items";

    private RssContentOpenHelper openHelper;

    @Override
    public boolean onCreate() {
        this.openHelper = new RssContentOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull final Uri uri, final String[] columns, final String selection,
                        final String[] selectionArgs, final String sortOrder) {
        SQLiteDatabase database = null;
        try {
            database = this.openHelper.getReadableDatabase();
            return database.query(TABLE, columns, selection, selectionArgs, null, null, sortOrder);
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        SQLiteDatabase database = null;
        try {
            database = this.openHelper.getWritableDatabase();
            database.insertOrThrow(TABLE, null, values);
            return null;
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    @Override
    public int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        SQLiteDatabase database = null;
        try {
            database = this.openHelper.getWritableDatabase();
            return database.update(TABLE, values, selection, selectionArgs);
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }
}
