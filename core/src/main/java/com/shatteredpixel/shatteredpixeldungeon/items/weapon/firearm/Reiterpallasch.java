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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BulletUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Reiterpallasch extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.REITERPALLASCH;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        type = FirearmType.FirearmPistol;
        max_round = 3;
    }

    public static boolean change = false;

    @Override
    public int STRReq(int lvl) {                    //18 base strength req, down from 19
        if (hero.heroClass == HeroClass.NOISE) {
            return STRReq(tier, lvl);
        } else {
            return STRReq(tier, lvl);
        }
    }

    @Override
    public int min(int lvl) {
        return  tier +  //base
                lvl;    //level scaling
    }

    @Override
    public int max(int lvl) {
        if (change) {
            return  2*(tier+1) +    //24 base, down from 30
                    lvl*(tier+1);   //scaling unchanged
        } else {
            return  4*(tier+1) +    //24 base, down from 30
                    lvl*(tier+1);   //scaling unchanged
        }
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return tier + lvl + hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return tier + lvl;
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (!change) {
            return 2 * tier +
                    (3*lvl);
        } else {
            return (5*tier-2) + (lvl*tier) +
                    (3*lvl);
        }
    }

    @Override
    public float abilityChargeUse( Hero hero ) {
        return 0;
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        Reiterpallasch.changeAbility(hero, this);
    }

    public static void changeAbility(Hero hero, FirearmWeapon wep){
        wep.beforeAbilityUsed(hero);
        if (!change) {
            GLog.w(Messages.get(wep, "mode_shot"));
            change = true;
        } else {
            GLog.w(Messages.get(wep, "mode_close"));
            change = false;
        }
        Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
        hero.sprite.operate(hero.pos);
        wep.afterAbilityUsed(hero);
    }

}
