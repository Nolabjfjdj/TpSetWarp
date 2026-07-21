package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.util.Messages;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * /delwarp <nom>
 * Réservé aux opérateurs.
 */
public final class DelWarpCommand {

    private DelWarpCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager) {
        return Commands.literal("delwarp")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("nom", StringArgumentType.word())
                        .executes(ctx -> execute(ctx, warpManager)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, WarpManager warpManager) {
        CommandSourceStack source = ctx.getSource();
        String name = StringArgumentType.getString(ctx, "nom");

        if (!warpManager.delete(name)) {
            source.sendFailure(Messages.warpNotFound(name));
            return 0;
        }

        source.sendSuccess(() -> Messages.warpDeleted(name), true);
        return 1;
    }
}
