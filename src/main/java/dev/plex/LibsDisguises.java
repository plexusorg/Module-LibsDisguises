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
        loadMessages("messages.yml");
        registerCommand(new DisguiseToggleCMD(this));
        registerCommand(new UndisguiseAllCMD());
    }

    @Override
    public void enable()
    {
        if (!Bukkit.getPluginManager().isPluginEnabled("LibsDisguises"))
        {
            throw new IllegalStateException("The Plex-LibsDisguises module requires the LibsDisguises plugin to work.");
        }
        disguiseListener = new DisguiseListener(this);
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
