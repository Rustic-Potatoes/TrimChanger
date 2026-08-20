package net.rusticpotatoes.trimChanger;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.rusticpotatoes.trimChanger.command.TrimCommand;
import net.rusticpotatoes.trimChanger.text.ChatSender;
import net.rusticpotatoes.trimChanger.text.GradientFormatter;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrimChanger extends JavaPlugin {

    public static final TextColor ORANGE = TextColor.color(0xB87500);
    public static final TextColor RED_ORANGE = TextColor.color(0xF04900);
    public static final TextComponent TEXT_PREFIX = Component.text()
            .append(GradientFormatter.getGradient(RED_ORANGE, ORANGE, "[TRIM]:").decorate(TextDecoration.BOLD))
            .appendSpace().build();

    public static final ChatSender CHAT_SENDER = new ChatSender(TEXT_PREFIX, NamedTextColor.GRAY);

    @Override
    public void onEnable() {

        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> {
                    TrimCommand.register(event.registrar());//TextColor.color(36, 144, 171)
                }
        );
    }
}
