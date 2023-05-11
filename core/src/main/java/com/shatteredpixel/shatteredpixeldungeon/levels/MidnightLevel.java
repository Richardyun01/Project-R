package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class MidnightLevel extends Level {

    {
        color1 = 0x4b6636;
        color2 = 0xf2f2f2;
        viewDistance = 99;
    }

    private static int WIDTH = 15;
    private static int HEIGHT = 34;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CITY;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        return false;
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
    }

    @Override
    protected void createItems() {
    }

}
