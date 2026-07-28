# TpSetWarp

Mod **Fabric** (Minecraft 1.21.4) ajoutant un système de warps simple, rapide et performant.

Ce projet a été porté depuis une version Forge 1.21.1. Le portage a été trivial car tout
le code métier (`warp/`, `storage/`, `command/`, `util/`, `teleport/`, `lang/`) est écrit
avec les **mappings officielles Mojang** (`ServerLevel`, `ServerPlayer`, `CommandSourceStack`,
`Component`, `ResourceLocation`...). Fabric Loom sait compiler directement contre ces mêmes
mappings (`mappings loom.officialMojangMappings()`), donc ces classes n'ont pas eu besoin
d'être réécrites. Seul `TpSetWarpMod.java` (point d'entrée) a été réécrit pour utiliser les
API Fabric à la place des API Forge :

| Forge | Fabric |
|---|---|
| `@Mod` + `FMLJavaModLoadingContext` | `ModInitializer` |
| `RegisterCommandsEvent` | `CommandRegistrationCallback.EVENT` |
| `ServerStartingEvent` | `ServerLifecycleEvents.SERVER_STARTING` |
| `ServerStoppingEvent` | `ServerLifecycleEvents.SERVER_STOPPING` |
| `META-INF/mods.toml` | `fabric.mod.json` |

## Build

```
./gradlew build
```

Le jar remappé se trouve ensuite dans `build/libs/`.

Le premier build télécharge Minecraft, les mappings officielles et Fabric API, ça peut
prendre plusieurs minutes.

Pour tester rapidement :
```
./gradlew runServer
```
(commande fournie par Loom, génère un dossier `run/` avec un serveur de test).

## ⚠️ Point à vérifier après le build

`TeleportHandler.java` appelle `ServerPlayer#teleportTo(ServerLevel, x, y, z, yaw, pitch)`.
Mojang a retravaillé une partie de l'API de téléportation autour de la 1.21.2 (introduction
de `TeleportTransition`). Si le build échoue précisément sur cette ligne, c'est le seul
endroit à corriger — comparer avec les sources déobfuscées générées par
`./gradlew genSources` pour retrouver la signature exacte disponible en 1.21.4.
Tout le reste du mod ne dépend pas de l'API sensible aux versions.

## Commandes

| Commande | Permission | Description |
|---|---|---|
| `/setwarp <nom>` | Op (niveau 2) | Crée un warp à la position du joueur |
| `/warp <nom>` | Tous | Téléporte vers un warp |
| `/delwarp <nom>` | Op (niveau 2) | Supprime un warp |
| `/warps` | Tous | Liste tous les warps |
| `/renamewarp <ancien> <nouveau>` | Op (niveau 2) | Renomme un warp |
| `/warpinfo <nom>` | Tous | Affiche les détails d'un warp |

## Stockage

Les warps sont sauvegardés dans `<monde>/tpsetwarp/warps.json`, écrit automatiquement
à chaque création/suppression/renommage (aucune perte au redémarrage).

## Porter le mod vers une nouvelle version de Minecraft

1. Mets à jour `minecraft_version`, `loader_version` et `fabric_version` dans
   `gradle.properties` (voir https://fabricmc.net/develop/ pour les versions compatibles).
2. Recompile — les seules erreurs attendues concernent généralement
   `TeleportHandler.java` (signature de `ServerPlayer#teleportTo`) si l'API a changé.
   C'est la seule classe volontairement "au contact" de l'API sensible aux versions.
3. Tout le reste (`warp/`, `storage/`, `command/`, `util/`) est indépendant de la
   version de Minecraft et ne devrait pas nécessiter de modification.
