package me.chrommob.minestore.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.chrommob.minestore.data.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("store")
public class Store extends BaseCommand {
    @Default
    @CommandPermission("ms.store")
    private void onBuy(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Config.getStoreMessage()));
    }
}
