package net.azry.p2p;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class DownloadManagerServerMock {
    private static final Logger logger = Logger.getLogger(DownloadManagerServerMock.class.getName());

    private static final int port = 50051;

    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new DownloadManagerImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                DownloadManagerServerMock.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final DownloadManagerServerMock server = new DownloadManagerServerMock();
        server.start();
        server.blockUntilShutdown();
    }

    static class DownloadManagerImpl extends DownloadManagerGrpc.DownloadManagerImplBase {
        @Override
        public void addDownload(DownloadInfo req, StreamObserver<DownloadInfo> responseObserver) {
            DownloadInfo reply = DownloadInfo.newBuilder().setUrl("Hello " + req.getUrl()).setSuccess(true).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
