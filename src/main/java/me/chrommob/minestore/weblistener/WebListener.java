package me.chrommob.minestore.weblistener;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commandexecution.Command;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.weblistener.objects.WebListenerObjects;
import org.spongepowered.api.Sponge;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class WebListener {
    private static HttpsURLConnection urlConnection;
    Config config;

    @SneakyThrows
    public boolean run() {
        config = MineStore.instance.getConfigData();
        String link;
        try {
            if (MineStore.instance.getConfigData().getSecret_key().equalsIgnoreCase("") || MineStore.instance.getConfigData().getSecret_key().equalsIgnoreCase("secret_key")) {
                link = config.getApi_link() + "servers/commands/queue";
            } else {
                link = config.getApi_link() + "servers/" + config.getSecret_key() + "/commands/queue";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
                return false;
            }
            String commandWithoutPrefix = data.getCommand().replaceFirst("/", "");
            commandWithoutPrefix = commandWithoutPrefix.replaceFirst("   ", " ");
            if (!Sponge.getServer().getPlayer(data.getUsername()).isPresent() && data.isPlayerOnlineNeeded()) {
                Command.offline(data.getUsername(), commandWithoutPrefix);
            } else {
                Command.online(commandWithoutPrefix);
            }
            post(data.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            urlConnection.disconnect();
        }
    }
    
    //Posting to the server that the command has been executed
    @SneakyThrows
    private void post(int id) {
        String link;
        if (MineStore.instance.getConfigData().getSecret_key().equalsIgnoreCase("") || MineStore.instance.getConfigData().getSecret_key().equalsIgnoreCase("secret_key")) {
            link = config.getApi_link() + "servers/commands/executed/" + id;
        } else {
            link = config.getApi_link() + "servers/" + config.getSecret_key() + "/commands/executed/" + id;
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