package net.azry.p2pd.daemon.api.services;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import net.azry.p2pd.daemon.TorrentManager;
import net.azry.p2pd.protos.*;

public class BackendService extends BackendGrpc.BackendImplBase {
    @Override
    public void check(Empty req, StreamObserver<Health> responseObserver) {
        Health reply = Health.newBuilder().setAlive(true).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    public void add(MagnetLink magnet, StreamObserver<Ack> response) {
        String id = magnet.getId();
        String link = magnet.getLink();
        TorrentManager.add(id, link);
        Ack ack = Ack.newBuilder().setSuccess(true).build();
        response.onNext(ack);
        response.onCompleted();
    }

    // TODO: Maybe cache this request
    public void list(Empty req, StreamObserver<JsonPayload> responseObserver) {
        String json = "[]";
        if (TorrentManager.instance.getTorrentList().size() > 0) {
            json = new Gson().toJson(TorrentManager.instance.getTorrentList());
        }
        JsonPayload jsonPayload = JsonPayload.newBuilder().setValue(json).build();
        responseObserver.onNext(jsonPayload);
        responseObserver.onCompleted();
    }
}
