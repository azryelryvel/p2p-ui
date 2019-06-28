package net.azry.p2pd.daemon;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.magnet.MagnetUriParser;
import bt.runtime.BtClient;
import net.azry.p2pd.core.Config;
import net.azry.p2pd.core.Torrent;
import net.azry.p2pd.core.TorrentStats;
import net.azry.p2pd.core.TorrentStatus;
import com.google.inject.Module;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class TorrentManager extends Thread {
    private static long REFRESH_TIME = Long.parseLong(Config.properties.get("queue.refresh"));

    private static String STORAGE_BASE_DIR = Config.properties.get("storage.base_dir");

    public static TorrentManager instance = new TorrentManager();

    private List<Torrent> torrentList = new CopyOnWriteArrayList<>();

    public static void add(String id, String link) {
        Optional<String> displayName = MagnetUriParser.parser().parse(link).getDisplayName();
        Torrent torrent = displayName.map(s -> new Torrent(id + "/" + s, link)).orElseGet(() -> new Torrent(id, link));
        torrent.setStatus(TorrentStatus.QUEUED);
        instance.torrentList.add(torrent);
    }

    void load() {

    }

    // TODO: make sure that if this thread crashes, the whole thing crashes
    @Override
    public void run() {
        boolean stop = false;
        while (!stop) {
            for (Torrent torrent : instance.torrentList) {
                if (torrent.getStatus() == TorrentStatus.QUEUED) {
                    File storageDir = new File(STORAGE_BASE_DIR + "/" + torrent.getId());

                    if (storageDir.exists() || storageDir.mkdirs()) {
                        Storage storage = new FileSystemStorage(storageDir.toPath());
                        Module dhtModule = new DHTModule(new DHTConfig() {
                            @Override
                            public boolean shouldUseRouterBootstrap() {
                                return true;
                            }
                        });

                        BtClient client = Bt.client()
                                .magnet(torrent.getLink())
                                .storage(storage)
                                .autoLoadModules()
                                .module(dhtModule)
                                .stopWhenDownloaded()
                                .build();

                        client.startAsync(state -> {
                            TorrentStats stats = new TorrentStats(
                                    state.getPiecesTotal(),
                                    state.getPiecesComplete(),
                                    state.getPiecesIncomplete(),
                                    state.getPiecesRemaining(),
                                    state.getPiecesSkipped(),
                                    state.getPiecesNotSkipped(),
                                    state.getDownloaded(),
                                    state.getUploaded(),
                                    state.getConnectedPeers().size()
                            );
                            torrent.setStats(stats);

                            if (state.getPiecesRemaining() == 0) {
                                try {
                                    client.stop();
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }
                            }
                        }, REFRESH_TIME);
                        torrent.setStatus(TorrentStatus.STARTED);
                    }
                }
            }

            try {
                Thread.sleep(REFRESH_TIME);
            } catch (InterruptedException ignore) {
                stop = true;
            }
        }
    }

    public List<Torrent> getTorrentList() {
        return torrentList;
    }
}
