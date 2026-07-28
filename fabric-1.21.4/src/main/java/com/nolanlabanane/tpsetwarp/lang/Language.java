package com.nolanlabanane.tpsetwarp.lang;

/**
 * Langues supportées par le mod. Anglais par défaut.
 */
public enum Language {
    EN,
    FR;

    /** Parse une entrée utilisateur ("fr", "en", peu importe la casse) vers une Language. */
    public static Language fromCode(String code) {
        if (code == null) {
            return EN;
        }
        return switch (code.toLowerCase()) {
            case "fr", "french", "français" -> FR;
            default -> EN;
        };
    }
}
