package net.rusticpotatoes.trimChanger.command;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.rusticpotatoes.trimChanger.TrimChanger;
import net.rusticpotatoes.trimChanger.config.TrimConfig;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

import java.util.Arrays;

public class TrimCommand {

    public static void register(Commands commands) {
        var trimRoot = Commands.literal("trim").requires(context -> TrimConfig.ALLOW_TRIM_ROOT_KEY.get());

        var clear = Commands.literal("clear") // clears the held item of trims
                .requires(context -> TrimConfig.ALLOW_CLEAR_KEY.get())
                .executes(context -> {

                    Entity executor = context.getSource().getExecutor();

                    if (executor == null) {
                        TrimChanger.CHAT_SENDER.sendMessage(Bukkit.getConsoleSender(), "You must be a player to use this command");
                        return 0;
                    }

                    if (!(executor instanceof Player player)) {
                        TrimChanger.CHAT_SENDER.sendMessage(executor, "You must be a player to use this command");
                        return 0;
                    }

                    ItemStack item = player.getInventory().getItemInMainHand();

                    if (item.isEmpty()) {
                        TrimChanger.CHAT_SENDER.sendMessage(player, "No item held");
                        return 0;
                    }

                    ItemMeta meta = item.getItemMeta();

                    if (!(meta instanceof ArmorMeta armorMeta)) {
                        TrimChanger.CHAT_SENDER.sendMessage(player, "You are not holding any armor");
                        return 0;
                    }

                    if (!armorMeta.hasTrim()) {
                        TrimChanger.CHAT_SENDER.sendMessage(player, "That armor doesn't have a trim");
                        return 0;
                    }

                    armorMeta.setTrim(null); // clears the armor trim
                    item.setItemMeta(armorMeta);

                    if (TrimConfig.PARTICLE_CLEAR_KEY.get()) {
                        player.playEffect(EntityEffect.TELEPORT_ENDER);
                    }
                    player.getInventory().setItemInMainHand(item);
                    TrimChanger.CHAT_SENDER.sendMessage(player, "Trimmed armor cleared");

                    return 1;
                });

        var help = Commands.literal("help") // shares info about the command
                .requires(context -> TrimConfig.ALLOW_HELP_KEY.get())
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();

                    TrimChanger.CHAT_SENDER.sendMessage(sender, "Usable commands:");

                    if (TrimConfig.ALLOW_CLEAR_KEY.get()) {
                        TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "trim clear: clears the armor of trims in your main hand");
                    }
                    TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "trim help: shares this info"); // doesn't need to check if the command is usable because its this command
                    if (TrimConfig.ALLOW_ABOUT_KEY.get()) {
                        TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "trim help about: shares info about the plugin");
                    }
                    if (TrimConfig.ALLOW_QUERY_KEY.get()) {
                        TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "trim query <player>: displays the armor and trim a player is wearing");
                    }
                    if (TrimConfig.OPERATOR_RELOAD_KEY.get() && sender.isOp() || sender instanceof ConsoleCommandSender) {
                        TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "trim reload: reloads config values from file");
                    }

                    return 1;
                });

        var about = Commands.literal("about") // shares info about the plugin
                .requires(context -> TrimConfig.ALLOW_ABOUT_KEY.get())
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();

                    TrimChanger.CHAT_SENDER.sendMessage(sender, "Plugin Name: " + TrimChanger.getInstance().name);
                    TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "Version: " + TrimChanger.getInstance().version);
                    TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "Authors: " + TrimChanger.getInstance().authors);
                    TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender, "Description: " + TrimChanger.getInstance().description);


                    return 1;
                });

        var query = Commands.literal("query") // displays the armor and trim a player is wearing
                .requires(context -> TrimConfig.ALLOW_QUERY_KEY.get())
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(context -> {

                            CommandSender sender = context.getSource().getSender();

                            PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player target = resolver.resolve(context.getSource()).getFirst();

                            ItemStack[] armorContents = target.getInventory().getArmorContents();

                            if (Arrays.stream(armorContents).allMatch(item -> item == null || item == ItemStack.empty())) {
                                TrimChanger.CHAT_SENDER.sendMessage(sender, target.displayName()
                                        .append(Component.text(" is wearing no armor"))
                                );
                                return 1;
                            }

                            TrimChanger.CHAT_SENDER.sendMessage(sender, target.displayName()
                                    .append(Component.text(" is wearing: "))
                            );

                            // loop through all armor slots
                            for (ItemStack item : armorContents) {

                                if (item != null) { // if not wearing any armor in that slot, ignore

                                    ItemMeta meta = item.getItemMeta();

                                    if (!(meta instanceof ArmorMeta armorMeta)) { // if not wearing armor but a wearable item
                                        TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender,
                                                Component.translatable(item.getType())
                                        );
                                    } else {

                                        ArmorTrim trimData = armorMeta.getTrim();

                                        if (trimData == null) { // armor is not trimmed
                                            TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender,
                                                    Component.translatable(item.getType())
                                                            .append(Component.text(": Not Trimmed"))
                                            );
                                        } else { // armor is trimmed
                                            TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender,
                                                    Component.translatable(item.getType())
                                                            .append(Component.text(": "))
                                                            .append(trimData.getPattern().description().color(trimData.getMaterial().description().color())
                                                                    .append(Component.text(", ")))
                                                            .append(trimData.getMaterial().description())
                                            );
                                        }
                                    }
                                }
                            }
                            return 1;
                        })
                );

        var reload = Commands.literal("reload") // reload config values
                .requires(context -> TrimConfig.OPERATOR_RELOAD_KEY.get() && context.getSender().isOp() || context.getSender() instanceof ConsoleCommandSender)
                .executes(context -> {
                    TrimChanger.getInstance().updateConfig();
                    TrimChanger.CHAT_SENDER.sendMessage(context.getSource().getSender(), "Reloaded TrimChanger Config");
                    return 1;
                });

        // command registration
        commands.register(
                trimRoot
                        .then(clear)
                        .then(help.then(about))
                        .then(query)
                        .then(reload)
                        .build()
        );
    }
}