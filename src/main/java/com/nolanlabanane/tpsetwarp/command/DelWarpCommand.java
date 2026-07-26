package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.lang.Language;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
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

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager, LanguageManager languageManager) {
        return Commands.literal("delwarp")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("nom", StringArgumentType.word())
                        .executes(ctx -> execute(ctx, warpManager, languageManager)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, WarpManager warpManager, LanguageManager languageManager) {
        CommandSourceStack source = ctx.getSource();
        Language lang = languageManager.resolve(source);
        String name = StringArgumentType.getString(ctx, "nom");

        if (!warpManager.delete(name)) {
            source.sendFailure(Messages.warpNotFound(lang, name));
            return 0;
        }

        source.sendSuccess(() -> Messages.warpDeleted(lang, name), true);
        return 1;
    }
}
