package com.daan.spelunky3D;

import com.daan.spelunky3D.pathgen.DungeonGenerator;
import com.daan.spelunky3D.roomgen.DungeonWorldBuilder;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spelunky3D extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().warning("Spelunky3D started!");
    }

    @Override
    public void onDisable() {
        getLogger().warning("Spelunky3D stopped!");
    }

    public void generateDungeon() {

        DungeonGenerator generator = new DungeonGenerator(this, 5, false, 0);

        generator.generateDungeon();

        World world = getServer().getWorlds().getFirst();

        DungeonWorldBuilder builder = new DungeonWorldBuilder(
                world,
                generator.getChunkManager()
        );

        builder.generate();

        getLogger().info("Dungeon fully generated in world!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("gendungeon")) {

            sender.sendMessage("§aGenerating dungeon...");

            getServer().getScheduler().runTask(this, this::generateDungeon);

            sender.sendMessage("§aDungeon generated!");

            return true;
        }

        return false;
    }
}