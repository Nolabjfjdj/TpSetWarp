package com.nolanlabanane.tpsetwarp;

import com.nolanlabanane.tpsetwarp.command.WarpCommands;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLCommonSetupEvent;
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

    /** Instance unique du gestionnaire de warps, créée au démarrage du serveur. */
    private WarpManager warpManager;

    public TpSetWarpMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("TpSetWarp - initialisation terminée.");
    }

    /**
     * Le monde n'est disponible qu'au démarrage du serveur : c'est ici qu'on
     * crée le WarpManager et qu'on charge les warps existants depuis le disque.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        var worldDataFolder = server.getWorldPath(LevelResource.ROOT).resolve(MOD_ID);

        warpManager = new WarpManager(worldDataFolder);
        warpManager.load();

        LOGGER.info("TpSetWarp - {} warp(s) chargé(s).", warpManager.count());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        if (warpManager == null) {
            // Sécurité : ne devrait jamais arriver car ServerStartingEvent précède
            // toujours RegisterCommandsEvent, mais on évite un crash silencieux.
            LOGGER.warn("WarpManager non initialisé lors de l'enregistrement des commandes !");
            return;
        }
        WarpCommands.registerAll(event.getDispatcher(), warpManager);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        // Les warps sont déjà sauvegardés à chaque modification (voir WarpManager),
        // donc rien à faire ici. Gardé comme point d'extension futur (ex: cache, stats...).
        LOGGER.info("TpSetWarp - arrêt propre.");
    }
}
