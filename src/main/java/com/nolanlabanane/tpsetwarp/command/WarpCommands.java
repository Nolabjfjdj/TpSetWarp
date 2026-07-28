package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;

/**
 * Point d'enregistrement unique de toutes les commandes du mod.
 * Ajouter une nouvelle commande = créer sa classe + une ligne ici.
 */
public final class WarpCommands {

    private WarpCommands() {
    }

    public static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher, WarpManager warpManager, LanguageManager languageManager) {
        dispatcher.register(SetWarpCommand.build(warpManager, languageManager));
        dispatcher.register(WarpCommand.build(warpManager, languageManager));
        dispatcher.register(DelWarpCommand.build(warpManager, languageManager));
        dispatcher.register(WarpsCommand.build(warpManager, languageManager));
        dispatcher.register(RenameWarpCommand.build(warpManager, languageManager));
        dispatcher.register(WarpInfoCommand.build(warpManager, languageManager));
        dispatcher.register(LanguageCommand.build(languageManager));
    }
}
