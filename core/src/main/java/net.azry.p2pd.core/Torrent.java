package net.azry.p2pd.core;

import java.io.Serializable;

public class Torrent implements Serializable {
    private String id;
    private String link;
    private TorrentStatus status;
    private TorrentStats stats;

    public Torrent(String id, String link) {
        this.id = id;
        this.link = link;
        this.stats = new TorrentStats(0,0,0,0,0,0,0,0,0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public TorrentStatus getStatus() {
        return status;
    }

    public void setStatus(TorrentStatus status) {
        this.status = status;
    }

    public TorrentStats getStats() {
        return stats;
    }

    public void setStats(TorrentStats stats) {
        this.stats = stats;
    }
}
