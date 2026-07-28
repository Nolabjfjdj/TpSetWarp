package com.nolanlabanane.tpsetwarp;

import com.nolanlabanane.tpsetwarp.command.WarpCommands;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Point d'entrée du mod (Fabric).
 * Ne contient QUE de l'initialisation / du câblage entre les composants,
 * aucune logique métier ici (voir warp/, command/, teleport/, storage/).
 */
public final class TpSetWarpMod implements ModInitializer {

    public static final String MOD_ID = "tpsetwarp";
    public static final Logger LOGGER = LoggerFactory.getLogger("TpSetWarp");

    /**
     * Instance unique du gestionnaire de warps.
     * Créée dès l'initialisation du mod (et non au démarrage du serveur) car
     * l'enregistrement des commandes peut se déclencher avant que le serveur/monde
     * ne soit prêt (menu de création de monde en solo notamment). Le stockage réel
     * est branché plus tard via initialize(), une fois le serveur démarré.
     */
    private final WarpManager warpManager = new WarpManager();

    /**
     * Instance unique du gestionnaire de langues. Même pattern que warpManager :
     * créée dès l'initialisation, stockage réel branché plus tard via initialize()
     * une fois qu'un monde est chargé (voir onServerStarting).
     */
    private final LanguageManager languageManager = new LanguageManager();

    @Override
    public void onInitialize() {
        LOGGER.info("TpSetWarp - initialisation terminée.");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                WarpCommands.registerAll(dispatcher, warpManager, languageManager));

        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
    }

    /**
     * Le monde n'est disponible qu'au démarrage du serveur : c'est ici qu'on
     * branche le stockage du WarpManager et du LanguageManager et qu'on charge
     * les warps et langues existants.
     */
    private void onServerStarting(MinecraftServer server) {
        var worldDataFolder = server.getWorldPath(LevelResource.ROOT).resolve(MOD_ID);

        warpManager.initialize(worldDataFolder);
        languageManager.initialize(worldDataFolder);

        LOGGER.info("TpSetWarp - {} warp(s) chargé(s).", warpManager.count());
    }

    private void onServerStopping(MinecraftServer server) {
        // Les warps et langues sont déjà sauvegardés à chaque modification (voir
        // WarpManager / LanguageManager), donc rien à faire ici. Gardé comme point
        // d'extension futur (ex: cache, stats...).
        LOGGER.info("TpSetWarp - arrêt propre.");
    }
}
