package net.azry.p2p.ui.shiro.authz;

public enum Roles {
	USER("user"),
	ADMIN("admin");

	public String label;

	Roles(String label) {
		this.label = label;
	}
}
