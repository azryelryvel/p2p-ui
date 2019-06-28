package net.azry.p2pd.core;

import java.io.Serializable;

public final class TorrentStats implements Serializable {
    private final long startTime;
    private final int piecesTotal;
    private final int piecesComplete;
    private final int piecesIncomplete;
    private final int piecesRemaining;
    private final int piecesSkipped;
    private final int piecesNotSkipped;
    private final long downloaded;
    private final long uploaded;
    private final int peers;

    public TorrentStats(int piecesTotal, int piecesComplete, int piecesIncomplete, int piecesRemaining, int piecesSkipped, int piecesNotSkipped, long downloaded, long uploaded, int peers) {
        this(System.currentTimeMillis(), piecesTotal, piecesComplete, piecesIncomplete, piecesRemaining, piecesSkipped, piecesNotSkipped, downloaded, uploaded, peers);
    }

    public TorrentStats(long startTime, int piecesTotal, int piecesComplete, int piecesIncomplete, int piecesRemaining, int piecesSkipped, int piecesNotSkipped, long downloaded, long uploaded, int peers) {
        this.startTime = startTime;
        this.piecesTotal = piecesTotal;
        this.piecesComplete = piecesComplete;
        this.piecesIncomplete = piecesIncomplete;
        this.piecesRemaining = piecesRemaining;
        this.piecesSkipped = piecesSkipped;
        this.piecesNotSkipped = piecesNotSkipped;
        this.downloaded = downloaded;
        this.uploaded = uploaded;
        this.peers = peers;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getPiecesTotal() {
        return piecesTotal;
    }

    public int getPiecesComplete() {
        return piecesComplete;
    }

    public int getPiecesIncomplete() {
        return piecesIncomplete;
    }

    public int getPiecesRemaining() {
        return piecesRemaining;
    }

    public int getPiecesSkipped() {
        return piecesSkipped;
    }

    public int getPiecesNotSkipped() {
        return piecesNotSkipped;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public long getUploaded() {
        return uploaded;
    }

    public int getPeers() {
        return peers;
    }
}
