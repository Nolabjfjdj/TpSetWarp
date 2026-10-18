package com.nolanlabanane.tpsetwarp.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

/**
 * Petits helpers autour des dimensions.
 * Regroupés ici pour éviter de dupliquer la logique de parsing/résolution
 * dans plusieurs classes.
 */
public final class DimensionUtil {

    private DimensionUtil() {
    }

    /** Convertit un ServerLevel en identifiant texte "namespace:path". */
    public static String toDimensionId(ServerLevel level) {
        return level.dimension().location().toString();
    }

    /**
     * Résout un identifiant de dimension texte ("minecraft:overworld", "minecraft:the_nether", ...)
     * vers le ServerLevel correspondant.
     * Retourne null si la dimension n'existe pas (mod désinstallé, dimension custom absente, etc.).
     */
    public static ServerLevel resolveLevel(MinecraftServer server, String dimensionId) {
        ResourceLocation location = ResourceLocation.tryParse(dimensionId);
        if (location == null) {
            return null;
        }
        ResourceKey<net.minecraft.world.level.Level> key =
                ResourceKey.create(Registries.DIMENSION, location);
        return server.getLevel(key);
    }
}
