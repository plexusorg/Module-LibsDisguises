package dev.plex;

import dev.plex.command.DisguiseToggleCMD;
import dev.plex.command.UndisguiseAllCMD;
import dev.plex.listener.DisguiseListener;
import dev.plex.module.PlexModule;
import org.bukkit.Bukkit;

public class LibsDisguises extends PlexModule
{
    private boolean enabled = true;
    DisguiseListener disguiseListener;

    @Override
    public void load()
    {
        loadMessages("libsdisguises/messages.yml");
    }

    @Override
    public void enable()
    {
        if (!Bukkit.getPluginManager().isPluginEnabled("LibsDisguises"))
        {
            api().logging().error("The Plex-LibsDisguises module requires the LibsDisguises plugin to work.");
            return;
        }
        registerCommand(new DisguiseToggleCMD(this));
        registerCommand(new UndisguiseAllCMD());
        disguiseListener = new DisguiseListener(this);
        disguiseListener.getCommands();
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
}
