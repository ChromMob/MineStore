package me.chrommob.minestore.gui;

import lombok.Getter;
import me.chrommob.minestore.data.Config;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Currency {
    @Getter
    private static String currency;

    public static void run() {
        HttpsURLConnection urlConnection;
        if (!(Config.getApiUrl().get(0).equalsIgnoreCase("hard_api_key_here") || Config.getApiUrl().get(0).equalsIgnoreCase(""))) {
            try {
                String link = Config.getApiUrl().get(0) + Config.getApiKey().get(0) + "/getMainCurrency";
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                StringBuilder stringBuilder = new StringBuilder();
                int data = inputStreamReader.read();
                while (data != -1) {
                    stringBuilder.append((char) data);
                    data = inputStreamReader.read();
                }
                currency = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String link = Config.getApiUrl() + "getMainCurrency";
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                StringBuilder stringBuilder = new StringBuilder();
                int data = inputStreamReader.read();
                while (data != -1) {
                    stringBuilder.append((char) data);
                    data = inputStreamReader.read();
                }
                currency = stringBuilder.toString();
            } catch (Exception e) {
                if (e instanceof ClassCastException){
                    Bukkit.getLogger().info("Please use HTTPS instead of HTTP.");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }
}
