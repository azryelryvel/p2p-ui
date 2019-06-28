package net.azry.p2pd.daemon;

import net.azry.p2pd.daemon.api.ApiServer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        ApiServer server = new ApiServer();
        server.start();
        new TorrentManager().start();
        server.block();
    }
}
