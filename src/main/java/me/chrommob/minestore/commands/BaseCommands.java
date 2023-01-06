package me.chrommob.minestore.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.chrommob.minestore.MineStore;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;

@CommandAlias("MineStore|ms")
public class BaseCommands extends BaseCommand {
    @Subcommand("reload")
    @CommandPermission("ms.reload")
    private void onReload(CommandSender sender) {
        try {
            MineStore.instance.loadConfig();
            sender.sendMessage("§aConfig reloaded! Be aware reloading doesn't reload the mode, websocket port, gui toggle, mysql toggle and store toggle.");
        } catch (Exception e) {
            sender.sendMessage("§cAn error occurred while reloading the config.");
            e.printStackTrace();
        }
    }
    @Subcommand("version")
    @CommandPermission("ms.version")
    private void onVersion(CommandSender sender) {
        sender.sendMessage("§aMineStore version: " + MineStore.instance.getDescription().getVersion());
    }

    @Subcommand("vault")
    @CommandPermission("ms.vault")
    private void onVault(CommandSender sender) {
        sender.sendMessage("Running vault checks...");
        Economy economy;
        try {
            economy = MineStore.instance.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        } catch (Exception e) {
            economy = null;
        }
        if (economy != null) {
            sender.sendMessage("§aEconomy: " + economy.getName());
        } else {
            sender.sendMessage("§cEconomy: null");
        }
        Chat chat;
        try {
            chat = MineStore.instance.getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        } catch (Exception e) {
            chat = null;
        }
        if (chat != null) {
            sender.sendMessage("§aChat: " + chat.getName());
        } else {
            sender.sendMessage("§cChat: null");
        }
    }
}
