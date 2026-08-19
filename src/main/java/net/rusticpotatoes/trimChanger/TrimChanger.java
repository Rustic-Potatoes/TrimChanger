package net.rusticpotatoes.trimChanger;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.rusticpotatoes.trimChanger.command.TrimCommand;
import net.rusticpotatoes.trimChanger.text.GradientFormatter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrimChanger extends JavaPlugin {

    public static final TextColor ORANGE = TextColor.color(0xB87500);
    public static final TextColor RED_ORANGE = TextColor.color(0xF04900);
//36, 144, 171
    private static final TextComponent TEXT_PREFIX = GradientFormatter.getGradient(RED_ORANGE, ORANGE, "[TRIM]:").decorate(TextDecoration.BOLD).appendSpace();

    @Override
    public void onEnable() {

        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> {
                    TrimCommand.register(event.registrar());//TextColor.color(36, 144, 171)
                }
        );
    }


    // TODO: move all send messages methods into a class

    private static TextComponent formatedText(String text) {
        return TEXT_PREFIX.append(Component.text(text, Style.style(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)));
    }

    public static void sendMessage(CommandSender destination, String text) {
        destination.sendMessage(formatedText(text));
    }
    public static void sendMessage(CommandSender destination, Component component) {
        destination.sendMessage(formatedText("").append(component.decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)));
    }
    public static void sendMessageWithoutPrefix(CommandSender destination, Component component) {
        destination.sendMessage(component);
    }
}
