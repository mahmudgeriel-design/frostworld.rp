package com.example.volcanoevent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class VolcanoListener implements Listener {
    private final VolcanoEvent plugin;

    public VolcanoListener(VolcanoEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();

        // Получаем редкость предмета (предполагаем, что она закодирована в названии)
        String rarity = getRarityFromItem(item);

        // Отправляем игроку сообщение в зависимости от редкости
        switch (rarity) {
            case "обычный":
                player.sendMessage(ChatColor.GRAY + "Вы подобрали обычный ресурс!");
                break;
            case "редкий":
                player.sendMessage(ChatColor.BLUE + "Поздравляем! Вы нашли редкий ресурс!");
                break;
            case "эпический":
                player.sendMessage(ChatColor.PURPLE + "Невероятная удача! Вы нашли эпический ресурс!");
                break;
            case "легендарный":
                Bukkit.broadcastMessage(ChatColor.GOLD + "🎉 Игрок " + player.getName() +
                    " нашёл ЛЕГЕНДАРНЫЙ ресурс! 🎉");
                break;
            default:
                // Если редкость не определена, просто сообщаем о подборе
                player.sendMessage(ChatColor.WHITE + "Вы подобрали предмет.");
                break;
        }
    }

    /**
     * Определяет редкость предмета по его названию.
     * Предполагается, что в названии предмета содержится метка редкости.
     */
    private String getRarityFromItem(ItemStack item) {
        if (item.getItemMeta() == null || !item.getItemMeta().hasDisplayName()) {
            return "неизвестный";
        }

        String displayName = item.getItemMeta().getDisplayName().toLowerCase();

        if (displayName.contains("обычный") || displayName.contains("common")) {
            return "обычный";
        } else if (displayName.contains("редкий") || displayName.contains("rare")) {
            return "редкий";
        } else if (displayName.contains("эпический") || displayName.contains("epic")) {
            return "эпический";
        } else if (displayName.contains("легендарный") || displayName.contains("legendary")) {
            return "легендарный";
        } else {
            return "неизвестный";
        }
    }
}
