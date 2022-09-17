package me.chrommob.minestore.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.chrommob.minestore.authorization.AuthManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("ms|minestore")
public class Verify extends BaseCommand {
    @Subcommand("verify")
    public void onVerify(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Command must be ran by a player.");
            return;
        }
        AuthManager.onCommand(commandSender.getName());
    }
}
