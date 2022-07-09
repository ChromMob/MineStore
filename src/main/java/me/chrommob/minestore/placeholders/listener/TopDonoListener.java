package me.chrommob.minestore.placeholders.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.PlaceHolderData;
import me.chrommob.minestore.placeholders.objects.TopDonoObjects;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class TopDonoListener {
    private static HttpsURLConnection urlConnection;
    @SneakyThrows
    public static void run() {
        if (!(Config.getApiUrl().equalsIgnoreCase("hard_api_key_here") || Config.getApiUrl().equalsIgnoreCase(""))) {
            String link = Config.getApiUrl() + Config.getApiKey() + "/top_donators";
            try {
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<TopDonoObjects>>() {}.getType();
                PlaceHolderData.setTopDonoObjects(gson.fromJson(inputStreamReader, listType));
                PlaceHolderData.createTopMap();
            } catch (Exception e) {
                Bukkit.getLogger().info("[MineStore] Error while getting top donators from "+ link);
                e.printStackTrace();
            }
        } else {
            String link = Config.getApiUrl() + "top_donators";
            try {
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<TopDonoObjects>>() {}.getType();
                PlaceHolderData.setTopDonoObjects(gson.fromJson(inputStreamReader, listType));
                PlaceHolderData.createTopMap();
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