package com.nolanlabanane.tpsetwarp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.commands.CommandSourceStack;

/**
 * Point d'enregistrement unique de toutes les commandes du mod.
 * Ajouter une nouvelle commande = créer sa classe + une ligne ici.
 */
public final class WarpCommands {

    private WarpCommands() {
    }

    public static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher, WarpManager warpManager) {
        dispatcher.register(SetWarpCommand.build(warpManager));
        dispatcher.register(WarpCommand.build(warpManager));
        dispatcher.register(DelWarpCommand.build(warpManager));
        dispatcher.register(WarpsCommand.build(warpManager));
        dispatcher.register(RenameWarpCommand.build(warpManager));
        dispatcher.register(WarpInfoCommand.build(warpManager));
    }
}
