package dev.plex.command;

import dev.plex.listener.UndisguiseEvent;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UndisguiseAllCMD extends SimplePlexCommand
{
    public UndisguiseAllCMD()
    {
        super(command("undisguiseall")
                .description("Undisguise all players")
                .usage("/<command> [-a]")
                .aliases("undisall,uall")
                .permission("plex.libsdisguises.undisguiseall")
                .build());
    }
    @Override
    protected Component execute(@NotNull CommandSender sender, @Nullable Player player, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            Bukkit.getServer().getPluginManager().callEvent(new UndisguiseEvent(false));
            broadcast(messageComponent("undisguiseAllNonAdmins", sender.getName()));
            return null;
        }
        else if (args[0].equalsIgnoreCase("-a"))
        {
            Bukkit.getServer().getPluginManager().callEvent(new UndisguiseEvent(true));
            broadcast(messageComponent("undisguiseAllPlayers", sender.getName()));
            return null;
        }
        return usage();
    }

    @Override
    protected @NotNull List<String> suggestions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException
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
