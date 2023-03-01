package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class NoEnergy extends Buff {

    {
        type = buffType.NEGATIVE;

        announced = true;
    }

    public static final float DURATION = 2f;

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

}
