package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.teleport.TeleportHandler;
import com.nolanlabanane.tpsetwarp.util.Messages;
import com.nolanlabanane.tpsetwarp.warp.Warp;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * /warp <nom>
 * Accessible à tous les joueurs (niveau de permission 0, système vanilla).
 */
public final class WarpCommand {

    private WarpCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager) {
        return Commands.literal("warp")
                .requires(source -> source.hasPermission(0))
                .then(Commands.argument("nom", StringArgumentType.word())
                        .executes(ctx -> execute(ctx, warpManager)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, WarpManager warpManager) {
        CommandSourceStack source = ctx.getSource();

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Messages.playerOnlyCommand());
            return 0;
        }

        String name = StringArgumentType.getString(ctx, "nom");
        Optional<Warp> warp = warpManager.get(name);

        if (warp.isEmpty()) {
            source.sendFailure(Messages.warpNotFound(name));
            return 0;
        }

        TeleportHandler.Result result = TeleportHandler.teleport(player, warp.get());

        if (result == TeleportHandler.Result.DIMENSION_NOT_FOUND) {
            source.sendFailure(Messages.dimensionUnavailable());
            return 0;
        }

        source.sendSuccess(() -> Messages.teleporting(name), false);
        return 1;
    }
}
