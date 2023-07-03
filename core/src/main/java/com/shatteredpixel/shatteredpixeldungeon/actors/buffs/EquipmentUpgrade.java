/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class EquipmentUpgrade extends FlavourBuff {

    public static final float DURATION = 30f;

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)){
            Item.updateQuickslot();
            if (target == Dungeon.hero) ((Hero) target).updateHT(false);
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        if (target == Dungeon.hero) ((Hero) target).updateHT(false);
        Item.updateQuickslot();
    }

    //called in Item.buffedLevel()
    public static int increaseLevel( int level ){
        // This means that levels 1/2/3/4/5/6/7/8/9/...
        // Are now instead:       2/3/4/5/6/7/8/9/10/...
        if (level < 0) {
            return level;
        } else {
            return level+1;
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.UPGRADE;
    }

    @Override
    public float iconFadePercent() {
        return (DURATION - visualcooldown())/DURATION;
    }

}
