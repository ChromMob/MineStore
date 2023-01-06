package me.chrommob.minestore.mysql.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.mysql.MySQLData;
import me.chrommob.minestore.mysql.data.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class ConnectionPool {
    private static MineStore plugin;

    private static HikariDataSource hikari;

    private String hostname;
    private String port;
    public static String database;
    private String username;
    private String password;

    private int minimumConnections;
    private int maximumConnections;
    private long connectionTimeout;

    public ConnectionPool(MineStore plugin) {
        ConnectionPool.plugin = plugin;
        init();
        setupPool();
    }

    private void init() {
        FileConfiguration fileConfig = plugin.getConfig();

        this.username = MySQLData.getUser();
        this.password = MySQLData.getPassword();
        ConnectionPool.database = MySQLData.getDatabase();
        this.hostname = MySQLData.getIp();
        this.port = String.valueOf(MySQLData.getPort());

        this.minimumConnections = 5;
        this.maximumConnections = 100;
        this.connectionTimeout = 2000;
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mariadb://" +
                        hostname +
                        ":" +
                        port +
                        "/" +
                        database +
                        "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minimumConnections);
        config.setMaximumPoolSize(maximumConnections);
        config.setConnectionTimeout(connectionTimeout);
        config.setLeakDetectionThreshold(60000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        // config.setConnectionTestQuery(testQuery);
        hikari = new HikariDataSource(config);

        MineStore.instance.getLogger().info(hikari.isRunning() ? "Database connection established." : "Database connection failed.");
    }

    public static void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        if (ps != null)
            try {
                ps.close();
            } catch (SQLException ignored) {
            }
        if (res != null)
            try {
                res.close();
            } catch (SQLException ignored) {
            }
    }

    public static void closePool() {
        if (hikari != null && !hikari.isClosed()) {
            hikari.close();
        }
    }

    public static void createTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = hikari.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata"
                    + "  (uuid           VARCHAR(255) UNIQUE,"
                    + "   username       VARCHAR(255) NOT NULL default '',"
                    + "   prefix         VARCHAR(255) NOT NULL default '',"
                    + "   suffix         VARCHAR(255) NOT NULL default '',"
                    + "   balance             DOUBLE NOT NULL default 0.00,"
                    + "   player_group          VARCHAR(255) NOT NULL default 0,"
                    + "   PRIMARY KEY  (uuid));");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, null);
        }
    }

    public static void updateTable() {
        try {
            Map<UUID, User> userMap = MineStore.instance.getUserManager().getAll();
            userMap.forEach((uuid, user) -> {
                update(user);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void update(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = hikari.getConnection();
            String sql_query = "INSERT INTO playerdata (uuid,username,prefix,suffix,balance,player_group) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE username=?,prefix=?,suffix=?,balance=?,player_group=?";
            ps = conn.prepareStatement(sql_query);
            String uuid = user.getUuid().toString();
            String username = user.getName();
            String prefix;
            String suffix;
            String group;
            if (user.getPrefix() == null) {
                prefix = "";
            } else {
                prefix = user.getPrefix();
            }
            if (user.getSuffix() == null) {
                suffix = "";
            } else {
                suffix = user.getSuffix();
            }
            if (user.getGroup_name() == null) {
                group = "";
            } else {
                group = user.getGroup_name();
            }
            ps.setString(1, uuid);
            ps.setString(2, username);
            ps.setString(3, prefix);
            ps.setString(4, suffix);
            ps.setDouble(5, user.getBalance());
            ps.setString(6, group);
            ps.setString(7, username);
            ps.setString(8, prefix);
            ps.setString(9, suffix);
            ps.setDouble(10, user.getBalance());
            ps.setString(11, group);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, null);
        }
    }
}
