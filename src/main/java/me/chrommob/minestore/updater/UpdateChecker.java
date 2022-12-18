package me.chrommob.minestore.updater;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.chrommob.minestore.MineStore;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UpdateChecker {
    private final String currentVersion;
    private final String newVersion;
    private String downloadLink;
    private final File zipFile = new File(MineStore.instance.getDataFolder().getParentFile() + File.separator + "MineStore", "MineStore.zip");
    private final File pluginFile = new File(MineStore.instance.getDataFolder().getParentFile() + File.separator + "MineStore", "MineStore.jar");

    public UpdateChecker() {
        if (zipFile.exists()) {
            if (zipFile.delete()) {
                MineStore.instance.getLogger().info("Deleted old zip file!");
            } else {
                MineStore.instance.getLogger().info("Failed to delete old zip file!");
            }
        }
        if (pluginFile.exists()) {
            if (pluginFile.delete()) {
                MineStore.instance.getLogger().info("Deleted old plugin file!");
            } else {
                MineStore.instance.getLogger().info("Failed to delete old plugin file!");
            }
        }
        this.currentVersion = getCurrentVersion();
        this.newVersion = getNewVersion();
        if (newVersion == null) {
            MineStore.instance.getLogger().warning("Failed to check for updates.");
            return;
        }
        if (isUpdateAvailable()) {
            downloadLink = "https://nightly.link/ChromMob/MineStore/workflows/maven/main/artifact.zip";
            if (downloadUpdate()) {
                MineStore.instance.getLogger().info("Update downloaded successfully.");
                if (unzipFile()) {
                    if (!isWindows() && replaceFile()) {
                        MineStore.instance.getLogger().info("File replaced successfully. So it will not be needed to be redownloaded on restart.");
                    } else {
                        MineStore.instance.getLogger().info("File not replaced. You are on Windows or the file is not found.");
                    }
                    new UpdateLoader();
                } else {
                    MineStore.instance.getLogger().warning("Failed to load update.");
                    MineStore.instance.getServer().shutdown();
                }
            } else {
                MineStore.instance.getLogger().warning("Failed to download update.");
            }
        } else {
            MineStore.instance.getLogger().info("No update available.");
        }
    }

    private boolean replaceFile() {
        return pluginFile.renameTo(new File(MineStore.instance.getDataFolder().getParentFile(), "MineStore.jar"));
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private boolean unzipFile() {
        FileInputStream fis;
        ZipInputStream zipIs;
        ZipEntry zentry;
        try {
            fis = new FileInputStream(zipFile);
            zipIs = new ZipInputStream(fis);
            while ((zentry = zipIs.getNextEntry()) != null) {
                try {
                    byte[] buffer = new byte[1024];
                    if (zentry.getName().equalsIgnoreCase("MineStore.jar")) {
                        FileOutputStream fos = new FileOutputStream(pluginFile);
                        int length;
                        while ((length = zipIs.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            zipIs.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pluginFile.exists();
    }


    private boolean isUpdateAvailable() {
        return !currentVersion.equals(newVersion);
    }

    private boolean downloadUpdate() {
        try {
            URL downloadUrl = new URL(downloadLink);
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(zipFile);

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
        return zipFile.exists();
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
