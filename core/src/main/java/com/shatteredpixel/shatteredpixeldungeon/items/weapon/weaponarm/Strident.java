package com.shatteredpixel.shatteredpixeldungeon.items.weapon.weaponarm;

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.ShortCarbine;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Strident extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.STRIDENT;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 3;
        type = FirearmType.FirearmAuto;
        max_round = 12;
        shot = 4;

        firearm = true;
        firearmAuto = true;
        weaponarm = true;

        bullet_image = ItemSpriteSheet.DUAL_BULLET;
    }

    public static final String AC_MODE  	= "MODE";
    public static final String SHOT         = "SHOT";
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
    public void setReloadTime() {
        if (change) {
            if (loader != null) {
                reload_time = 4f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
            } else {
                reload_time = 4f * RingOfReload.reloadMultiplier(Dungeon.hero);
            }
        } else {
            if (loader != null) {
                reload_time = 2f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
            } else {
                reload_time = 2f * RingOfReload.reloadMultiplier(Dungeon.hero);
            }
        }
    }

    @Override
    public void setMaxRound() {
        max_round = 12 + 4 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE) + 4 * Dungeon.hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY);
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (change) {
            if (Dungeon.level.distance(owner.pos, target.pos) <= 2) {
                return 0.8f;
            } else if (Dungeon.level.distance(owner.pos, target.pos) > 2 && Dungeon.level.distance(owner.pos, target.pos) <= 7) {
                return 0.5f;
            } else {
                return 0.2f;
            }
        } else {
            if (Dungeon.level.distance(owner.pos, target.pos) <= 2) {
                return 1.25f;
            } else if (Dungeon.level.distance(owner.pos, target.pos) > 2 && Dungeon.level.distance(owner.pos, target.pos) <= 7) {
                return 1f;
            } else {
                return 0.8f;
            }
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_MODE) && isEquipped(hero)) {
            if (change) {
                change = false;
                this.shot = 4;
            } else {
                change = true;
                this.shot = 12 + 4 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE) + 4 * Dungeon.hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY);
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
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHANGE, change);
        bundle.put(SHOT, shot);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        change = bundle.getBoolean(CHANGE);
        shot = bundle.getInt(SHOT);
    }

    @Override
    public float abilityChargeUse( Hero hero ) { return 0; }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        ShortCarbine.shootAbility(hero, this);
    }

}

