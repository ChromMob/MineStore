package me.chrommob.minestore.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.chrommob.minestore.MineStore;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdateChecker {
    private final String currentVersion;
    private final String newVersion;
    private String downloadLink;

    public UpdateChecker() {
        this.currentVersion = getCurrentVersion();
        this.newVersion = getNewVersion();
        if (newVersion == null) {
            MineStore.instance.getLogger().warning("Failed to check for updates.");
            return;
        }
        if (isUpdateAvailable()) {
            downloadLink = getDownloadLink();
            if (downloadLink != null) {
                downloadUpdate();
            } else {
                MineStore.instance.getLogger().warning("Failed to get download link.");
            }
        }
    }

    private String getDownloadLink() {
        String repository = "ChromMob/MineStore"; // Replace with the repository you want to fetch the commit history for
        String apiUrl = "https://api.github.com/repos/" + repository + "/actions/artifacts";

        try {
            URL url = new URL(apiUrl);
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
            JsonArray object = root.get(1).getAsJsonArray();
            // Get the first commit in the list (which should be the latest commit)
            JsonObject latestAction = root.get(0).getAsJsonObject();

            // Extract the commit information from the JSON object
            return latestAction.get("archive_download_url").getAsString();
        } catch (IOException e) {
            // Handle error
        }
        return null;
    }

    private boolean isUpdateAvailable() {
        return !currentVersion.equals(newVersion);
    }

    private void downloadUpdate() {
        try {
            URL downloadUrl = new URL(downloadLink);
            InputStream inputStream = new BufferedInputStream(downloadUrl.openStream());
            Path path = Paths.get("plugins/MineStore.zip");
            FileOutputStream outputStream = new FileOutputStream(path.toFile());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNewVersion() {
        String repository = "ChromMob/MineStore"; // Replace with the repository you want to fetch the commit history for
        String apiUrl = "https://api.github.com/repos/" + repository + "/commits";

        try {
            URL url = new URL(apiUrl);
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
