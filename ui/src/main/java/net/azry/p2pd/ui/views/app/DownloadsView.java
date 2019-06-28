package net.azry.p2pd.ui.views.app;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import net.azry.p2pd.core.Torrent;
import net.azry.p2pd.ui.components.AddTorrentModal;
import net.azry.p2pd.ui.components.SidebarRouterLayout;
import net.azry.p2pd.ui.grpc.BackendCache;

@Route(value = "app/downloads", layout = SidebarRouterLayout.class)
public class DownloadsView extends VerticalLayout {
    private FeederThread thread;
    private Grid<Torrent> torrentGrid = new Grid<>(Torrent.class);

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        torrentGrid.setColumns();
        torrentGrid.addColumn(new ComponentRenderer<>(torrent -> {
            Anchor link = new Anchor();
            link.setHref(torrent.getLink());
            link.setText(torrent.getId());
            return link;
        })).setHeader("Name");
        torrentGrid.addColumn(new TextRenderer<>(torrent -> String.valueOf(torrent.getStats().getPeers()))).setHeader("Peers").setWidth("20px");
        torrentGrid.addColumn(new ComponentRenderer<>(torrent -> {
            double progress = 0;
            if (torrent.getStats().getPiecesTotal() != 0) {
                progress = (double) torrent.getStats().getPiecesComplete() / torrent.getStats().getPiecesTotal();
            }
            return new ProgressBar(0, 1, progress);
        }
        )).setHeader("Progress");

        thread = new FeederThread(attachEvent.getUI(), this);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        thread.interrupt();
        thread = null;
    }

    private static class FeederThread extends Thread {
        private final UI ui;
        private final DownloadsView view;

        FeederThread(UI ui, DownloadsView view) {
            this.ui = ui;
            this.view = view;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    ui.access(() -> {
                        view.torrentGrid.setItems(BackendCache.getTorrentListCache());
                        view.torrentGrid.getDataProvider().refreshAll();
                    });
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ignore) {
            }
        }
    }


    public DownloadsView() {
//        if (SecurityUtils.getSubject().isPermitted("users:add")) {
        H1 title = new H1("Downloads");
        AddTorrentModal modal = new AddTorrentModal();
        Button addTorrentButton = new Button("Add torrent", event -> modal.open());
        add(title, torrentGrid, addTorrentButton);
//        }
    }

}
