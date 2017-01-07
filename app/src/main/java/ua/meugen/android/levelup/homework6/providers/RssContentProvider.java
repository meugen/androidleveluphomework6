package ua.meugen.android.levelup.homework6.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.regex.Pattern;

import ua.meugen.android.levelup.homework6.helpers.RssContentOpenHelper;

/**
 * @author meugen
 */

public class RssContentProvider extends ContentProvider implements RssContent {

    private static final String TABLE = "rss_items";

    private static final int ITEMS = 1;
    private static final int ITEM_ID = 2;
    private static final int GUIDS = 3;

    private static final UriMatcher MATCHER;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(AUTHORITY, "items", ITEMS);
        MATCHER.addURI(AUTHORITY, "item/#", ITEM_ID);
        MATCHER.addURI(AUTHORITY, "guids", GUIDS);
    }

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
        final int which = MATCHER.match(uri);
        if (which == GUIDS) {
            return getGuids();
        } else if (which == ITEM_ID) {
            return getItem(uri);
        }
        final SQLiteDatabase database = this.openHelper.getReadableDatabase();
        final Cursor cursor = database.query(TABLE, columns, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getGuids() {
        final SQLiteDatabase database = this.openHelper.getReadableDatabase();
        return database.rawQuery("SELECT DISTINCT guid FROM " + TABLE, null);
    }

    private Cursor getItem(final Uri uri) {
        final String id = uri.getPathSegments().get(1);
        final SQLiteDatabase database = this.openHelper.getReadableDatabase();
        return database.rawQuery("SELECT title, description FROM " + TABLE + " WHERE id=?",
                new String[] { id });
    }

    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        final SQLiteDatabase database = this.openHelper.getWritableDatabase();
        final long id = database.insertOrThrow(TABLE, null, values);
        final Uri result = URL_ITEM_BASE.buildUpon().appendPath(Long.toString(id)).build();

        final ContentResolver resolver = getContext().getContentResolver();
        resolver.notifyChange(URL_ITEMS, null, false);
        return result;
    }

    @Override
    public int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        final SQLiteDatabase database = this.openHelper.getWritableDatabase();
        final int result = database.update(TABLE, values, selection, selectionArgs);

        final ContentResolver resolver = getContext().getContentResolver();
        resolver.notifyChange(URL_ITEMS, null, false);
        return result;
    }
}
