package me.chrommob.minestore.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.gui.create.catGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("store")
public class Store extends BaseCommand {
    @Default
    @CommandPermission("ms.store")
    private void onBuy(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Config.getStoreMessage()));
    }
}
