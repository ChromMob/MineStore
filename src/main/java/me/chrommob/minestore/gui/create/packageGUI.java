package me.chrommob.minestore.gui.create;

import me.chrommob.minestore.gui.Currency;
import me.chrommob.minestore.gui.objects.PackagesType;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.data.GuiData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.chrommob.minestore.util.HashMapSortInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class packageGUI {
    private final MineStore plugin;
    private Inventory GUI;

    public packageGUI(MineStore plugin, String catName) {
        this.plugin = plugin;
        createPackageGUI(catName);
    }

    private void createPackageGUI(String catName) {
        GUI = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', Config.getGuiName()));
        for (int i = 0; i < GuiData.getData().size(); i++) {
            int sorting = 0;
            HashMap<Integer, Integer> sortingMap = new HashMap<>();
            for (int j = 0; j < GuiData.getData().get(i).getPackages().size(); j++) {
                if (GuiData.getData().get(i).getPackages().get(j).getCategory_url().equalsIgnoreCase(catName)) {
                    if (GuiData.getData().get(i).getPackages().get(j).getActive() == 1) {
                        if (GuiData.getData().get(i).getPackages().get(j).getFeatured() == 1) {
                            sortingMap.put(j, -10);
                        } else {
                            sortingMap.put(j, GuiData.getData().get(i).getPackages().get(j).getSorting());
                        }
                    }
                }
            }
            Map<Integer, Integer> sortedMap = HashMapSortInt.sortByValue(sortingMap);
            for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
                PackagesType data = GuiData.getData().get(i).getPackages().get(entry.getKey());
                Material material = Material.CHEST;
                if (data.getItem_id() != null) {
                    String materialName = data.getItem_id();
                    String[] materialData = new String[2];
                    if (materialName.contains(":")) {
                        materialData = materialName.split(":");
                    } else {
                        materialData[0] = "0";
                        materialData[1] = materialName;
                    }
                    material = Material.matchMaterial(materialData[1]);
                    if (material == null) {
                        Bukkit.getLogger().info("[MineStore] Error: Material " + GuiData.getData().get(i).getGui_item_id() + " not found!");
                    }
                }
                ItemStack item = new ItemStack(material);
                if (entry.getValue() == -10) {
                    try {
                    item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<>();
                double doublePrice = data.getPrice() - ((data.getPrice() / 100) * data.getDiscount());
                double price = ((int) (doublePrice * 100)) / 100.0;
                try {
                    lore.add(ChatColor.translateAlternateColorCodes('&', Config.getItemPrice().replace("%minestore_item_price%", price + "").replace("%currency%", Currency.getCurrency())));
                } catch (Exception ignored) {}
                try {
                lore.add(ChatColor.translateAlternateColorCodes('&', Config.getItemDescription().replace("%minestore_item_description%", data.getItem_lore())));
                } catch (Exception ignored) {}
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setLore(lore);
                try {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Config.getItemName().replace("%minestore_item_name%", data.getName())));
                } catch (Exception ignored) {}
                item.setItemMeta(meta);
                GUI.setItem(sorting, item);
                sorting++;
            }
        }
    }

    public void openGUI(Player player) {
        player.openInventory(GUI);
    }
}
