# TpSetWarp

Mod Forge (Minecraft 1.21.1) ajoutant un système de warps simple, rapide et performant.

## ⚠️ Comment utiliser ce projet

Ce projet contient le **code source** et la config `build.gradle` du mod, mais **pas**
le wrapper Gradle binaire (`gradlew` + `gradle-wrapper.jar`) ni les fichiers du MDK Forge
officiel — ils n'ont pas pu être téléchargés depuis cet environnement (accès réseau
restreint aux dépôts Maven/Forge).

Pour lancer/builder le projet (ex: dans ton GitHub Codespaces habituel) :

1. Télécharge le **MDK Forge 1.21.1** officiel :
   https://files.minecraftforge.net/net/minecraftforge/forge/index_1.21.1.html
2. Extrais-le dans un dossier (il contient `gradlew`, `gradlew.bat`, `gradle/`, etc.)
3. **Remplace** dans ce MDK :
   - `build.gradle` → par celui fourni ici
   - `gradle.properties` → par celui fourni ici
   - `settings.gradle` → par celui fourni ici
   - `src/` → par celui fourni ici (copie tout le dossier `src/main`)
4. Lance `./gradlew build` (ou `./gradlew runServer` pour tester directement)

Le premier build télécharge Forge + les mappings, ça peut prendre plusieurs minutes.

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

1. Mets à jour `minecraft_version` et `forge_version` dans `gradle.properties`.
2. Recompile — les seules erreurs attendues concernent généralement
   `TeleportHandler.java` (signature de `ServerPlayer#teleportTo`) si l'API a changé.
   C'est la seule classe volontairement "au contact" de l'API sensible aux versions.
3. Tout le reste (`warp/`, `storage/`, `command/`, `util/`) est indépendant de la
   version de Minecraft et ne devrait pas nécessiter de modification.
