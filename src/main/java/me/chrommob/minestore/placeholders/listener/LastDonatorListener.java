package me.chrommob.minestore.placeholders.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.PlaceHolderData;
import me.chrommob.minestore.placeholders.objects.LastDonator;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class LastDonatorListener {
    private static HttpsURLConnection urlConnection;
    @SneakyThrows
    public static void run() {
        if (!(Config.getApiUrl().equalsIgnoreCase("hard_api_key_here") || Config.getApiUrl().equalsIgnoreCase(""))) {
            try {
                String link = Config.getApiUrl() + Config.getApiKey() + "/getTotalPayments";
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<LastDonator>>() {}.getType();
                PlaceHolderData.setLastDonatorsObjects(gson.fromJson(inputStreamReader, listType));
                PlaceHolderData.createLastMap();
            }
                catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String link = Config.getApiUrl() + "getTotalPayments";
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<LastDonator>>() {}.getType();
                PlaceHolderData.setLastDonatorsObjects(gson.fromJson(inputStreamReader, listType));
                PlaceHolderData.createLastMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
