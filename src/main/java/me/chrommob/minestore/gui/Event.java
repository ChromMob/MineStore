package me.chrommob.minestore.gui;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.GuiData;
import me.chrommob.minestore.gui.create.packageGUI;
import me.chrommob.minestore.gui.create.subCatGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Event implements Listener {
    private final MineStore plugin;
    public Event(MineStore plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event){
        try {
            String inventory_name = ChatColor.translateAlternateColorCodes('&', Config.getGuiName());
            if (!event.getView().getTitle().equals(inventory_name)) {
                return;
            }
            event.setCancelled(true);
            String item;
            String category_url;
            try {
                item = ChatColor.stripColor(event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName());
                item = item.toLowerCase();
            } catch (NullPointerException ignored) {
                return;
            }
            for (int i = 0; i < GuiData.getData().size(); i++) {
                if (GuiData.getData().get(i).getName().equalsIgnoreCase(item)) {
                    category_url = String.valueOf(i);
                    Player player = (Player) event.getWhoClicked();
                    if (GuiData.getData().get(i).getSubcategories().size() != 0) {
                        subCatGUI gui = new subCatGUI(plugin, category_url);
                        gui.openGUI(player);
                    } else {
                        category_url = GuiData.getData().get(i).getUrl();
                        packageGUI gui = new packageGUI(plugin, category_url);
                        gui.openGUI(player);
                    }
                    return;
                }
                for (int j = 0; j < GuiData.getData().get(i).getSubcategories().size(); j++) {
                    if (GuiData.getData().get(i).getSubcategories().get(j).getName().equalsIgnoreCase(item)) {
                        category_url = GuiData.getData().get(i).getSubcategories().get(j).getUrl();
                        Player player = (Player) event.getWhoClicked();
                        packageGUI gui = new packageGUI(plugin, category_url);
                        gui.openGUI(player);
                        return;
                    }
                }
                for (int j = 0; j < GuiData.getData().get(i).getPackages().size(); j++) {
                    if (GuiData.getData().get(i).getPackages().get(j).getName().equalsIgnoreCase(item)) {
                        Player player = (Player) event.getWhoClicked();
                        player.closeInventory();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getPackageMessage()));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
