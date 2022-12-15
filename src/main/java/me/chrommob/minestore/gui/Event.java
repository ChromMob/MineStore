package me.chrommob.minestore.gui;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.GuiData;
import me.chrommob.minestore.gui.create.packageGUI;
import me.chrommob.minestore.gui.create.subCatGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
            try {
                item = ChatColor.stripColor(event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName());
                item = item.toLowerCase();
            } catch (NullPointerException ignored) {
                return;
            }
            if (GuiData.getSubcategory().get(item) != null) {
                if (!GuiData.getSubcategory().get(item).isEmpty()) {
                    Player player = (Player) event.getWhoClicked();
                    subCatGUI gui = new subCatGUI(plugin, item);
                    gui.openGUI(player);
                    return;
                }
            }
            for (int i = 0; i < GuiData.getData().size(); i++) {
                for (int j = 0; j < GuiData.getData().get(i).getPackages().size(); j++) {
                    if (GuiData.getData().get(i).getPackages().get(j).getCategory_url().equalsIgnoreCase(item)) {
                        plugin.getLogger().info("Opening packageGUI for " + GuiData.getData().get(i).getPackages().get(j).getName());
                        Player player = (Player) event.getWhoClicked();
                        packageGUI gui = new packageGUI(plugin, item);
                        gui.openGUI(player);
                        return;
                    }
                    if (GuiData.getData().get(i).getPackages().get(j).getName().equalsIgnoreCase(item) && (GuiData.getSubcategory().containsValue(item) || GuiData.getSubcategory().containsKey(item)) || GuiData.getData().get(i).getName().equalsIgnoreCase(item)) {
                        plugin.getLogger().info("Opening packageGUI for " + GuiData.getData().get(i).getPackages().get(j).getName());
                        Player player = (Player) event.getWhoClicked();
                        packageGUI gui = new packageGUI(plugin, item);
                        gui.openGUI(player);
                        return;
                    }
                }
            }
            for (int i = 0; i < GuiData.getData().size(); i++) {
                for (int j = 0; j < GuiData.getData().get(i).getPackages().size(); j++) {
                    String name = GuiData.getData().get(i).getPackages().get(j).getName().toLowerCase();
                    if (name.contains(item)) {
                        Player player = (Player) event.getWhoClicked();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getPackageMessage() ));
                        event.getWhoClicked().closeInventory();
                        return;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
