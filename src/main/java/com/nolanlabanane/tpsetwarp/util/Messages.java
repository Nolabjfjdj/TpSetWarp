package com.nolanlabanane.tpsetwarp.util;

import net.minecraft.network.chat.Component;

/**
 * Tous les messages envoyés aux joueurs sont centralisés ici.
 * Ça évite les chaînes de caractères éparpillées dans les commandes
 * et rend la traduction / la modification des textes triviale.
 */
public final class Messages {

    private Messages() {
    }

    public static Component warpCreated(String name) {
        return Component.literal("§aWarp '" + name + "' créé !");
    }

    public static Component warpAlreadyExists(String name) {
        return Component.literal("§cCe warp existe déjà.");
    }

    public static Component warpNotFound(String name) {
        return Component.literal("§cWarp introuvable.");
    }

    public static Component warpDeleted(String name) {
        return Component.literal("§aWarp '" + name + "' supprimé.");
    }

    public static Component warpRenamed(String oldName, String newName) {
        return Component.literal("§aWarp '" + oldName + "' renommé en '" + newName + "'.");
    }

    public static Component teleporting(String name) {
        return Component.literal("§aTéléportation vers " + name + "...");
    }

    public static Component dimensionUnavailable() {
        return Component.literal("§cLa dimension de ce warp est introuvable.");
    }

    public static Component noWarps() {
        return Component.literal("§cAucun warp n'a été créé pour le moment.");
    }

    public static Component warpsListHeader(int count) {
        return Component.literal("§aWarps disponibles (" + count + ") :");
    }

    public static Component warpsListEntry(String name) {
        return Component.literal("§7- §f" + name);
    }

    public static Component playerOnlyCommand() {
        return Component.literal("§cCette commande ne peut être utilisée que par un joueur.");
    }

    public static Component internalError() {
        return Component.literal("§cUne erreur interne est survenue. Vérifie les logs du serveur.");
    }
}
