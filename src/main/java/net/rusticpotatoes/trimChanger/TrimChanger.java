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

import java.util.List;

public final class TrimChanger extends JavaPlugin {

    public static final TextColor ORANGE = TextColor.color(0xB87500);
    public static final TextColor RED_ORANGE = TextColor.color(0xF04900);
    public static final TextComponent TEXT_PREFIX = Component.text()
            .append(GradientFormatter.getGradient(RED_ORANGE, ORANGE, "[TRIM]:").decorate(TextDecoration.BOLD))
            .appendSpace().build();

    public static final ChatSender CHAT_SENDER = new ChatSender(TEXT_PREFIX, NamedTextColor.GRAY);

    private static TrimChanger instance;

    // plugin information
    public final String name = this.getPluginMeta().getName();
    public final String version = this.getPluginMeta().getVersion();
    public final List<String> authors = this.getPluginMeta().getAuthors();

    @Override
    public void onEnable() {
        instance = this;

        // register trim command
        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> {
                    TrimCommand.register(event.registrar());
                }
        );
    }

    public static TrimChanger getInstance() {
        return instance;
    }
}
