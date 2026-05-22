package com.example.volcanoevent;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VolcanoCommand implements CommandExecutor {
    private final VolcanoEvent plugin;
    private final VolcanoManager manager;

    public VolcanoCommand(VolcanoEvent plugin, VolcanoManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("vulcan.admin")) {
            player.sendMessage(ChatColor.RED + "У вас нет прав для этой команды!");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.YELLOW + "Использование: /vulcan add <обычный|редкий|эпический|легендарный>");
                    return true;
                }
                manager.openRarityGUI(player, args[1]);
                break;
            case "start":
                manager.startEvent();
                player.sendMessage(ChatColor.GREEN + "Ивент 'Вулкан' запущен!");
                break;
            case "stop":
                manager.stopEvent();
                player.sendMessage(ChatColor.GOLD + "Ивент 'Вулкан' остановлен!");
                break;
            case "reload":
                plugin.reloadConfig();
                player.sendMessage(ChatColor.BLUE + "Конфигурация перезагружена!");
                break;
            default:
                showHelp(player);
                break;
        }
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage(ChatColor.DARK_AQUA + "=== Вулкан — помощь ===");
        player.sendMessage(ChatColor.AQUA + "/vulcan add <редкость> — добавить предметы для редкости");
        player.sendMessage(ChatColor.AQUA + "/vulcan start — запустить ивент");
        player.sendMessage(ChatColor.AQUA + "/vulcan stop — остановить ивент");
        player.sendMessage(ChatColor.AQUA + "/vulcan reload — перезагрузить конфигурацию");
    }
}
