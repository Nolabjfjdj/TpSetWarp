package com.nolanlabanane.tpsetwarp.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nolanlabanane.tpsetwarp.TpSetWarpMod;
import com.nolanlabanane.tpsetwarp.lang.Language;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Responsable UNIQUEMENT de la lecture/écriture du fichier languages.json.
 * Même pattern que WarpStorage : aucune logique métier ici.
 */
public final class LanguageStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LANGUAGE_MAP_TYPE = new TypeToken<HashMap<UUID, Language>>() {}.getType();
    private static final String FILE_NAME = "languages.json";

    private final Path filePath;

    public LanguageStorage(Path worldDataFolder) {
        this.filePath = worldDataFolder.resolve(FILE_NAME);
    }

    public Map<UUID, Language> load() {
        if (!Files.exists(filePath)) {
            return new HashMap<>();
        }

        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            Map<UUID, Language> loaded = GSON.fromJson(reader, LANGUAGE_MAP_TYPE);
            return loaded != null ? loaded : new HashMap<>();
        } catch (IOException e) {
            TpSetWarpMod.LOGGER.error("Impossible de charger languages.json, une map vide sera utilisée.", e);
            return new HashMap<>();
        }
    }

    public void save(Map<UUID, Language> languages) {
        try {
            Files.createDirectories(filePath.getParent());
            try (Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                GSON.toJson(languages, LANGUAGE_MAP_TYPE, writer);
            }
        } catch (IOException e) {
            TpSetWarpMod.LOGGER.error("Impossible de sauvegarder languages.json !", e);
        }
    }
}
