package ua.meugen.android.levelup.homework6.providers;

import android.net.Uri;

/**
 * Created by meugen on 06.01.17.
 */

public interface RssContent {

    String SCHEME = "content";

    String AUTHORITY = "ua.meugen.android.levelup.homework6";

    Uri URL_ITEMS = new Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("items")
            .build();

    Uri URL_GUIDS = new Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("guids")
            .build();

    Uri URL_ITEM_BASE = new Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("item")
            .build();
}
