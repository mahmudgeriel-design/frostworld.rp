package com.example.volcanoevent;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class VolcanoManager {
    private final VolcanoEvent plugin;
    private boolean eventActive = false;
    private Location volcanoLocation;
    private final Map<String, List<ItemStack>> rarityItems = new HashMap<>();
    private final Random random = new Random();
    private List<Location> volcanoBlocks = new ArrayList<>();

    public VolcanoManager(VolcanoEvent plugin) {
        this.plugin = plugin;
        rarityItems.put("обычный", new ArrayList<>());
        rarityItems.put("редкий", new ArrayList<>());
        rarityItems.put("эпический", new ArrayList<>());
        rarityItems.put("легендарный", new ArrayList<>());
    }

    public void openRarityGUI(Player player, String rarity) {
        Inventory gui = Bukkit.createInventory(player, 27, "Добавить предметы для: " + rarity);
        player.openInventory(gui);
    }

    public void startEvent() {
        if (eventActive) return;

        spawnRandomVolcano();
        eventActive = true;

        Bukkit.broadcastMessage(ChatColor.RED + "🔥 ВУЛКАН ПРОСНУЛСЯ! 🔥");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Ловите ценные ресурсы из извержения!");

        plugin.getServer().getScheduler().runTaskLater(plugin, this::startEruption, 600L); // Через 30 сек
        plugin.getServer().getScheduler().runTaskLater(plugin, this::stopEvent, 6000L); // Через 5 мин
    }

    private void spawnRandomVolcano() {
        World world = Bukkit.getWorld("world");
        int x = random.nextInt(1000) - 500;
        int z = random.nextInt(1000) - 500;
        int y = world.getHighestBlockYAt(x, z) + 1;

        volcanoLocation = new Location(world, x, y, z);

        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = -5; dz <= 5; dz++) {
                double distance = Math.sqrt(dx * dx + dz * dz);
                if (distance <= 5) {
                    int height = (int) (5 - distance);
                    for (int dy = 0; dy <= height; dy++) {
                Location blockLoc = volcanoLocation.clone().add(dx, dy, dz);
                if (dy == height) {
                    blockLoc.getBlock().setType(Material.OBSIDIAN);
                } else {
            blockLoc.getBlock().setType(Material.STONE);
                }
                volcanoBlocks.add(blockLoc);
            }
        }
    }
}

        volcanoLocation.add(0, 6, 0).getBlock().setType(Material.LAVA);
        volcanoBlocks.add(volcanoLocation.clone().add(0, 6, 0));
    }

    private void startEruption() {
        if (!eventActive) return;

        Bukkit.broadcastMessage(ChatColor.DARK_RED + "💥 ВУЛКАН ИЗВЕРГАЕТСЯ! 💥");
        spawnLavaParticles();
        plugin.getServer().getScheduler().runTaskLater(plugin, this::spawnItemsFromEruption, 100L);
    }

    private void spawnLavaParticles() {
        for (int i = 0; i < 50; i++) {
            Location particleLoc = volcanoLocation.clone().add(
                random.nextDouble() * 10 - 5,
                random.nextDouble() * 5,
                random.nextDouble() * 10 - 5
            );
            volcanoLocation.getWorld().spawnParticle(Particle.LAVA, particleLoc, 1);
        }
    }

    private void spawnItemsFromEruption() {
        int totalItems = random.nextInt(20) + 10;

        for (int i = 0; i < totalItems; i++) {
            String rarity = getRandomRarity();
            List<ItemStack> items = rarityItems.get(rarity);
            if (items.isEmpty()) continue;

            ItemStack item = items.get(random.nextInt(items.size())).clone();
            Location dropLoc = volcanoLocation.clone().add(
                random.nextDouble() * 20 - 10,
                random.nextDouble() * 15 + 5,
                random.nextDouble() * 20 - 10
            );

            Item droppedItem = volcanoLocation.getWorld().dropItem(dropLoc, item);
            droppedItem.setPickupDelay(10);

            // Подсветка предметов в зависимости от редкости
            Particle particleType;
            switch (rarity) {
                case "обычный":
                    particleType = Particle.VILLAGER_SPELL;
                    break;
                case "редкий":
                    particleType = Particle.WITCH_SPELL;
                    break;
                case "эпический":
                    particleType = Particle.ENCHANTMENT_TABLE;
                    break;
                case "легендарный":
                    particleType = Particle.FLAME;
                    break;
                default:
                    particleType = Particle.SMOKE_NORMAL;
                    break;
            }

            // Запускаем частицы вокруг предмета
            plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                if (!droppedItem.isDead()) {
                    Location itemLoc = droppedItem.getLocation().add(0, 0.5, 0);
            volcanoLocation.getWorld().spawnParticle(
                particleType,
                itemLoc,
                3,
                0.3, 0.3, 0.3,
                0.05
            );
        } else {
            // Останавливаем частицы, если предмет подобран
            plugin.getServer().getScheduler().cancelTask(this.getTaskId());
        }
            }, 0L, 20L); // Каждые 20 тиков (1 секунда)
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "Вулкан изверг ценные ресурсы! Ловите предметы!");
    }

    private String getRandomRarity() {
        int roll = random.nextInt(100);
        if (roll < 80) return "обычный";
        else if (roll < 95) return "редкий";
        else if (roll < 99) return "эпический";
        else return "легендарный";
    }

    public void stopEvent() {
        if (!eventActive) return;

        // Удаляем все блоки вулкана
        for (Location blockLoc : volcanoBlocks) {
            blockLoc.getBlock().setType(Material.AIR);
        }
        volcanoBlocks.clear();

        Bukkit.broadcastMessage(ChatColor.RED + "🔥 ВУЛКАН ЗАТИХ! 🔥");

        eventActive = false;
    }
}
