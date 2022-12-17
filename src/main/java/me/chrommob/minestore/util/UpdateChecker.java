package me.chrommob.minestore.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.chrommob.minestore.MineStore;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private final String currentVersion;
    private final String newVersion;

    public UpdateChecker() {
        this.currentVersion = getCurrentVersion();
        this.newVersion = getNewVersion();
        if (newVersion == null) {
            MineStore.instance.getLogger().warning("Failed to check for updates.");
            return;
        }
        if (isUpdateAvailable()) {
            MineStore.instance.getLogger().info("A new update is available.");
            MineStore.instance.getLogger().info("Current version: " + currentVersion);
            MineStore.instance.getLogger().info("New version: " + newVersion);
            MineStore.instance.getLogger().info("Download: https://github.com/ChromMob/MineStore/actions");
        }
    }

    private boolean isUpdateAvailable() {
        return !currentVersion.equals(newVersion);
    }

    private String getNewVersion() {
        String repository = "ChromMob/MineStore"; // Replace with the repository you want to fetch the commit history for
        String apiUrl = "https://api.github.com/repos/" + repository + "/commits";

        try {
            URL url = new URL("https://api.github.com/repos/ChromMob/MineStore/commits");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            JsonArray root = gson.fromJson(String.valueOf(response), JsonArray.class);

            // Get the first commit in the list (which should be the latest commit)
            JsonObject latestCommit = root.get(0).getAsJsonObject();

            // Extract the commit information from the JSON object
            return latestCommit.get("sha").getAsString();
        } catch (IOException e) {
            // Handle error
        }
        return null;
    }

    private String getCurrentVersion() {
        return MineStore.instance.getDescription().getVersion();
    }
}
