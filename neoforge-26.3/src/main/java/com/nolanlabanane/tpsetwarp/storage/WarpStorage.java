package com.nolanlabanane.tpsetwarp.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nolanlabanane.tpsetwarp.TpSetWarpMod;
import com.nolanlabanane.tpsetwarp.warp.Warp;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Responsable UNIQUEMENT de la lecture/écriture du fichier warps.json.
 * Ne connaît rien à la logique métier (WarpManager) : reçoit et retourne
 * de simples Map<String, Warp>.
 */
public final class WarpStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type WARP_MAP_TYPE = new TypeToken<LinkedHashMap<String, Warp>>() {}.getType();
    private static final String FILE_NAME = "warps.json";

    private final Path filePath;

    /**
     * @param worldDataFolder dossier de données du monde (ex: run/world/tpsetwarp/)
     */
    public WarpStorage(Path worldDataFolder) {
        this.filePath = worldDataFolder.resolve(FILE_NAME);
    }

    /** Charge les warps depuis le disque. Retourne une map vide si le fichier n'existe pas encore. */
    public Map<String, Warp> load() {
        if (!Files.exists(filePath)) {
            return new LinkedHashMap<>();
        }

        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            Map<String, Warp> loaded = GSON.fromJson(reader, WARP_MAP_TYPE);
            return loaded != null ? loaded : new LinkedHashMap<>();
        } catch (IOException e) {
            TpSetWarpMod.LOGGER.error("Impossible de charger warps.json, un fichier vide sera utilisé.", e);
            return new LinkedHashMap<>();
        }
    }

    /** Sauvegarde la map de warps sur le disque de façon atomique-safe (écriture directe + flush). */
    public void save(Map<String, Warp> warps) {
        try {
            Files.createDirectories(filePath.getParent());
            try (Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                GSON.toJson(warps, WARP_MAP_TYPE, writer);
            }
        } catch (IOException e) {
            TpSetWarpMod.LOGGER.error("Impossible de sauvegarder warps.json !", e);
        }
    }
}
