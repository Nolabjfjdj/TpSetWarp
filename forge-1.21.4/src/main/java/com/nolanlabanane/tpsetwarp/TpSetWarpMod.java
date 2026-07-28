package com.nolanlabanane.tpsetwarp;

import com.nolanlabanane.tpsetwarp.command.WarpCommands;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Point d'entrée du mod.
 * Ne contient QUE de l'initialisation / du câblage entre les composants,
 * aucune logique métier ici (voir warp/, command/, teleport/, storage/).
 */
@Mod(TpSetWarpMod.MOD_ID)
public final class TpSetWarpMod {

    public static final String MOD_ID = "tpsetwarp";
    public static final Logger LOGGER = LoggerFactory.getLogger("TpSetWarp");

    /**
     * Instance unique du gestionnaire de warps.
     * Créée dès le constructeur (et non au ServerStartingEvent) car RegisterCommandsEvent
     * peut se déclencher avant ServerStartingEvent selon le contexte (menu de création de
     * monde en solo notamment). Le stockage réel est branché plus tard via initialize().
     */
    private final WarpManager warpManager = new WarpManager();

    /**
     * Instance unique du gestionnaire de langues. Même pattern que warpManager :
     * créée dès le constructeur, stockage réel branché plus tard via initialize()
     * une fois qu'un monde est chargé (voir onServerStarting).
     */
    private final LanguageManager languageManager = new LanguageManager();

    public TpSetWarpMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("TpSetWarp - initialisation terminée.");
    }

    /**
     * Le monde n'est disponible qu'au démarrage du serveur : c'est ici qu'on
     * branche le stockage du WarpManager et du LanguageManager et qu'on charge
     * les warps et langues existants.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        var worldDataFolder = server.getWorldPath(LevelResource.ROOT).resolve(MOD_ID);

        warpManager.initialize(worldDataFolder);
        languageManager.initialize(worldDataFolder);

        LOGGER.info("TpSetWarp - {} warp(s) chargé(s).", warpManager.count());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // warpManager et languageManager existent toujours (créés dans le constructeur
        // du mod), même si ServerStartingEvent ne s'est pas encore déclenché à ce stade.
        WarpCommands.registerAll(event.getDispatcher(), warpManager, languageManager);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        // Les warps et langues sont déjà sauvegardés à chaque modification (voir
        // WarpManager / LanguageManager), donc rien à faire ici. Gardé comme point
        // d'extension futur (ex: cache, stats...).
        LOGGER.info("TpSetWarp - arrêt propre.");
    }
}
