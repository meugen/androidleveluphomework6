package ua.meugen.android.levelup.homework6.data;

/**
 * @author meugen
 */

public final class Entry {

    private String title;
    private String link;
    private String description;
    private String creator;
    private String pubDate;
    private String guid;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(final String creator) {
        this.creator = creator;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(final String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "Entry{\n" +
                "  title='" + cut(title) + "\'\n" +
                "  link='" + cut(link) + "\'\n" +
                "  description='" + cut(description) + "\'\n" +
                "  creator='" + cut(creator) + "\'\n" +
                "  pubDate='" + cut(pubDate) + "\'\n" +
                "  guid='" + cut(guid) + "\'\n" +
                '}';
    }

    private String cut(final String original) {
        if (original == null || original.length() < 100) {
            return original;
        }
        return original.substring(0, 100) + "...";
    }
}
