package ua.meugen.android.levelup.homework6.helpers;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.meugen.android.levelup.homework6.data.Entry;

/**
 * @author meugen
 */

public final class DouFeedFetchHelper {
    
    private static final String CHANNEL_TAG = "channel";
    private static final String ITEM_TAG = "item";
    
    private static final String TITLE_TAG = "title";
    private static final String LINK_TAG = "link";
    private static final String DESCRIPTION_TAG = "description";
    private static final String CREATOR_TAG = "dc:creator";
    private static final String PUB_DATE_TAG = "pubDate";
    private static final String GUID_TAG = "guid";

    private final String urlString;

    public DouFeedFetchHelper(final String urlString) {
        this.urlString = urlString;
    }

    public List<Entry> fetch() throws IOException, XmlPullParserException {
        final URL url = new URL(this.urlString);

        InputStream input = null;
        try {
            input = url.openStream();
            return parse(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    private List<Entry> parse(final InputStream input) throws XmlPullParserException, IOException {
        final XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(input, null);
        parser.nextTag();
        return readFeed(parser);
    }

    private List<Entry> readFeed(final XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<>();

        while (true) {
            final int type = parser.next();
            final String name = parser.getName();
            if (type == XmlPullParser.START_TAG && CHANNEL_TAG.equals(name)) {
                break;
            }
        }
        parser.require(XmlPullParser.START_TAG, null, CHANNEL_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            final String name = parser.getName();
            if (ITEM_TAG.equals(name)) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, ITEM_TAG);

        String title = null;
        String link = null;
        String description = null;
        String creator = null;
        String pubDate = null;
        String guid = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            final String name = parser.getName();
            if (TITLE_TAG.equals(name)) {
                title = readTitle(parser);
            } else if (LINK_TAG.equals(name)) {
                link = readLink(parser);
            } else if (DESCRIPTION_TAG.equals(name)) {
                description = readDescription(parser);
            } else if (CREATOR_TAG.equals(name)) {
                creator = readCreator(parser);
            } else if (PUB_DATE_TAG.equals(name)) {
                pubDate = readPubDate(parser);
            } else if (GUID_TAG.equals(name)) {
                guid = readGuid(parser);
            } else {
                skip(parser);
            }
        }

        final Entry entry = new Entry();
        entry.setTitle(title);
        entry.setLink(link);
        entry.setDescription(description);
        entry.setCreator(creator);
        entry.setPubDate(pubDate);
        entry.setGuid(guid);
        return entry;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TITLE_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TITLE_TAG);
        return title;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, LINK_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, LINK_TAG);
        return title;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, DESCRIPTION_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, DESCRIPTION_TAG);
        return title;
    }

    private String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, CREATOR_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, CREATOR_TAG);
        return title;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, PUB_DATE_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, PUB_DATE_TAG);
        return title;
    }

    private String readGuid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, GUID_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, GUID_TAG);
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
