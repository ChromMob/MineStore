package me.chrommob.minestore.mysql.data;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.chrommob.minestore.MineStore;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

@Getter
@Setter
public class User {
    private String name;
    private UUID uuid;
    private String prefix;
    private String suffix;
    private double balance;
    private String group_name;
    private static Economy economy;
    private static Chat chat;

    public void update() {
        RegisteredServiceProvider<Economy> esp = MineStore.instance.getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<Chat> csp = MineStore.instance.getServer().getServicesManager().getRegistration(Chat.class);
        economy = esp.getProvider();
        chat = csp.getProvider();
        try {
            this.prefix = chat.getPlayerPrefix(Bukkit.getPlayer(uuid));
            this.suffix = chat.getPlayerSuffix(Bukkit.getPlayer(uuid));
            this.balance = economy.getBalance(Bukkit.getPlayer(uuid));
            this.group_name = chat.getPrimaryGroup(Bukkit.getPlayer(uuid));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User(UUID uuid, String name) {
        RegisteredServiceProvider<Economy> esp = MineStore.instance.getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<Chat> csp = MineStore.instance.getServer().getServicesManager().getRegistration(Chat.class);
        economy = esp.getProvider();
        chat = csp.getProvider();
        this.uuid = uuid;
        this.name = name;
        try {
            this.prefix = chat.getPlayerPrefix(Bukkit.getPlayer(uuid));
            this.suffix = chat.getPlayerSuffix(Bukkit.getPlayer(uuid));
            this.balance = economy.getBalance(Bukkit.getPlayer(uuid));
            this.group_name = chat.getPrimaryGroup(Bukkit.getPlayer(uuid));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
