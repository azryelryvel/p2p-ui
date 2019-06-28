package net.azry.p2pd.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import net.azry.p2pd.ui.User;
import net.azry.p2pd.ui.grpc.BackendService;

import java.io.InputStream;

public class AddTorrentModal extends Dialog {
	public AddTorrentModal() {
		H1 title = new H1("Add");

		FileBuffer fileBuffer = new FileBuffer();
		Upload upload = new Upload(fileBuffer);
		upload.addFinishedListener(e -> {
			InputStream inputStream = fileBuffer.getInputStream();
		});

		Checkbox shareCheckbox = new Checkbox();
		shareCheckbox.setLabel("Share download");
		shareCheckbox.setEnabled(true);


		TextField magnetField = new TextField();
		magnetField.setLabel("Magnet URI");
		magnetField.setPlaceholder("magnet:?xt=urn:...");
		magnetField.setValue("magnet:?xt=urn:btih:AE4X56LTTQH2MAHPFC37JZKWJ3T3EU4I&dn=debian-9.9.0-amd64-netinst.iso&xl=306184192&tr=http%3A%2F%2Fbttracker.debian.org%3A6969%2Fannounce");

		User user = User.getCurrent();

		Button addButton = new Button("Add", event -> {
			String path;
			if (shareCheckbox.isEnabled()) {
				path = user.getUuid();
			} else {
				path = "shared";
			}
			new BackendService().add(path, magnetField.getValue());
		});

		add(title, shareCheckbox, upload, magnetField, addButton);
	}
}
