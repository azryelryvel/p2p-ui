package net.azry.p2pd.ui.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.azry.p2pd.core.Config;

import java.util.concurrent.TimeUnit;

class GrpcService {
    private static final String host = Config.properties.get("backend.host");
    private static final int port = Integer.parseInt(Config.properties.get("backend.port"));
    ManagedChannel channel = generateChannel();

    private static ManagedChannel generateChannel() {
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

    public void stop() {
        channel.shutdown();
        try {
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {
        }
    }


}
