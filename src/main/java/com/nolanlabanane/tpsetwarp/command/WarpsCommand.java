package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nolanlabanane.tpsetwarp.lang.Language;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
import com.nolanlabanane.tpsetwarp.util.Messages;
import com.nolanlabanane.tpsetwarp.warp.Warp;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * /warps
 * Affiche la liste de tous les warps. Accessible à tous les joueurs.
 */
public final class WarpsCommand {

    private WarpsCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(WarpManager warpManager, LanguageManager languageManager) {
        return Commands.literal("warps")
                .requires(source -> source.hasPermission(0))
                .executes(ctx -> execute(ctx, warpManager, languageManager));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, WarpManager warpManager, LanguageManager languageManager) {
        CommandSourceStack source = ctx.getSource();
        Language lang = languageManager.resolve(source);
        Collection<Warp> warps = warpManager.getAll();

        if (warps.isEmpty()) {
            source.sendFailure(Messages.noWarps(lang));
            return 0;
        }

        source.sendSuccess(() -> Messages.warpsListHeader(lang, warps.size()), false);

        String names = warps.stream()
                .map(Warp::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.joining(", "));

        source.sendSuccess(() -> Component.literal("§7" + names), false);
        return warps.size();
    }
}
