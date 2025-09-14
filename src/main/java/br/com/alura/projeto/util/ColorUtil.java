package br.com.alura.projeto.util;

public final class ColorUtil {

    @Deprecated
    private ColorUtil() {}

    public static String getFontColorForHex(String colorHex) {
        if (colorHex == null || !colorHex.startsWith("#") || colorHex.length() < 7) {
            return "#000000";
        }

        try {
            int r = Integer.parseInt(colorHex.substring(1, 3), 16);
            int g = Integer.parseInt(colorHex.substring(3, 5), 16);
            int b = Integer.parseInt(colorHex.substring(5, 7), 16);
            double luminance = (0.299 * r + 0.587 * g + 0.114 * b);

            return (luminance > 149) ? "#000000" : "#FFFFFF";
        } catch (NumberFormatException e) {
            return "#000000";
        }
    }
}