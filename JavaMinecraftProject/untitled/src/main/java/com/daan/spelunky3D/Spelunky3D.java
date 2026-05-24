package com.daan.spelunky3D;

import com.daan.spelunky3D.commands.AddPointsCommand;
import com.daan.spelunky3D.commands.ResetPointsCommand;
import com.daan.spelunky3D.commands.TestChunkCommand;
import com.daan.spelunky3D.commands.TestShopCommand;
import com.daan.spelunky3D.entitygen.MonsterSpawner;
import com.daan.spelunky3D.entitygen.PlayerSpawner;
import com.daan.spelunky3D.pathgen.DungeonGenerator;
import com.daan.spelunky3D.roomgen.ChunkBuilder;
import com.daan.spelunky3D.roomgen.DungeonWorldBuilder;
import com.daan.spelunky3D.roomgen.SchemLoader;
import com.daan.spelunky3D.shopkeeper.shop.ShopListener;
import com.daan.spelunky3D.shopkeeper.shop.ShopManager;
import com.daan.spelunky3D.shopkeeper.economy.PlayerConnectionListener;
import com.daan.spelunky3D.shopkeeper.economy.PointBarManager;
import com.daan.spelunky3D.shopkeeper.economy.PointListener;
import com.daan.spelunky3D.shopkeeper.economy.PointManager;
import com.daan.spelunky3D.shopkeeper.shop.ShopSpawner;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spelunky3D extends JavaPlugin {

    private SchemLoader schemLoader;
    private ChunkBuilder chunkBuilder;

    private MonsterSpawner monsterSpawner;
    private PlayerSpawner playerSpawner;
    private ShopSpawner shopSpawner;
    private ShopManager shopManager;

    private PointManager pointManager;
    private PointBarManager pointBarManager;

    @Override
    public void onEnable() {

        initializeEconomy();

        initializeShopSystem();

        initializeGenerators();

        registerCommands();

        registerListeners();

        getLogger().info("Spelunky3D enabled");
    }

    @Override
    public void onDisable() {

        getLogger().warning("Spelunky3D stopped!");
    }

    private void initializeGenerators() {

        monsterSpawner = new MonsterSpawner();
        playerSpawner = new PlayerSpawner();

        schemLoader = new SchemLoader(this);
        schemLoader.loadAll();
        chunkBuilder = new ChunkBuilder(
                schemLoader,
                monsterSpawner,
                playerSpawner,
                shopSpawner
        );
    }

    private void initializeEconomy() {

        pointManager = new PointManager();

        pointBarManager = new PointBarManager(pointManager);
    }

    private void initializeShopSystem() {

        shopManager = new ShopManager(
                pointManager,
                pointBarManager
        );

        shopSpawner = new ShopSpawner(shopManager);
    }
    private void registerCommands() {

        getCommand("testchunk").setExecutor(
                new TestChunkCommand(chunkBuilder)
        );

        getCommand("testvillager").setExecutor(
                new TestShopCommand(shopManager)
        );

        getCommand("addpoints").setExecutor(
                new AddPointsCommand(pointManager, pointBarManager)
        );

        getCommand("resetpoints").setExecutor(
                new ResetPointsCommand(pointManager, pointBarManager)
        );
    }

    private void registerListeners() {

        getServer().getPluginManager().registerEvents(
                new ShopListener(shopManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new PointListener(pointManager, pointBarManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new PlayerConnectionListener(pointBarManager),
                this
        );
    }

    public void generateDungeon() {

        DungeonGenerator generator = new DungeonGenerator(
                this,
                5,
                false,
                9169522
        );

        generator.generateDungeon();

        World world = getServer().getWorlds().getFirst();

        DungeonWorldBuilder builder = new DungeonWorldBuilder(
                world,
                generator.getChunkManager(),
                chunkBuilder
        );

        builder.generate();

        getLogger().info("Dungeon fully generated in world!");
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (command.getName().equalsIgnoreCase("gendungeon")) {

            sender.sendMessage("§aGenerating dungeon...");

            getServer().getScheduler().runTask(
                    this,
                    this::generateDungeon
            );

            sender.sendMessage("§aDungeon generated!");

            return true;
        }

        return false;
    }
}