package me.chrommob.minestore.data;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Config {
    @Getter
    @Setter
    private static String password;

    @Getter
    @Setter
    private static int port;

    @Getter
    @Setter
    private static boolean guiEnabled;

    @Getter
    @Setter
    private static boolean storeEnabled;

    @Getter
    @Setter
    private static String storeMessage;

    @Getter
    @Setter
    private static ArrayList<String> apiUrl = new ArrayList<>();

    @Getter
    @Setter
    private static ArrayList<String> secretKey = new ArrayList<>();

    @Getter
    @Setter
    private static String guiName;

    @Getter
    @Setter
    private static String packageMessage;

    @Getter
    @Setter
    private static String itemName;

    @Getter
    @Setter
    private static String itemDescription;

    @Getter
    @Setter
    private static String itemPrice;

    @Setter
    @Getter
    private static ArrayList<String> apiKey = new ArrayList<>();

    @Setter
    @Getter
    private static boolean placeholderPresent;

    @Setter
    @Getter
    private static boolean vaultPresent;

    @Setter
    @Getter
    private static ArrayList<Boolean> empty = new ArrayList<>();

    @Setter
    @Getter
    private static String authMessage;

    @Getter
    @Setter
    private static double authDelay;

    @Setter
    @Getter
    private static String authSuccessful;

    @Setter
    @Getter
    private static String authFailed;
    private static boolean debug = false;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Config.debug = debug;
    }
}