package me.chrommob.minestore.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.chrommob.minestore.MineStore;
import org.bukkit.command.CommandSender;

@CommandAlias("MineStore|ms")
public class Reload extends BaseCommand {
    @Subcommand("reload")
    @CommandPermission("ms.reload")
    private void onReload(CommandSender sender) {
        try {
            MineStore.instance.loadConfig();
            sender.sendMessage("§aConfig reloaded! Be aware reloading doesn't reload the mode, websocket port, gui toggle and store toggle.");
        } catch (Exception e) {
            sender.sendMessage("§cAn error occurred while reloading the config.");
            e.printStackTrace();
        }
    }
}
