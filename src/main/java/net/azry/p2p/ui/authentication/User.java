package net.azry.p2p.ui.authentication;

import net.azry.p2p.ui.Config;
import net.azry.p2p.ui.database.DatabaseConnectionFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;

import java.sql.*;

import java.math.BigInteger;
import java.security.SecureRandom;

public class User {
    private String username;

    private String hashedPassword;

    private String salt;

    private String roles;

    public User(String username) {
        this.username = username;
    }

    public boolean login(String password){
        boolean isAuthenticated = false;

        try {
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            String[] creds = generateSaltedHashedPassword(password);

            currentUser.login(token);
            isAuthenticated = currentUser.isAuthenticated();
        } catch (IllegalStateException e) {
            System.out.println("illegal");
        } catch (UnknownAccountException uae ) {
            try {
                this.register(password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IncorrectCredentialsException ice ) {
            System.out.println("incorrect");
        } catch (LockedAccountException lae ) {
            System.out.println("locked");
        } catch(AuthenticationException aex){
            System.out.println("auth");
        }
        return isAuthenticated;
    }

    public void register(String password) throws SQLException {
        String[] creds = generateSaltedHashedPassword(password);

        Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
        String query = "INSERT INTO users (username, password, password_salt) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, creds[0]);
        statement.setString(3, creds[1]);
        statement.execute();
    }

    private static String[] generateSaltedHashedPassword(String password) {
        String privateSalt = Config.properties.get("authentication.private_salt");
        String algorithm = Config.properties.get("authentication.hash.algorithm");
        int iterations = Integer.parseInt(Config.properties.get("authentication.hash.iterations"));
        String publicSalt = new BigInteger(250, new SecureRandom()).toString(32);

        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashIterations(iterations);
        hashService.setHashAlgorithmName(algorithm);
        hashService.setPrivateSalt(new SimpleByteSource(privateSalt));

        HashRequest request = new HashRequest.Builder().setSalt(new SimpleByteSource(publicSalt)).setSource(new SimpleByteSource(password)).build();

        Hash hash = hashService.computeHash(request);

        System.out.println("Hash: " + hash.toHex());
        System.out.println("public salt: " + publicSalt);
        System.out.println("private salt: " + privateSalt);

        String[] result = new String[2];
        result[0] = hash.toHex();
        result[1] = publicSalt;
        return result;
    }
}
