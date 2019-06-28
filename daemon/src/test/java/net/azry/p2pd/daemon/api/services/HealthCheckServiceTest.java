package net.azry.p2pd.daemon.api.services;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.azry.p2pd.daemon.api.BackgroundServerExtension;
import net.azry.p2pd.protos.Empty;
import net.azry.p2pd.protos.Health;
import net.azry.p2pd.protos.BackendGrpc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BackgroundServerExtension.class})
class HealthCheckServiceTest {
    @Test
    void check() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051).usePlaintext().build();
        BackendGrpc.BackendBlockingStub stub = BackendGrpc.newBlockingStub(channel);
        Health health = stub.check(Empty.newBuilder().build());
        assert health.getAlive();
    }
}