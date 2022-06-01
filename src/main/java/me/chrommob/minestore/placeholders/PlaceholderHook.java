package me.chrommob.minestore.placeholders;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.GuiData;
import me.chrommob.minestore.data.PlaceHolderData;
import me.chrommob.minestore.gui.Currency;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderHook extends PlaceholderExpansion {

    private final MineStore plugin;

    public PlaceholderHook(MineStore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "ChromMob";
    }

    @Override
    public String getIdentifier() {
        return "Minestore";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if(params.contains("top_donator_username_")) {
            int arg = Integer.parseInt(params.replaceFirst("top_donator_username_", ""));
            try {
                return PlaceHolderData.getTopDonators().get(arg);
            } catch (Exception ignored) {
            }
        }
        if(params.contains("top_donator_amount_")) {
            int arg = Integer.parseInt(params.replaceFirst("top_donator_amount_", ""));
            try {
                return PlaceHolderData.getTopDonations().get(arg).toString();
            } catch (Exception ignored) {
            }
        }
        if(params.contains("donation_goal")) {
            try {
                return (PlaceHolderData.getDonationGoal() + "");
            } catch (Exception ignored) {
            }
        }
        if(params.contains("donation_goal_sum")) {
            try {
                return (PlaceHolderData.getDonationGoalSum() + "");
            } catch (Exception ignored) {
            }
        }
        if (params.contains("main_currency")) {
            try {
                return (Currency.getCurrency());
            } catch (Exception ignored) {
            }
        }
        if (params.contains("last_donator_name")) {
            int arg = Integer.parseInt(params.replaceFirst("last_donator_name_", ""));
            try {
                return (PlaceHolderData.getLastDonator().get(arg));
            } catch (Exception ignored) {
            }
        }
        if (params.contains("last_donator_amount")) {
            int arg = Integer.parseInt(params.replaceFirst("last_donator_amount_", ""));
            try {
                return (PlaceHolderData.getLastDonatorPrice().get(arg).toString());
            } catch (Exception ignored) {
            }
        }
        return ""; // Placeholder is unknown by the Expansion
    }
}
