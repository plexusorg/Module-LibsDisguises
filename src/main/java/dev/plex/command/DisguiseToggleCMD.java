package dev.plex.command;

import dev.plex.LibsDisguises;
import dev.plex.listener.UndisguiseEvent;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisguiseToggleCMD extends SimplePlexCommand
{
    private final LibsDisguises module;

    public DisguiseToggleCMD(LibsDisguises module)
    {
        super(command("disguisetoggle")
                .description("Toggle LibsDisguises")
                .aliases("dtoggle")
                .permission("plex.libsdisguises.disguisetoggle")
                .build());
        this.module = module;
    }

    @Override
    protected Component execute(@NotNull CommandSender commandSender, @Nullable Player player, @NotNull String[] strings)
    {
        module.setEnabled(!module.isEnabled());
        if (!module.isEnabled())
        {
            Bukkit.getServer().getPluginManager().callEvent(new UndisguiseEvent(true));
        }
        broadcast(messageComponent(module.isEnabled() ? "disguisesEnabled" : "disguisesDisabled", commandSender.getName()));
        return null;
    }

    @Override
    protected @NotNull List<String> suggestions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException
    {
        return Collections.emptyList();
    }
}
