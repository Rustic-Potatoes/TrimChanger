package net.rusticpotatoes.trimChanger.command;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.rusticpotatoes.trimChanger.TrimChanger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class TrimCommand {

    public static void register(Commands commands) {
        commands.register(
                Commands.literal("trim")
                        .then(Commands.literal("clear") // clears the held item of trims
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();

                                    if (!(sender instanceof Player player)) {
                                        TrimChanger.CHAT_SENDER.sendMessage(sender, "You must be a player to use this command");
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
                                    player.getInventory().setItemInMainHand(item);
                                    TrimChanger.CHAT_SENDER.sendMessage(player, "Trimmed armor cleared");

                                    return 1;
                                })
                        )
                        .then(Commands.literal("help") // shares info about the command
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();

                                    TrimChanger.CHAT_SENDER.sendMessage(sender, "trim clear: clears the armor of trims in your main hand");
                                    TrimChanger.CHAT_SENDER.sendMessage(sender, "trim help: shares this info");
                                    TrimChanger.CHAT_SENDER.sendMessage(sender, "trim query <player>: displays the armor and trim a player is wearing");

                                    return 1;
                                })
                        )

                        .then(Commands.literal("query") // displays the armor and trim a player is wearing
                                .then(Commands.argument("player", ArgumentTypes.player())
                                        .executes(context -> {
                                            CommandSender sender = context.getSource().getSender();


                                            PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);

                                            Player target = resolver.resolve(context.getSource()).getFirst();

                                            ItemStack[] items = target.getInventory().getArmorContents();

                                            TrimChanger.CHAT_SENDER.sendMessage(sender, target.displayName()
                                                    .append(Component.text(" is wearing: ")).color(NamedTextColor.GOLD)
                                            );

                                            for (ItemStack item : items) {

                                                if (item != null) {

                                                    ItemMeta meta = item.getItemMeta();

                                                    if (!(meta instanceof ArmorMeta armorMeta)) {
                                                        TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender,
                                                                Component.translatable(item.getType())
                                                        );
                                                    } else {

                                                        ArmorTrim trimData = armorMeta.getTrim();

                                                        if (trimData == null) {
                                                            TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender,
                                                                    Component.translatable(item.getType())
                                                                            .append(Component.text(": Not Trimmed")).color(NamedTextColor.GOLD)
                                                            );
                                                        } else {

                                                            TrimMaterial material = trimData.getMaterial();
                                                            TrimPattern pattern = trimData.getPattern();

                                                            TrimChanger.CHAT_SENDER.sendMessageWithoutPrefix(sender,
                                                                    Component.translatable(item.getType())
                                                                            .append(Component.text(": "))
                                                                            .append(material.description())
                                                                            .append(Component.text(", "))
                                                                            .append(pattern.description()).color(NamedTextColor.GOLD)
                                                            );
                                                        }
                                                    }
                                                }
                                            }

                                            return 1;
                                        })
                                )
                        )
                        .build()
        );
    }
}