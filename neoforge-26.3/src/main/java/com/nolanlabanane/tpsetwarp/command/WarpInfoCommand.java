package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.lang.Language;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
import com.nolanlabanane.tpsetwarp.util.Messages;
import com.nolanlabanane.tpsetwarp.warp.Warp;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.Optional;

/**
 * /warpinfo <nom>
 * Affiche les détails d'un warp. Accessible à tous les joueurs (lecture seule).
 */
public final class WarpInfoCommand {

    private WarpInfoCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager, LanguageManager languageManager) {
        return Commands.literal("warpinfo")
                .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                .then(Commands.argument("nom", StringArgumentType.word())
                        .executes(ctx -> execute(ctx, warpManager, languageManager)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, WarpManager warpManager, LanguageManager languageManager) {
        CommandSourceStack source = ctx.getSource();
        Language lang = languageManager.resolve(source);
        String name = StringArgumentType.getString(ctx, "nom");
        Optional<Warp> optWarp = warpManager.get(name);

        if (optWarp.isEmpty()) {
            source.sendFailure(Messages.warpNotFound(lang, name));
            return 0;
        }

        Warp warp = optWarp.get();
        source.sendSuccess(() -> Messages.warpInfoHeader(lang, warp.getName()), false);
        source.sendSuccess(() -> Messages.warpInfoDimension(lang, warp.getDimension()), false);
        source.sendSuccess(() -> Messages.warpInfoCoordinates(lang, warp.getX(), warp.getY(), warp.getZ()), false);
        source.sendSuccess(() -> Messages.warpInfoCreator(lang, warp.getCreatorName()), false);
        source.sendSuccess(() -> Messages.warpInfoCreatedOn(lang, warp.getFormattedCreationDate()), false);
        return 1;
    }
}
