package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.util.Messages;
import com.nolanlabanane.tpsetwarp.warp.Warp;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Optional;

/**
 * /warpinfo <nom>
 * Affiche les détails d'un warp. Accessible à tous les joueurs (lecture seule).
 */
public final class WarpInfoCommand {

    private WarpInfoCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager) {
        return Commands.literal("warpinfo")
                .requires(source -> source.hasPermission(0))
                .then(Commands.argument("nom", StringArgumentType.word())
                        .executes(ctx -> execute(ctx, warpManager)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, WarpManager warpManager) {
        CommandSourceStack source = ctx.getSource();
        String name = StringArgumentType.getString(ctx, "nom");
        Optional<Warp> optWarp = warpManager.get(name);

        if (optWarp.isEmpty()) {
            source.sendFailure(Messages.warpNotFound(name));
            return 0;
        }

        Warp warp = optWarp.get();
        source.sendSuccess(() -> Component.literal("§a== Warp : " + warp.getName() + " =="), false);
        source.sendSuccess(() -> Component.literal("§7Dimension : §f" + warp.getDimension()), false);
        source.sendSuccess(() -> Component.literal(String.format(
                "§7Coordonnées : §f%.1f, %.1f, %.1f", warp.getX(), warp.getY(), warp.getZ())), false);
        source.sendSuccess(() -> Component.literal("§7Créateur : §f" + warp.getCreatorName()), false);
        source.sendSuccess(() -> Component.literal("§7Créé le : §f" + warp.getFormattedCreationDate()), false);
        return 1;
    }
}
