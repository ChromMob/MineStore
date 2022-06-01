package me.chrommob.minestore.data;


import lombok.Getter;
import lombok.Setter;

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
    private static String apiUrl;

    @Getter
    @Setter
    private static String secretKey;

    @Getter
    @Setter
    private static String guiName;

    @Getter
    @Setter
    private static String packageMessage;

    @Getter
    @Setter
    private static String buyUrl;

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
    private static String apiKey;

    @Setter
    @Getter
    private static boolean placeholderPresent;

    @Setter
    @Getter
    private static boolean vaultPresent;

    @Setter
    @Getter
    private static boolean empty;

    @Setter
    @Getter
    private static int version;
}