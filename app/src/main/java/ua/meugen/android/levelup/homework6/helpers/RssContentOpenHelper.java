package ua.meugen.android.levelup.homework6.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author meugen
 */

public final class RssContentOpenHelper extends SQLiteOpenHelper {

    private static final String NAME = "rsscontent";
    private static final int VERSION = 2;

    private static final Pattern PATTERN = Pattern.compile("\\s*([^;]+);");

    private final Context context;

    public RssContentOpenHelper(final Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase database) {
        onUpgrade(database, 0, VERSION);
    }

    @Override
    public void onUpgrade(
            final SQLiteDatabase database,
            final int oldVersion,
            final int newVersion) {
        final AssetManager assets = this.context.getAssets();
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            upgrade(database, assets, version);
        }
    }

    private void upgrade(
            final SQLiteDatabase database,
            final AssetManager assets,
            final int version) {
        final CharSequence sqls = fetchSqls(assets, version);

        final Matcher matcher = PATTERN.matcher(sqls);
        while (matcher.find()) {
            database.execSQL(matcher.group(1));
        }
    }

    private CharSequence fetchSqls(
            final AssetManager assets, final int version) {
        final StringBuilder builder = new StringBuilder();
        try {
            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(assets.open(
                        "db/" + NAME + "/" + version + ".sql")));

                char[] buf = new char[1024];
                while (true) {
                    final int count = reader.read(buf);
                    if (count < 0) {
                        break;
                    }
                    builder.append(buf, 0, count);
                }

            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder;
    }

}
