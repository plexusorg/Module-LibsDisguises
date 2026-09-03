package dev.plex.listener;

import dev.plex.LibsDisguises;
import java.util.ArrayList;
import java.util.List;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.AreaEffectCloudWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.EnderDragonWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PhantomWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WitherWatcher;
import me.libraryaddict.disguise.events.DisguiseEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class DisguiseListener implements Listener
{
    private final LibsDisguises module;

    public DisguiseListener(LibsDisguises module, Plugin dependency)
    {
        this.module = module;
        commands.addAll(PluginCommandYamlParser.parse(dependency));
        module.api().logging().info("Successfully fetched all LibsDisguises commands!");
    }

    private static float safeYMod(float f)
    {
        return Math.max(-256f, Math.min(256f, f));
    }

    @EventHandler
    // The decisions are the complete ordered safety policy for one disguise operation.
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public void onDisguiseEvent(DisguiseEvent event)
    {
        event.setCancelled(true);
        if (event.getDisguise().getType() == DisguiseType.FISHING_HOOK)
        {
            event.getCommandSender().sendMessage(module.messageComponent("fishingHookDisguiseDenied"));
            return;
        }
        String name = event.getDisguise().getWatcher().getCustomName();
        if (name != null)
        {
            int noColorLen = PlainTextComponentSerializer.plainText().serialize(LegacyComponentSerializer.legacySection().deserialize(name)).length();
            // each color code counts as one char rather than two, for flexibility
            if (((name.length() - noColorLen) / 2) + noColorLen > 32)
            {
                event.getCommandSender().sendMessage(module.messageComponent("disguiseNameTooLong"));
                return;
            }
        }
        if (event.getDisguise().getWatcher() instanceof EnderDragonWatcher watcher && watcher.getPhase() == 7)
        {
            watcher.setPhase(6);
        }
        if (event.getDisguise().getWatcher() instanceof WitherWatcher watcher && watcher.getInvulnerability() > 2048)
        {
            watcher.setInvulnerability(2048);
        }
        if (event.getDisguise().isPlayerDisguise()
                && event.getCommandSender() instanceof Player playerSender
                && !playerSender.hasPermission("plex.libsdisguises.player"))
        {
            PlayerDisguise playerDisguise = (PlayerDisguise)event.getDisguise();
            String targetName = playerDisguise.getName();
            String origName = event.getDisguised().getName();
            playerDisguise.setName(origName);
            playerDisguise.setNameVisible(true);
            playerDisguise.getWatcher().setNameYModifier(0);
            playerDisguise.setSkin(targetName);
            playerDisguise.setDisplayedInTab(false);
            playerDisguise.setTablistName(origName);
        }
        if (event.getDisguise().isHidePlayer())
        {
            event.getDisguise().setHidePlayer(false);
        }
        if (event.getDisguise().getWatcher() instanceof AreaEffectCloudWatcher watcher)
        {
            if (watcher.getRadius() > 5)
            {
                watcher.setRadius(5);
            }
            else if (watcher.getRadius() < 0)
            {
                watcher.setRadius(0);
            }
        }
        event.getDisguise().getWatcher().setNameYModifier(safeYMod(event.getDisguise().getWatcher().getNameYModifier()));
        event.getDisguise().getWatcher().setYModifier(safeYMod(event.getDisguise().getWatcher().getYModifier()));
        if (event.getDisguise().getWatcher() instanceof SlimeWatcher watcher && watcher.getSize() > 10)
        {
            watcher.setSize(10);
        }
        if (event.getDisguise().getWatcher() instanceof PhantomWatcher watcher)
        {
            if (watcher.getSize() > 20)
            {
                watcher.setSize(20);
            }
            else if (watcher.getSize() < -36)
            {
                watcher.setSize(-36);
            }
        }
        event.setCancelled(false);
    }

    final List<Command> commands = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        String message = event.getMessage();
        // Don't check the arguments
        String commandLabel = message.replaceAll("\\s.*", "").replaceFirst("/", "");
        if (!module.isEnabled())
        {
            boolean disguiseCommand = commands.stream().anyMatch(command ->
                    command.getName().equalsIgnoreCase(commandLabel)
                            || command.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(commandLabel)));
            if (disguiseCommand)
            {
                event.getPlayer().sendMessage(module.messageComponent("libsDisguisesCurrentlyDisabled"));
                event.setCancelled(true);
            }
        }
    }
}
