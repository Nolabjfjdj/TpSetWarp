package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.util.DimensionUtil;
import com.nolanlabanane.tpsetwarp.util.Messages;
import com.nolanlabanane.tpsetwarp.warp.Warp;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

/**
 * /setwarp <nom>
 * Réservé aux opérateurs (niveau de permission 2, système vanilla).
 */
public final class SetWarpCommand {

    private SetWarpCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager) {
        return Commands.literal("setwarp")
                .requires(source -> source.hasPermission(2))
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

        if (warpManager.exists(name)) {
            source.sendFailure(Messages.warpAlreadyExists(name));
            return 0;
        }

        Warp warp = new Warp(
                name,
                player.getUUID(),
                player.getGameProfile().getName(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYRot(),
                player.getXRot(),
                DimensionUtil.toDimensionId(player.serverLevel()),
                System.currentTimeMillis()
        );

        warpManager.create(warp);
        source.sendSuccess(() -> Messages.warpCreated(name), true);
        return 1;
    }
}
