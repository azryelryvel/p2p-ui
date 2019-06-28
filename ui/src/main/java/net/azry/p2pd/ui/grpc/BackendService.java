package net.azry.p2pd.ui.grpc;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.grpc.StatusRuntimeException;
import net.azry.p2pd.core.Torrent;
import net.azry.p2pd.protos.*;

import java.util.ArrayList;
import java.util.List;

public class BackendService extends GrpcService {
    private BackendGrpc.BackendBlockingStub stub = BackendGrpc.newBlockingStub(channel);

    public boolean check() {
        Health health;
        try {
            health = stub.check(Empty.newBuilder().build());
        } catch (StatusRuntimeException e) {
            return false;
        }
        return health.getAlive();
    }

    public boolean add(String id, String link) {
        MagnetLink magnetLink = MagnetLink.newBuilder().setLink(link).setId(id).build();
        Ack ack = stub.add(magnetLink);
        if (ack.getSuccess()) {
            return true;
        } else {
            System.out.println(ack.getError());
            return false;
        }
    }

    public List<Torrent> list() {
        JsonPayload json = null;
        try {
            json = stub.list(Empty.newBuilder().build());
        } catch (StatusRuntimeException ignore) {}
        if (json == null) {
            return new ArrayList<>();
        } else {
            return new Gson().fromJson(json.getValue(), new TypeToken<List<Torrent>>(){}.getType());
        }
    }
}
