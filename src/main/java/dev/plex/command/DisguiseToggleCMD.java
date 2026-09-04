package dev.plex.command;

import static dev.plex.api.message.MessagePlaceholder.placeholder;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.plex.LibsDisguises;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
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
    protected void configureCommand(LiteralArgumentBuilder<CommandSourceStack> command)
    {
        command.executes(context -> executeCommand(context, (sender, player) -> toggle(sender)));
        command.then(greedyString("ignored").executes(context -> executeCommand(context, (sender, player) -> toggle(sender))));
    }

    private Component toggle(CommandSender commandSender)
    {
        module.setEnabled(!module.isEnabled());
        if (!module.isEnabled())
        {
            module.undisguiseAll(true);
        }
        broadcast(messageComponent(module.isEnabled() ? "disguisesEnabled" : "disguisesDisabled", placeholder("player", commandSender.getName())));
        return null;
    }

}
