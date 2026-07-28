package com.nolanlabanane.tpsetwarp.lang;

import com.nolanlabanane.tpsetwarp.storage.LanguageStorage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gère la langue préférée de chaque joueur (anglais par défaut si jamais définie).
 *
 * Suit le même pattern d'initialisation différée que WarpManager : l'instance
 * existe dès le lancement du mod (constructeur), et le stockage réel n'est
 * branché que plus tard via initialize(), une fois qu'un monde est chargé.
 * Voir WarpManager pour le détail de pourquoi c'est nécessaire (ordre des
 * events Forge : RegisterCommandsEvent peut précéder ServerStartingEvent).
 */
public final class LanguageManager {

    private final Map<UUID, Language> playerLanguages = new HashMap<>();
    private LanguageStorage storage;

    public LanguageManager() {
    }

    public void initialize(Path worldDataFolder) {
        this.storage = new LanguageStorage(worldDataFolder);
        playerLanguages.clear();
        playerLanguages.putAll(storage.load());
    }

    /** Langue préférée du joueur, ou anglais par défaut si jamais définie. */
    public Language get(UUID playerUuid) {
        return playerLanguages.getOrDefault(playerUuid, Language.EN);
    }

    public void set(UUID playerUuid, Language language) {
        playerLanguages.put(playerUuid, language);
        if (storage != null) {
            storage.save(playerLanguages);
        }
    }

    /**
     * Résout la langue à utiliser pour une source de commande donnée.
     * Retourne l'anglais par défaut si la source n'est pas un joueur (ex: console).
     */
    public Language resolve(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return get(player.getUUID());
        }
        return Language.EN;
    }
}
