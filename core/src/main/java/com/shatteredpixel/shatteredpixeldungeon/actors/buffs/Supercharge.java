package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.text.DecimalFormat;

public class Supercharge extends Buff{

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    float damageFactor = 1;
    float maxDamage;
    float minDamage;

    public int turnCount = 0;

    private float superchargeTime = 0f;
    private float initialsuperchargeTime = 5f;

    public int pos = -1;

    @Override
    public boolean act() {
        if (pos != target.pos) {
            pos = target.pos;
            if (--turnCount < 0) turnCount = 0;
        } else {
            spend(TICK);
            if (turnCount < 5) turnCount++;
        }
        return true;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (initialsuperchargeTime - superchargeTime) / initialsuperchargeTime);
    }

    public void setDamageFactor(float lvl) {
        damageFactor += (75f + 25f*lvl)/5;

        if (damageFactor > maxDamage) {
            damageFactor = maxDamage;
        }
        if (damageFactor > minDamage) {
            damageFactor = minDamage;
        }
    }

    public float getDamageFactor() {
        return damageFactor;
    }

    @Override
    public int icon() {
        return BuffIndicator.SUPERCHARGE;
    }

    public float duration() {
        return visualcooldown();
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", new DecimalFormat("#.##").format(turnCount));
    }

    private static final String DAMAGE_FACTOR = "damageFactor";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DAMAGE_FACTOR, damageFactor);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bundle.put(DAMAGE_FACTOR, damageFactor);
    }

}
