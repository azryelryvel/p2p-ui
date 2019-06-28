package net.azry.p2pd.daemon.api;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class BackgroundServerExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    private static boolean started = false;
    private ApiServer server = new ApiServer();

    @Override
    public void beforeAll(ExtensionContext context) throws IOException {
        if (!started) {
            started = true;
            context.getRoot().getStore(GLOBAL).put("any unique name", this);
            server.start();
        }
    }

    @Override
    public void close() {
        server.stop();
    }
}