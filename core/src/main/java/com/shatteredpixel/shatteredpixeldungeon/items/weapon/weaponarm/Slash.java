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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.weaponarm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Slash extends MeleeWeapon {

    {
        defaultAction = AC_MODE;

        image = ItemSpriteSheet.SLASH;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        weaponarm = true;
        RCH = 1;
        DLY = 1;

        tier=4;
    }

    public static final String AC_MODE  	= "MODE";
    public static final String REACH        = "REACH";
    public static final String DELAY        = "DELAY";
    public static final String CHANGE       = "CHANGE";
    private boolean change = false;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            actions.add("MODE");
        }
        return actions;
    }

    @Override
    public int max(int lvl) {
        return  5*(tier+1) +    //25 base, up from 15
                lvl*(tier+3);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_MODE) && isEquipped(hero)) {
            if (change) {
                change = false;
                DLY = 1.0f;
                RCH = 1;
            } else {
                change = true;
                DLY = 1.5f;
                RCH = 3;
            }
            updateQuickslot();
            curUser.spendAndNext(1.0f);
        }
    }

    @Override
    public String desc() {
        if (change) return Messages.get(this, "desc_mode");
        else return super.desc();
    }

    @Override
    public String status() {
        if (!isIdentified()) {
            return null;
        } else if (change) {
            return "LN";
        } else {
            return "ST";
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHANGE, change);
        bundle.put(REACH, RCH);
        bundle.put(DELAY, DLY);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        change = bundle.getBoolean(CHANGE);
        RCH = bundle.getInt(REACH);
        DLY = bundle.getFloat(DELAY);
    }

    @Override
    public float abilityChargeUse( Hero hero, Char target ) {
        if (hero.buff(Sword.CleaveTracker.class) != null){
            return 0;
        } else {
            return super.abilityChargeUse( hero, target );
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        Sword.cleaveAbility(hero, target, 1.05f, this);
    }

}
