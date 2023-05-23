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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.weaponarm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BulletUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Vega;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Stinger extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.STINGER;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        type = FirearmType.FirearmEnergy2;
        max_round = 6;

        firearm = true;
        firearmEnergy = true;
        weaponarm = true;

        bullet_image = ItemSpriteSheet.NOTHING;
        bullet_sound = Assets.Sounds.ZAP;
    }

    public static final String AC_MODE  	= "MODE";
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
        if (loader != null) {
            reload_time = 3.5f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 3.5f * RingOfReload.reloadMultiplier(Dungeon.hero);
        }
    }

    @Override
    public void setMaxRound() {
        if (change) {
            max_round = 2;
        } else {
            max_round = 6 + 1 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE) + 1 * Dungeon.hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY);
        }
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        return 1.25f;
    }

    @Override
    public int Bulletmin(int lvl) {
        if (!change) {
            if (Dungeon.hero.buff(BulletUp.class) != null) {
                return (int) (((tier + 9) + lvl * 4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
            } else {
                return (int) (((tier + 9) + lvl * 4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY)));
            }
        } else {
            if (Dungeon.hero.buff(BulletUp.class) != null) {
                return (int) (tier)*3 + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
            } else {
                return (int) (tier)*3 + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            }
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (!change) {
            if (Dungeon.hero.buff(BulletUp.class) != null) {
                return (int) (((tier + 13) + lvl * 4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
            } else {
                return (int) (((tier + 13) + lvl * 4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY)));
            }
        } else {
            if (Dungeon.hero.buff(BulletUp.class) != null) {
                return (tier+5)*3 + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
            } else {
                return (tier+5)*3 + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            }
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_MODE) && isEquipped(hero)) {
            if (change) {
                change = false;
            } else {
                change = true;
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
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        change = bundle.getBoolean(CHANGE);
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Vega.shootAbility(hero, this);
    }

}
