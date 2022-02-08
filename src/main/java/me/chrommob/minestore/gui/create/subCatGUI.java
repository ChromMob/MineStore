package me.chrommob.minestore.gui.create;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.GuiData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class subCatGUI {
    private final MineStore plugin;
    private Inventory GUI;

    public subCatGUI(MineStore plugin, String catName) {
        this.plugin = plugin;
        createSubCatGui(catName);
    }

    private void createSubCatGui(String catName) {
        GUI = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', Config.getGuiName()));
        for (int i = 0; i < GuiData.getData().size(); i++) {
            if (GuiData.getData().get(i).getName().equalsIgnoreCase(catName)) {
                for (int j = 0; j < GuiData.getData().get(i).getSubcategories().size(); j++) {
                    Material material = Material.CHEST;
                    if (Material.matchMaterial(GuiData.getData().get(i).getSubcategories().get(j).getGui_item_id().replaceFirst("minecraft:", "")) != null) {
                        material = Material.matchMaterial(GuiData.getData().get(i).getSubcategories().get(j).getGui_item_id().replaceFirst("minecraft:", ""));
                    }
                    ItemStack item = new ItemStack(material);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', GuiData.getData().get(i).getSubcategories().get(j).getName()));
                    item.setItemMeta(meta);
                    GUI.setItem(j, item);
                }
            }
        }
    }

    public void openGUI(Player player) {
        player.openInventory(GUI);
    }
}
