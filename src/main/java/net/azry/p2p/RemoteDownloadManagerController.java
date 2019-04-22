package net.azry.p2p;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteDownloadManagerController {
    private static final Logger logger = Logger.getLogger(RemoteDownloadManagerController.class.getName());
    private final ManagedChannel channel;
    private final DownloadManagerGrpc.DownloadManagerBlockingStub blockingStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public RemoteDownloadManagerController(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    RemoteDownloadManagerController(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = DownloadManagerGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void addDownload(String url) {
        logger.info("adding " + url + " ...");
        DownloadInfo request = DownloadInfo.newBuilder().setUrl(url).build();
        DownloadInfo response;
        try {
            response = blockingStub.addDownload(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Got : " + response.getActive());
    }

    public static void main(String[] args) throws Exception {
        RemoteDownloadManagerController client = new RemoteDownloadManagerController("localhost", 50051);
        try {
            client.addDownload("magnet:testURL");
        } finally {
            client.shutdown();
        }
    }
}
