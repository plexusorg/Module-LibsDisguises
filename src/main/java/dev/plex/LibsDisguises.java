package dev.plex;

import dev.plex.command.DisguiseToggleCMD;
import dev.plex.command.UndisguiseAllCMD;
import dev.plex.listener.DisguiseListener;
import dev.plex.module.PlexModule;
import java.util.List;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LibsDisguises extends PlexModule
{
    private boolean enabled = true;
    DisguiseListener disguiseListener;

    @Override
    public void load()
    {
        loadMessages("messages.yml");
        registerCommand(new DisguiseToggleCMD(this));
        registerCommand(new UndisguiseAllCMD(this));
    }

    @Override
    public void enable()
    {
        Plugin dependency = Bukkit.getPluginManager().getPlugin("LibsDisguises");
        if (dependency == null || !dependency.isEnabled())
        {
            throw new IllegalStateException("The Plex-LibsDisguises module requires the LibsDisguises plugin to work.");
        }
        disguiseListener = new DisguiseListener(this, dependency);
        registerListener(disguiseListener);
    }

    @Override
    public void disable()
    {
        // Unregistering listeners / commands is handled by Plex
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void undisguiseAll(boolean includeBypass)
    {
        scheduler().runGlobal(() ->
        {
            for (Player player : List.copyOf(Bukkit.getOnlinePlayers()))
            {
                scheduler().runEntity(player, () ->
                {
                    if (includeBypass || !player.hasPermission("plex.libsdisguises.bypass"))
                    {
                        DisguiseAPI.undisguiseToAll(player);
                    }
                });
            }
        });
    }
}
