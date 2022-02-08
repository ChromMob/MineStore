package me.chrommob.minestore.weblistener;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import me.chrommob.minestore.commandsend.Command;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.weblistener.objects.WebListenerObjects;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Listener {
    private static HttpsURLConnection urlConnection;

    @SneakyThrows
    public static void run() {
        WebListenerObjects data = new WebListenerObjects();
        String link = "https://pro.minestorecms.com/api/servers/" + Config.getSecretKey() + "/commands/queue";
        URL url = new URL(link);
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                Gson gson = new Gson();
                data = gson.fromJson(line, WebListenerObjects.class);
            }

            if (data.getCommand() == null) {
                return;
            }
            String commandWithoutPrefix = data.getCommand().replaceFirst("/", "");
            commandWithoutPrefix = commandWithoutPrefix.replaceFirst("   ", " ");
            if (Bukkit.getPlayer(data.getUsername()) == null && data.isPlayerOnlineNeeded()) {
                Command.offline(data.getUsername(), commandWithoutPrefix);
            } else {
                Command.online(commandWithoutPrefix);
            }
            post(data.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
    }

    @SneakyThrows
    private static void post(int id) {
        String link = "https://pro.minestorecms.com/api/servers/" + Config.getSecretKey() + "/commands/executed/" + id;
        URL url = new URL(link);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        urlConnection.setDoOutput(true);
        try (final OutputStream os = urlConnection.getOutputStream()) {
            //get current time in milliseconds
            os.write(id);
        }
        urlConnection.getInputStream();
    }
}