package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class DefenderBuff extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public int defenseCounter = 5 + Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);
    public int defenseLeft = 5;
    public int chargeCount = 0;
    public int maxCount = 8 - Dungeon.hero.pointsInTalent(Talent.SHOCKWAVE_ABSORB);

    private static final String COUNT	= "count";
    private static final String LEFT	= "left";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNT, defenseCounter );
        bundle.put( LEFT, defenseLeft );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        defenseCounter = bundle.getInt( COUNT );
        defenseLeft = bundle.getInt( LEFT );
    }

    public int getDefenseCounter() {
        return this.defenseCounter;
    }

    public int getDefenseLeft() {
        return this.defenseLeft;
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
    public void tintIcon(Image icon) {
        icon.hardlight(2f, 0.75f, 0f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, defenseLeft / defenseCounter);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", defenseCounter);
    }

}
