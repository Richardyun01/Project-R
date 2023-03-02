package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class NoEnergy extends Buff {

    {
        type = buffType.NEGATIVE;

        announced = true;
    }

    private int level = 0;
    private int interval = 1;

    public static final float DURATION = 1f;

    @Override
    public int icon() {
        return BuffIndicator.NOENERGY;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1, 0, 0);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

}
