package me.chrommob.minestore.commandexecution;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commands.PunishmentManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import static me.chrommob.minestore.commandexecution.Command.runLater;


public class JoinQuitListener {
    @Listener
    public void onPlayerJoin(final ClientConnectionEvent.Join event) {
        try {
            String name = event.getTargetEntity().getName();
            if (runLater.get(name).isEmpty()) {
                runLater.remove(name);
            } else {
                runLater.get(name).forEach(command -> {
                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
                });
                runLater.remove(name);
                PunishmentManager.update();
            }
        } catch (Exception ignored) {
        }
    }
}
