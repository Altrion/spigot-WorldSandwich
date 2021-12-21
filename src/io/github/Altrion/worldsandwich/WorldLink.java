package io.github.Altrion.worldsandwich;

import java.util.UUID;

public class WorldLink {

    public boolean TOP_groundTeleport;
        public boolean TOP_notOnFlight;
    public boolean TOP_disableFallDamage;

    public boolean BOT_groundTeleport;
        public boolean BOT_notOnFlight;
    public boolean BOT_disableFallDamage;
    public int TOP_FireRes;
    public int BOT_FireRes;
    public Integer _bottomExit=0;
    public Integer _topExit=256;
    public UUID _topWorld;
    public UUID _bottomWorld;

    public int worldTop_padding=0;
    public int worldBottom_padding=255;
}
