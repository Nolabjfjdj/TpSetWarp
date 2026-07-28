package com.nolanlabanane.tpsetwarp.warp;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Représente un warp unique.
 * Classe immuable : toute modification (rename, etc.) passe par WarpManager
 * qui recrée une nouvelle instance plutôt que de muter celle-ci.
 */
public final class Warp {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

    private final String name;
    private final UUID creatorUuid;
    private final String creatorName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    /** ID de dimension au format "namespace:path", ex: "minecraft:overworld". */
    private final String dimension;
    /** Timestamp de création (epoch millis). */
    private final long creationTime;

    public Warp(String name, UUID creatorUuid, String creatorName,
                double x, double y, double z, float yaw, float pitch,
                String dimension, long creationTime) {
        this.name = name;
        this.creatorUuid = creatorUuid;
        this.creatorName = creatorName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.dimension = dimension;
        this.creationTime = creationTime;
    }

    /** Retourne une copie de ce warp avec un nouveau nom (utilisé pour /renamewarp). */
    public Warp withName(String newName) {
        return new Warp(newName, creatorUuid, creatorName, x, y, z, yaw, pitch, dimension, creationTime);
    }

    public String getName() {
        return name;
    }

    public UUID getCreatorUuid() {
        return creatorUuid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getDimension() {
        return dimension;
    }

    public long getCreationTime() {
        return creationTime;
    }

    /** Date de création formatée de façon lisible (dd/MM/yyyy HH:mm). */
    public String getFormattedCreationDate() {
        return DATE_FORMAT.format(Instant.ofEpochMilli(creationTime));
    }
}
