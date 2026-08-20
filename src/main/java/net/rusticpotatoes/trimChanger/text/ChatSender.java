package net.rusticpotatoes.trimChanger.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;


public class ChatSender {
    private final TextComponent prefix;
    private final TextColor defaultColor;

    public ChatSender(TextComponent preFix, TextColor defaultColor) {
        this.prefix = preFix;
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

    public void sendMessageWithoutPrefix(CommandSender destination, Component message, boolean useDefaultColor) {
        if ((useDefaultColor)) {
            sendSimpleMessage(Component.empty(), destination, message.color(defaultColor));
        } else {
            sendSimpleMessage(Component.empty(), destination, message);
        }
    }

    public void sendMessage(CommandSender destination, Component message) {
        sendMessage(destination, message, true);
    }

    public void sendMessage(CommandSender destination, String message) {
        sendMessage(destination, Component.text(message), true);
    }

    public void sendMessageWithoutPrefix(CommandSender destination, Component message) {
        sendMessageWithoutPrefix(destination, message, true);
    }


    public void sendMessageWithoutPrefix(CommandSender destination, String message) {
        sendMessageWithoutPrefix(destination, Component.text(message), true);

    }
}
