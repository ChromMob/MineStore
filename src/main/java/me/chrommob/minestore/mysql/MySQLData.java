package me.chrommob.minestore.mysql;

import lombok.Getter;
import lombok.Setter;
import me.chrommob.minestore.util.ModeDatabase;

public class MySQLData {
    @Getter
    @Setter
    private static boolean enabled;

    @Getter
    @Setter
    private static String ip;

    @Getter
    @Setter
    private static int port;

    @Getter
    @Setter
    private static String database;

    @Getter
    @Setter
    private static String user;

    @Getter
    @Setter
    private static String password;

    @Getter
    @Setter
    private static ModeDatabase method;

}
