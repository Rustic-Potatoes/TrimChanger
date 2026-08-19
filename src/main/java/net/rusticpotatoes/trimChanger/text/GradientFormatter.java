package net.rusticpotatoes.trimChanger.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class GradientFormatter {

    public static TextComponent getGradient(TextColor color1, TextColor color2, String text) {

        if (text.length() <= 1) { // if length is one or less, return just color1
            return Component.text(text, color1);
        }

        TextComponent.Builder builder = Component.text();

        for (int index = 0; index < text.length(); index++) { // loop through all char in text and gives them a lerped color

            float ratio = (float) index / text.length();

            builder.append(Component.text(text.charAt(index)).color(TextColor.lerp(ratio, color1, color2)));
        }

        return builder.build();
    }
}
