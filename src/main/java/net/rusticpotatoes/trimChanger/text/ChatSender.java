package net.rusticpotatoes.trimChanger.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.rusticpotatoes.trimChanger.TrimChanger;
import org.bukkit.command.CommandSender;


public class ChatSender {
    private final TextComponent prefix;
    private final TextComponent shortPrefix;
    private final TextColor defaultColor;

    public ChatSender(TextComponent preFix, TextComponent shortPrefix, TextColor defaultColor) {
        this.prefix = preFix;
        this.shortPrefix = shortPrefix;
        this.defaultColor = defaultColor;
    }

    private static void sendSimpleMessage(TextComponent prefix, CommandSender destination, Component message) {
        destination.sendMessage(prefix.append(message));
    }

    public void sendMessage(CommandSender destination, Component message, boolean useDefaultColor) {
        if ((useDefaultColor)) {
            sendSimpleMessage(this.prefix, destination, message.color(defaultColor));
        } else {
            sendSimpleMessage(this.prefix, destination, message);
        }
    }

    public void sendMessageWithShortPrefix(CommandSender destination, Component message, boolean useDefaultColor) {
        if ((useDefaultColor)) {
            sendSimpleMessage(shortPrefix, destination, message.color(defaultColor));
        } else {
            sendSimpleMessage(shortPrefix, destination, message);
        }
    }

    public void sendMessage(CommandSender destination, Component message) {
        sendMessage(destination, message, true);
    }

    public void sendMessage(CommandSender destination, String message) {
        sendMessage(destination, Component.text(message), true);
    }

    public void sendMessageWithShortPrefix(CommandSender destination, Component message) {
        sendMessageWithShortPrefix(destination, message, true);
    }


    public void sendMessageWithShortPrefix(CommandSender destination, String message) {
        sendMessageWithShortPrefix(destination, Component.text(message), true);

    }
}
