package me.chrommob.minestore.authorization;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class AuthManager {
    public AuthManager(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(MineStore.instance, this::removeOld, 0, 20);
    }

    private void removeOld(){
        long currentTime = System.currentTimeMillis();
        if (userHashMap.isEmpty())
            return;
        for (AuthUser user : userHashMap.values()) {
            if (user.getEndTime() < currentTime) {
                //post(user.getName(), "deny");
                userHashMap.remove(user.getName());
            }
        }
    }

    private static final ConcurrentHashMap<String, AuthUser> userHashMap = new ConcurrentHashMap<>();
    public static void auth(String auth_id, String username, int id, int index) {
        if (Config.isDebug())
            MineStore.instance.getLogger().info("AuthManager.java received: auth_id: " + auth_id + " username: " + username + " id: " + id + " index: " + index);
        if (!userHashMap.containsKey(username)) {
            sendAuthMessage(username);
        }
        userHashMap.put(username, new AuthUser(username, auth_id, id, index));
    }

    public static void sendAuthMessage(String username) {
        if (!userHashMap.containsKey(username)) {
            return;
        }
        if (Config.isDebug())
            MineStore.instance.getLogger().info("AuthManager.java sendAuthMessage: " + username);
        Player player = Bukkit.getPlayer(username);
        if (player == null) return;
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(Config.getAuthMessage()).clickEvent(ClickEvent.runCommand("/minestore verify"));
        MineStore.instance.adventure().player(player).sendMessage(component);
    }

    public static void onCommand(String username){
        if (Config.isDebug())
            MineStore.instance.getLogger().info("AuthManager.java onCommand: " + username);
        Player player = Bukkit.getPlayer(username);
        if (player == null) return;
        if (!userHashMap.containsKey(username)) {
            Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(Config.getAuthFailed());
            MineStore.instance.adventure().player(player).sendMessage(component);
            return;
        }
        post(player.getName(), "confirm");
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(Config.getAuthSuccessful());
        MineStore.instance.adventure().player(player).sendMessage(component);
    }
    private static void post(String name, String state) {
        if (Config.isDebug())
            MineStore.instance.getLogger().info("AuthManager.java post: " + name + " " + state);
        AuthUser user = userHashMap.get(name);
        try {
            HttpsURLConnection urlConnection;
            int index = user.getIndex();
            String link;
            link = Config.getApiUrl().get(index) + "game_auth/" + state + "/" + user.getAuth_id();
            URL url;
            url = new URL(link);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlConnection.setDoOutput(true);
            try (final OutputStream os = urlConnection.getOutputStream()) {
                // get current time in milliseconds
                os.write(5);
            }
            urlConnection.getInputStream();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
