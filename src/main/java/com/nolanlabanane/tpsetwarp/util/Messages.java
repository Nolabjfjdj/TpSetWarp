package com.nolanlabanane.tpsetwarp.util;

import com.nolanlabanane.tpsetwarp.lang.Language;
import net.minecraft.network.chat.Component;

/**
 * Tous les messages envoyés aux joueurs sont centralisés ici, en anglais et en français.
 * Chaque méthode prend la langue du joueur en premier paramètre.
 */
public final class Messages {

    private Messages() {
    }

    public static Component warpCreated(Language lang, String name) {
        return text(lang,
                "§aWarp '" + name + "' créé !",
                "§aWarp '" + name + "' created!");
    }

    public static Component warpAlreadyExists(Language lang, String name) {
        return text(lang,
                "§cCe warp existe déjà.",
                "§cThis warp already exists.");
    }

    public static Component warpNotFound(Language lang, String name) {
        return text(lang,
                "§cWarp introuvable.",
                "§cWarp not found.");
    }

    public static Component warpDeleted(Language lang, String name) {
        return text(lang,
                "§aWarp '" + name + "' supprimé.",
                "§aWarp '" + name + "' deleted.");
    }

    public static Component warpRenamed(Language lang, String oldName, String newName) {
        return text(lang,
                "§aWarp '" + oldName + "' renommé en '" + newName + "'.",
                "§aWarp '" + oldName + "' renamed to '" + newName + "'.");
    }

    public static Component teleporting(Language lang, String name) {
        return text(lang,
                "§aTéléportation vers " + name + "...",
                "§aTeleporting to " + name + "...");
    }

    public static Component dimensionUnavailable(Language lang) {
        return text(lang,
                "§cLa dimension de ce warp est introuvable.",
                "§cThis warp's dimension could not be found.");
    }

    public static Component noWarps(Language lang) {
        return text(lang,
                "§cAucun warp n'a été créé pour le moment.",
                "§cNo warps have been created yet.");
    }

    public static Component warpsListHeader(Language lang, int count) {
        return text(lang,
                "§aWarps disponibles (" + count + ") :",
                "§aAvailable warps (" + count + "):");
    }

    public static Component playerOnlyCommand(Language lang) {
        return text(lang,
                "§cCette commande ne peut être utilisée que par un joueur.",
                "§cThis command can only be used by a player.");
    }

    public static Component internalError(Language lang) {
        return text(lang,
                "§cUne erreur interne est survenue. Vérifie les logs du serveur.",
                "§cAn internal error occurred. Check the server logs.");
    }

    public static Component languageChanged(Language newLang) {
        return text(newLang,
                "§aLangue changée en Français.",
                "§aLanguage changed to English.");
    }

    public static Component warpInfoHeader(Language lang, String name) {
        return text(lang,
                "§a== Warp : " + name + " ==",
                "§a== Warp: " + name + " ==");
    }

    public static Component warpInfoDimension(Language lang, String dimension) {
        return text(lang,
                "§7Dimension : §f" + dimension,
                "§7Dimension: §f" + dimension);
    }

    public static Component warpInfoCoordinates(Language lang, double x, double y, double z) {
        return text(lang,
                String.format("§7Coordonnées : §f%.1f, %.1f, %.1f", x, y, z),
                String.format("§7Coordinates: §f%.1f, %.1f, %.1f", x, y, z));
    }

    public static Component warpInfoCreator(Language lang, String creatorName) {
        return text(lang,
                "§7Créateur : §f" + creatorName,
                "§7Creator: §f" + creatorName);
    }

    public static Component warpInfoCreatedOn(Language lang, String date) {
        return text(lang,
                "§7Créé le : §f" + date,
                "§7Created on: §f" + date);
    }

    private static Component text(Language lang, String fr, String en) {
        return Component.literal(lang == Language.FR ? fr : en);
    }
}
