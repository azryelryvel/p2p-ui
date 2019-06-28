package net.azry.p2pd.ui;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.Theme;
import net.azry.p2pd.ui.grpc.BackendCache;

@HtmlImport("frontend://styles/ui-theme.html")
@Theme(Lumo.class)
public class MainUI extends UI {
    public MainUI() {
        new BackendCache().start();
    }
}
