package me.chrommob.minestore.placeholders.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.PlaceHolderData;
import me.chrommob.minestore.placeholders.objects.DonationGoal;
import me.chrommob.minestore.placeholders.objects.TopDonoObjects;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class DonationGoalListener {
    private static HttpsURLConnection urlConnection;
    @SneakyThrows
    //Trying to get the donation goal from the server with the secret key
    public static void run() {
        if (!(Config.getApiUrl().equalsIgnoreCase("hard_api_key_here") || Config.getApiUrl().equalsIgnoreCase(""))) {
            try {
                String link = Config.getApiUrl() + Config.getApiKey() + "/donation_goal";
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                final DonationGoal donationGoal = gson.fromJson(inputStreamReader, DonationGoal.class);
                PlaceHolderData.setDonationGoal(donationGoal.getGoal());
                PlaceHolderData.setDonationGoalSum(donationGoal.getGoalSum());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //If the api key is not set, we will try to get the donation goal from the server withouth the secret key
            try {
                String link = Config.getApiUrl() + "donation_goal";
                URL url = new URL(link);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                final DonationGoal donationGoal = gson.fromJson(inputStreamReader, DonationGoal.class);
                PlaceHolderData.setDonationGoal(donationGoal.getGoal());
                PlaceHolderData.setDonationGoalSum(donationGoal.getGoalSum());
            } catch (Exception e) {
                if (e instanceof ClassCastException){
                    Bukkit.getLogger().info("Please use HTTPS instead of HTTP.");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }
}
