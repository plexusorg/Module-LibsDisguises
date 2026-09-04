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

public class UndisguiseAllCMD extends SimplePlexCommand
{
    private final LibsDisguises module;

    public UndisguiseAllCMD(LibsDisguises module)
    {
        super(command("undisguiseall")
                .description("Undisguise all players")
                .usage("/<command> [-a]")
                .aliases("undisall,uall")
                .permission("plex.libsdisguises.undisguiseall")
                .build());
        this.module = module;
    }
    @Override
    protected void configureCommand(LiteralArgumentBuilder<CommandSourceStack> command)
    {
        command.executes(context -> executeCommand(context, (sender, player) -> undisguise(sender, null)));
        command.then(word("flag").suggests((context, builder) -> suggestMatching(builder, List.of("-a")))
                .executes(context -> executeCommand(context, (sender, player) -> undisguise(sender, string(context, "flag"))))
                .then(greedyString("ignored").executes(context -> executeCommand(context,
                        (sender, player) -> undisguise(sender, string(context, "flag"))))));
    }

    private Component undisguise(CommandSender sender, @Nullable String flag)
    {
        if (flag == null)
        {
            module.undisguiseAll(false);
            broadcast(messageComponent("undisguiseAllNonAdmins", placeholder("player", sender.getName())));
            return null;
        }
        else if (flag.equalsIgnoreCase("-a"))
        {
            module.undisguiseAll(true);
            broadcast(messageComponent("undisguiseAllPlayers", placeholder("player", sender.getName())));
            return null;
        }
        return usage();
    }

}
