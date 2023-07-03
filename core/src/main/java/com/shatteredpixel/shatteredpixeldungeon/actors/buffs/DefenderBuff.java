package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class DefenderBuff extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public int defenseCounter = 5 + Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);
    public int defenseLeft = 5 + Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);
    public int chargeCount = 0;
    public int maxCount = 8 - Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);

    private static final String COUNT	= "count";
    private static final String LEFT	= "left";
    private static final String CHARGE	= "charge";
    private static final String MAX 	= "max";

    public int getDefenseCounter() {
        return this.defenseCounter + Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);
    }

    public int getDefenseLeft() {
        return this.defenseLeft + Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);
    }

    public int getMaxCount() {
        return this.maxCount - Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);
    }

    public void countAddition() {
        if (chargeCount >= maxCount && defenseCounter > defenseLeft) {
            defenseLeft++;
            chargeCount = 0;
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.DEFENDER_BARRIER;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, defenseLeft / defenseCounter);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", defenseLeft, defenseCounter, chargeCount, maxCount);
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNT, defenseCounter );
        bundle.put( LEFT, defenseLeft );
        bundle.put( CHARGE, chargeCount );
        bundle.put( MAX, maxCount );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        defenseCounter = bundle.getInt( COUNT );
        defenseLeft = bundle.getInt( LEFT );
        chargeCount = bundle.getInt( CHARGE );
        maxCount = bundle.getInt( MAX );
    }

}
