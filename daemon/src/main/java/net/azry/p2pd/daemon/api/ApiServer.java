package net.azry.p2pd.daemon.api;

import io.grpc.ServerBuilder;
import net.azry.p2pd.daemon.api.services.BackendService;

import java.io.IOException;
import java.util.logging.Logger;

public class ApiServer {
    private static final Logger logger = Logger.getLogger(ApiServer.class.getName());

    private io.grpc.Server server;

    public void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new BackendService())
                .build()
                .start();

        logger.info("ApiServer started, listening on " + port);
    }

    public void stop() {
        logger.info("Stopping API server");
        if (server != null) {
            server.shutdown();
        }
    }

    public void block() throws InterruptedException {
        server.awaitTermination();
    }
}
