package me.chrommob.minestore.weblistener;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commandexecution.Command;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.weblistener.objects.WebListenerObjects;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;

public class Listener {
    private static HttpsURLConnection urlConnection;

    @SneakyThrows
    public static void run(int index) {
        String link;
        if (Config.getApiKey().get(index).equalsIgnoreCase("")
                || Config.getApiKey().get(index).equalsIgnoreCase("hard_secret_key_here")) {
            link = Config.getApiUrl().get(index) + "servers/commands/queue";
        } else {
            link = Config.getApiUrl() + "servers/" + Config.getApiKey().get(index) + "/commands/queue";
        }
        WebListenerObjects data = new WebListenerObjects();
        URL url = new URL(link);
        // Listening for commands
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
            String commandWithoutPrefix = data.getCommand();
            String[] commandArray = commandWithoutPrefix.split(" ");
            commandArray[0] = commandArray[0].replaceFirst("/", "");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(commandArray[0]);
            if (commandArray.length > 1) {
                for (String part : commandArray) {
                    stringBuilder.append(" " + part);
                }
            }
            commandWithoutPrefix = commandWithoutPrefix.replaceFirst("   ", " ");
            if (Bukkit.getPlayer(data.getUsername()) == null && data.isPlayerOnlineNeeded()) {
                Command.offline(data.getUsername(), commandWithoutPrefix);
            } else {
                Command.online(commandWithoutPrefix);
            }
            post(data.getId(), index);
        } catch (Exception e) {
            if (e instanceof ClassCastException) {
                Bukkit.getLogger().info("Please use HTTPS instead of HTTP.");
            } else if (e instanceof SocketException) {
                Bukkit.getLogger().info("Please check your internet connection.");
            } else {
                e.printStackTrace();
                Config.getEmpty().set(index, true);
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // Posting to the server that the command has been executed
    @SneakyThrows
    private static void post(int id, int index) {
        Config.getEmpty().set(index, true);
        String link;
        if (Config.getApiKey().get(index).equalsIgnoreCase("")
                || Config.getApiKey().get(index).equalsIgnoreCase("hard_secret_key_here")) {
            link = Config.getApiUrl().get(index) + "servers/commands/queue";
        } else {
            link = Config.getApiUrl() + "servers/" + Config.getApiKey().get(index) + "/commands/queue";
        }
        URL url = new URL(link);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        urlConnection.setDoOutput(true);
        try (final OutputStream os = urlConnection.getOutputStream()) {
            // get current time in milliseconds
            os.write(id);
        }
        urlConnection.getInputStream();
    }
}