package me.chrommob.minestore.authorization;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.MalformedURLException;
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
                post(user.getName(), "deny");
                userHashMap.remove(user.getName());
            }
        }
    }

    private static ConcurrentHashMap<String, AuthUser> userHashMap = new ConcurrentHashMap<>();
    public static void auth(String auth_id, String username, int id, int index) {
        if (userHashMap.containsKey(username)) {
            return;
        }
        userHashMap.put(username, new AuthUser(username, auth_id, id, index));
        sendAuthMessage(username);
    }

    public static void sendAuthMessage(String username) {
        if (!userHashMap.containsKey(username)) {
            return;
        }
        Player player = Bukkit.getPlayer(username);
        if (player == null) return;
        TextComponent textComponent = TextComponent.builder()
                .content(Config.getAuthMessage())
                .color(TextColor.RED)
                .clickEvent(ClickEvent.runCommand("/minestore verify"))
                .build();
        TextAdapter.sendMessage(player, textComponent);
    }

    public static void onCommand(String username){
        Player player = Bukkit.getPlayer(username);
        if (player == null) return;
        if (!userHashMap.containsKey(username)) {
            TextComponent failed = TextComponent.builder()
                    .content(Config.getAuthFailed())
                    .color(TextColor.RED)
                    .build();
            TextAdapter.sendMessage(player, failed);
            return;
        }
        post(player.getName(), "confirm");
    }
    private static void post(String name, String state) {
        AuthUser user = userHashMap.get(name);
        try {
            HttpsURLConnection urlConnection;
            int index = userHashMap.get(name).getIndex();
            String link;
            if (Config.getApiKey().get(index).equalsIgnoreCase("")
                    || Config.getApiKey().get(index).equalsIgnoreCase("hard_api_key_here")) {
                link = Config.getApiUrl().get(index) + "game_auth/" + state + "?id" + user.getAuth_id();
            } else {
                link = Config.getApiUrl().get(index) + "game_auth/" + Config.getApiKey().get(index) + "/" + state + "?id=" + user.getAuth_id();
            }
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
