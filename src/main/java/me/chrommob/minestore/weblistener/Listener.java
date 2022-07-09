package me.chrommob.minestore.weblistener;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import me.chrommob.minestore.commandexecution.Command;
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
        String link;
        if (Config.getSecretKey().equalsIgnoreCase("") || Config.getSecretKey().equalsIgnoreCase("hard_secret_key_here")) {
            link = Config.getApiUrl() + "servers/commands/queue";
        } else {
            link = Config.getApiUrl() + "servers/" + Config.getSecretKey() + "/commands/queue";
        }
        WebListenerObjects data = new WebListenerObjects();
        URL url = new URL(link);
        //Listening for commands
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
            if (e instanceof ClassCastException){
                Bukkit.getLogger().info("Please use HTTPS instead of HTTP.");
            } else {
                e.printStackTrace();
                Config.setEmpty(true);
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
    
    //Posting to the server that the command has been executed
    @SneakyThrows
    private static void post(int id) {
        Config.setEmpty(false);
        String link;
        if (Config.getSecretKey().equalsIgnoreCase("") || Config.getSecretKey().equalsIgnoreCase("hard_secret_key_here")) {
            link = Config.getApiUrl() + "servers/commands/executed/" + id;
        } else {
            link = Config.getApiUrl() + "servers/" + Config.getSecretKey() + "/commands/executed/" + id;
        }
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