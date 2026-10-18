package com.nolanlabanane.tpsetwarp;

import com.nolanlabanane.tpsetwarp.command.WarpCommands;
import com.nolanlabanane.tpsetwarp.lang.LanguageManager;
import com.nolanlabanane.tpsetwarp.warp.WarpManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TpSetWarpMod.MOD_ID)
public final class TpSetWarpMod {
    public static final String MOD_ID = "tpsetwarp";
    public static final Logger LOGGER = LoggerFactory.getLogger("TpSetWarp");
    private final WarpManager warpManager = new WarpManager();
    private final LanguageManager languageManager = new LanguageManager();

    public TpSetWarpMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        LOGGER.info("TpSetWarp NeoForge 26.3 initialisé.");
    }

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
        WarpCommands.registerAll(event.getDispatcher(), warpManager, languageManager);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("TpSetWarp - arrêt propre.");
    }
}
