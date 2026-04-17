package com.daan.spelunky3D.commands;

import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.daan.spelunky3D.roomgen.ChunkBuilder;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestChunkCommand implements CommandExecutor {

    private final ChunkBuilder builder;

    public TestChunkCommand(ChunkBuilder builder) {
        this.builder = builder;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;
        player.sendMessage("testchunk executed");
        Chunk chunk = new Chunk(new Vector3Int(0,0,0));
        chunk.setChunkType(ChunkTypeEnum.START);

        chunk.getOpeningDirections().add(DirectionEnum.EAST);
        chunk.getOpeningDirections().add(DirectionEnum.WEST);

        Vector3Int vector3Int = new Vector3Int(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ());

        builder.buildChunk(vector3Int, player.getWorld(), chunk);

        return true;
    }
}