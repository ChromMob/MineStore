package me.chrommob.minestore.data;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.chrommob.minestore.placeholders.objects.LastDonator;
import me.chrommob.minestore.placeholders.objects.TopDonoObjects;
import me.chrommob.minestore.util.HashMapSort;
import me.chrommob.minestore.util.HashMapSortDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PlaceHolderData {
    @Getter
    @Setter
    private static List<TopDonoObjects> topDonoObjects;

    @Getter
    @Setter
    private static List<LastDonator> lastDonatorsObjects;

    @Getter
    @Setter
    private static HashMap<String, Double> donators = new HashMap<>();

    @Getter
    private static HashMap<Integer, Double> topDonations = new HashMap<>();

    @Getter
    private static HashMap<Integer, String> topDonators = new HashMap<>();

    @Getter
    private static HashMap<Integer, Double> lastDonatorPrice = new HashMap<>();

    @Getter
    private static HashMap<Integer, String> lastDonator = new HashMap<>();

    @Getter
    @Setter
    private static double donationGoal;

    @Getter
    @Setter
    private static double donationGoalSum;

    private static int i;

    public static void createTopMap() {
        i = 0;
        donators.clear();
        topDonations.clear();
        topDonators.clear();
        for (TopDonoObjects topDonoObjects : topDonoObjects) {
            donators.put(topDonoObjects.getUsername(), topDonoObjects.getAmount());
        }
        donators = HashMapSort.sortByValue(donators);
        donators.forEach((k, v) -> {
            i++;
            topDonators.put(i, k);
            topDonations.put(i, v);
        });
    }

    private static int x;

    @SneakyThrows
    public static void createLastMap() {
        x = 0;
        HashMap<Integer, Date> prices = new HashMap<>();
        for (int i = 0; i < lastDonatorsObjects.size(); i++) {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastDonatorsObjects.get(i).getDate());
            prices.put(i, date);
        }
        prices = HashMapSortDate.sortByValue(prices);
        prices.forEach((k, v) -> {
            x++;
            lastDonator.put(x, lastDonatorsObjects.get(k).getName());
            lastDonatorPrice.put(x, lastDonatorsObjects.get(k).getAmount());
        });
    }
}
