package me.chrommob.minestore.weblistener;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.authorization.AuthManager;
import me.chrommob.minestore.commandexecution.Command;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.weblistener.objects.WebListenerObjects;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;

public class Listener {
    private static HttpsURLConnection urlConnection;
    public static void run(int index) {
        String link;
        if (Config.getApiKey().get(index).equalsIgnoreCase("")
                || Config.getApiKey().get(index).equalsIgnoreCase("hard_secret_key_here")) {
            link = Config.getApiUrl().get(index) + "servers/commands/queue";
        } else {
            link = Config.getApiUrl().get(index) + "servers/" + Config.getApiKey().get(index) + "/commands/queue";
        }
        WebListenerObjects data = new WebListenerObjects();
        // Listening for commands
        try {
            URL url = new URL(link);
            urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                Gson gson = new Gson();
                data = gson.fromJson(line, WebListenerObjects.class);
            }
            if (data.getType() != null){
                AuthManager.auth(data.getAuth_id(), data.getUsername(), data.getId(), index);
            } else {
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
                        stringBuilder.append(" ").append(part);
                    }
                }
                commandWithoutPrefix = commandWithoutPrefix.replaceFirst("   ", " ");
                if (Bukkit.getPlayer(data.getUsername()) == null && data.isPlayerOnlineNeeded()) {
                    Command.offline(data.getUsername(), commandWithoutPrefix);
                } else {
                    Command.online(commandWithoutPrefix);
                }
            }
            post(data.getId(), index);
        } catch (Exception e) {
            if (e instanceof ClassCastException) {
                Bukkit.getLogger().info("Please use HTTPS instead of HTTP.");
            } else if (e instanceof SocketException || e instanceof SSLHandshakeException) {
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
    public static void post(int id, int index) {
        try {
            Config.getEmpty().set(index, false);
            String link;
            if (Config.getSecretKey().get(index).equalsIgnoreCase("")
                    || Config.getSecretKey().get(index).equalsIgnoreCase("hard_secret_key_here")) {
                link = Config.getApiUrl().get(index) + "servers/commands/executed/" + id;
            } else {
                link = Config.getApiUrl().get(index) + "servers/" + Config.getSecretKey().get(index) + "/commands/executed/" + id;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}