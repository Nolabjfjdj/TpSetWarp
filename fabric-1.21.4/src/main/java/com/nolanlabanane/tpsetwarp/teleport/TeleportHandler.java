package com.nolanlabanane.tpsetwarp.teleport;

import com.nolanlabanane.tpsetwarp.util.DimensionUtil;
import com.nolanlabanane.tpsetwarp.warp.Warp;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;

import java.util.Set;

/**
 * Toute la logique de téléportation est isolée ici.
 *
 * IMPORTANT POUR LE PORTAGE : si l'API Minecraft/Forge change la signature
 * de ServerPlayer#teleportTo entre deux versions, c'est LA SEULE classe
 * à modifier dans tout le mod.
 */
public final class TeleportHandler {

    private TeleportHandler() {
    }

    /**
     * Résultat d'une tentative de téléportation.
     */
    public enum Result {
        SUCCESS,
        DIMENSION_NOT_FOUND
    }

    /**
     * Téléporte un joueur vers un warp, y compris entre dimensions.
     * Conserve l'orientation (yaw/pitch) enregistrée dans le warp.
     */
    public static Result teleport(ServerPlayer player, Warp warp) {
        ServerLevel targetLevel = DimensionUtil.resolveLevel(player.getServer(), warp.getDimension());
        if (targetLevel == null) {
            return Result.DIMENSION_NOT_FOUND;
        }

        // Stoppe toute chute/élan en cours pour éviter les téléportations "glissées".
        player.fallDistance = 0;

        // teleportTo gère nativement le changement de dimension (ServerLevel identique ou non).
        // Depuis la 1.21.2, la signature demande en plus :
        //  - un Set<Relative> : vide ici, car warp.getX/Y/Z/Yaw/Pitch sont des coordonnées
        //    ABSOLUES (rien de relatif façon "~ ~ ~" façon commande /tp).
        //  - un boolean final (recentre la caméra du joueur sur la nouvelle position,
        //    comportement standard attendu pour une téléportation "franche" type warp).
        player.teleportTo(
                targetLevel,
                warp.getX(),
                warp.getY(),
                warp.getZ(),
                Set.of(),
                warp.getYaw(),
                warp.getPitch(),
                true
        );

        return Result.SUCCESS;
    }
}
