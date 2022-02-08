package me.chrommob.minestore.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.GuiData;
import me.chrommob.minestore.gui.objects.ListObject;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class BuyListener {
    private static HttpsURLConnection urlConnection;
    @SneakyThrows
    public static void run() {
        String link = Config.getBuyUrl();
        URL url = new URL(link);
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            Type listType = new TypeToken<List<ListObject>>() {}.getType();
            GuiData.setData(new Gson().fromJson(inputStreamReader, listType));
            GuiData.createSub();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}