package com.daan.spelunky3D;

import com.daan.spelunky3D.commands.TestChunkCommand;
import com.daan.spelunky3D.entitygen.MonsterSpawner;
import com.daan.spelunky3D.entitygen.PlayerSpawner;
import com.daan.spelunky3D.pathgen.DungeonGenerator;
import com.daan.spelunky3D.roomgen.ChunkBuilder;
import com.daan.spelunky3D.roomgen.DungeonWorldBuilder;
import com.daan.spelunky3D.roomgen.SchemLoader;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;

public final class Spelunky3D extends JavaPlugin {

    Clipboard clipboard;
    private SchemLoader schemLoader;
    private ChunkBuilder chunkBuilder;
    MonsterSpawner monsterSpawner;
    PlayerSpawner playerSpawner;

    @Override
    public void onEnable() {
        monsterSpawner = new MonsterSpawner();
        playerSpawner = new PlayerSpawner();

        schemLoader = new SchemLoader(this);
        schemLoader.loadAll();

        chunkBuilder = new ChunkBuilder(schemLoader, monsterSpawner, playerSpawner);

        getCommand("testchunk").setExecutor(new TestChunkCommand(chunkBuilder));

        getLogger().info("Spelunky3D enabled");
    }

    @Override
    public void onDisable() {
        getLogger().warning("Spelunky3D stopped!");
    }

    public void generateDungeon() {

        DungeonGenerator generator = new DungeonGenerator(this, 5, false, 9992490);

        generator.generateDungeon();

        World world = getServer().getWorlds().getFirst();

        DungeonWorldBuilder builder = new DungeonWorldBuilder(world, generator.getChunkManager(), chunkBuilder);

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