package net.azry.p2pd.ui.grpc;

import net.azry.p2pd.core.Torrent;
import java.util.List;

public class BackendCache extends Thread {
    private static List<Torrent> torrentListCache;

    // TODO: enable views to know when the backend is down so they can warn their users
    @Override
    public void run() {
        boolean stop = false;
        BackendService service = new BackendService();

        while (!stop) {
            torrentListCache = service.list();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                stop = true;
            }
        }

        service.stop();
    }

    public static List<Torrent> getTorrentListCache() {
        return torrentListCache;
    }
}
