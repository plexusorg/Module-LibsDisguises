package dev.plex.command;

import dev.plex.command.annotation.CommandParameters;
import dev.plex.command.annotation.CommandPermissions;
import dev.plex.listener.UndisguiseEvent;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandParameters(name = "undisguiseall", usage = "/<command> [-a]", description = "Undisguise all players", aliases = "undisall,uall")
@CommandPermissions(permission = "plex.libsdisguises.undisguiseall")
public class UndisguiseAllCMD extends PlexCommand
{
    @Override
    protected Component execute(@NotNull CommandSender sender, @Nullable Player player, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            Bukkit.getServer().getPluginManager().callEvent(new UndisguiseEvent(false));
            Bukkit.broadcast(Component.text(sender.getName() + " - Undisguising all non-admins").color(NamedTextColor.RED));
            return null;
        }
        else if (args[0].equalsIgnoreCase("-a"))
        {
            Bukkit.getServer().getPluginManager().callEvent(new UndisguiseEvent(true));
            Bukkit.broadcast(Component.text(sender.getName() + " - Undisguising all players").color(NamedTextColor.RED));
            return null;
        }
        return usage();
    }

    @Override
    public @NotNull List<String> smartTabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException
    {
        if (silentCheckPermission(sender, this.getPermission()))
        {
            if (args.length == 1)
            {
                return Collections.singletonList("-a");
            }
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
}
