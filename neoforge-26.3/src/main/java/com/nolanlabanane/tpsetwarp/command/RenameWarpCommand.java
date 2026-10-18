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
 * /renamewarp <ancien> <nouveau>
 * Réservé aux opérateurs.
 */
public final class RenameWarpCommand {

    private RenameWarpCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager, LanguageManager languageManager) {
        return Commands.literal("renamewarp")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("ancien", StringArgumentType.word())
                        .then(Commands.argument("nouveau", StringArgumentType.word())
                                .executes(ctx -> execute(ctx, warpManager, languageManager))));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, WarpManager warpManager, LanguageManager languageManager) {
        CommandSourceStack source = ctx.getSource();
        Language lang = languageManager.resolve(source);
        String oldName = StringArgumentType.getString(ctx, "ancien");
        String newName = StringArgumentType.getString(ctx, "nouveau");

        if (!warpManager.exists(oldName)) {
            source.sendFailure(Messages.warpNotFound(lang, oldName));
            return 0;
        }

        if (warpManager.exists(newName)) {
            source.sendFailure(Messages.warpAlreadyExists(lang, newName));
            return 0;
        }

        warpManager.rename(oldName, newName);
        source.sendSuccess(() -> Messages.warpRenamed(lang, oldName, newName), true);
        return 1;
    }
}
