package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.lang.Language;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
import com.nolanlabanane.tpsetwarp.util.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

/**
 * /warplang <fr|en>
 * Change la langue des messages du mod pour le joueur qui exécute la commande.
 * Accessible à tous les joueurs (niveau de permission 0, système vanilla) : chacun
 * choisit sa propre langue, indépendamment des autres.
 */
public final class LanguageCommand {

    private LanguageCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(LanguageManager languageManager) {
        return Commands.literal("warplang")
                .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                .then(Commands.argument("langue", StringArgumentType.word())
                        .executes(ctx -> execute(ctx, languageManager)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, LanguageManager languageManager) {
        CommandSourceStack source = ctx.getSource();

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Messages.playerOnlyCommand(languageManager.resolve(source)));
            return 0;
        }

        String code = StringArgumentType.getString(ctx, "langue");
        Language newLang = Language.fromCode(code);

        languageManager.set(player.getUUID(), newLang);
        source.sendSuccess(() -> Messages.languageChanged(newLang), false);
        return 1;
    }
}
