package me.chrommob.minestore.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.gui.create.catGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("buy")
public class Buy extends BaseCommand {
    private MineStore plugin;
    public Buy(MineStore plugin) {
        this.plugin = plugin;
    }
    @Default
    @CommandPermission("ms.buy")
    private void onBuy(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cYou must be a player to use this command.");
            return;
        }
        Player player = (Player) sender;
        catGUI gui = new catGUI(plugin);
        gui.openGUI(player);
    }
}
