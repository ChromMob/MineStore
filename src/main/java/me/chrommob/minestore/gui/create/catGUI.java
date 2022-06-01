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

public class catGUI {
    private Inventory GUI;

    private final MineStore plugin;

    public catGUI(MineStore plugin) {
        this.plugin = plugin;
        createCatGui();
    }

    private void createCatGui() {

        GUI = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', Config.getGuiName()));
        for (int i = 0; i < GuiData.getData().size(); i++) {
            Material material;
            if (GuiData.getData().get(i).getGui_item_id() == null) {
                material = Material.CHEST;
            } else {
                if (Config.getVersion() >= 13) {
                    if (Material.matchMaterial(GuiData.getData().get(i).getGui_item_id().replaceFirst("minecraft:", ""), false) != null) {
                        material = Material.matchMaterial(GuiData.getData().get(i).getGui_item_id().replaceFirst("minecraft:", ""), false);
                    } else {
                        Bukkit.getLogger().info("[MineStore] Error: Material " + GuiData.getData().get(i).getGui_item_id() + " not found!");
                        material = Material.CHEST;
                    }
                } else {
                    if (Material.matchMaterial(GuiData.getData().get(i).getGui_item_id().replaceFirst("minecraft:", "")) != null) {
                        material = Material.matchMaterial(GuiData.getData().get(i).getGui_item_id().replaceFirst("minecraft:", ""));
                    } else {
                        Bukkit.getLogger().info("[MineStore] Error: Material " + GuiData.getData().get(i).getGui_item_id() + " not found!");
                        material = Material.CHEST;
                    }
                }
            }
            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', GuiData.getData().get(i).getName()));
            item.setItemMeta(meta);
            GUI.setItem(i, item);
        }
    }

    public void openGUI(Player player) {
        player.openInventory(GUI);
    }
}
