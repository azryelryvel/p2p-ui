package net.azry.p2p.ui.authentication;

import net.azry.p2p.ui.Config;
import net.azry.p2p.ui.database.DatabaseConnectionFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
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
            UsernamePasswordToken token = new UsernamePasswordToken(username, generateSaltedHashedPassword(password));
            currentUser.login(token);
            isAuthenticated = currentUser.isAuthenticated();
        } catch (IllegalStateException e) {
            System.out.println("illegal");
        } catch (UnknownAccountException uae ) {
            System.out.println("unknownaccount");
        } catch (IncorrectCredentialsException ice ) {
            System.out.println("incorrect");
        } catch (LockedAccountException lae ) {
            System.out.println("locked");
        } catch(AuthenticationException aex){
            System.out.println("auth");
        }
        return isAuthenticated;
    }

    public void register(String password) throws SQLException, ClassNotFoundException {
        String newHashedPassword = generateSaltedHashedPassword(password);

        Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
        String query = "INSERT INTO users (username, password, password_salt) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setString(3, salt);
        statement.execute();
    }

    public static void authorized(){
        if(null != SecurityUtils.getSubject().getPrincipal()){
        }
    }

    private static String generateSaltedHashedPassword(String password) {
        String privateSalt = Config.properties.get("authentication.private_salt");
        int iterations = Integer.parseInt(Config.properties.get("authentication.hash_iterations"));
        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashIterations(iterations);
        hashService.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashService.setPrivateSalt(new SimpleByteSource(privateSalt));
        hashService.setGeneratePublicSalt(true);
        DefaultPasswordService passwordService = new DefaultPasswordService();
        passwordService.setHashService(hashService);
        return passwordService.encryptPassword(password);

    }
}
