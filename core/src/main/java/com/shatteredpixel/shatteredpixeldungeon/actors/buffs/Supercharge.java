package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Supercharge extends Buff{

    {
        type = buffType.POSITIVE;
    }

    private int count = 0;
    private float superchargeTime = 0f;
    private float initialsuperchargeTime = 5f;

    @Override
    public int icon() {
        return BuffIndicator.SUPERCHARGE;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (initialsuperchargeTime - superchargeTime)/ initialsuperchargeTime);
    }



}
