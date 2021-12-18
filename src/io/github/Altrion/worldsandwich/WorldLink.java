package io.github.Altrion.worldsandwich;

import org.bukkit.World;

import java.util.UUID;

public class WorldLink {
    public Integer _bottomExit;
    public Integer _topExit;
    public UUID _topWorld;
    public UUID _bottomWorld;

    public int worldTop_padding;
    public int worldBottom_padding;
    public WorldLink() {}
    public WorldLink(Integer bottomExit, Integer topExit, UUID topWorld, UUID bottomWorld) {
        _bottomExit=bottomExit;
        _topExit=topExit;
        _topWorld=topWorld;
        _bottomWorld=bottomWorld;
    }

}
