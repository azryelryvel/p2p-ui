package net.azry.p2p.ui.authentication;

import org.apache.shiro.realm.jdbc.JdbcRealm;

public class Realm extends JdbcRealm {
    public Realm() {
        setSaltStyle(SaltStyle.COLUMN);
        setAuthenticationQuery(DEFAULT_SALTED_AUTHENTICATION_QUERY);
    }
}
