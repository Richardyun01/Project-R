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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BulletUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ApachePistol extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.APACHE_PISTOL;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 1;
        type = FirearmType.FirearmPistol;
        max_round = 3;// + 1 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE);

        bones = false;
    }

    public static boolean change = false;

    @Override
    public int STRReq(int lvl) {
        if (hero.heroClass == HeroClass.NOISE || hero.heroClass == HeroClass.CARROLL) {
            return STRReq(tier, lvl);
        } else {
            return STRReq(tier, lvl) - 1;
        }
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        return (Dungeon.level.distance(owner.pos, target.pos) <= 3) ? 1.4f : 0.9f;
    }

    @Override
    public void setMaxRound() {
        max_round = 3 + 1 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE);
    }

    @Override
    public int max(int lvl) {
        if (hero.heroClass == HeroClass.NOISE || hero.subClass == HeroSubClass.TERCIO) {
            return 4 * (tier + 1) + 2 +   //8 base, down from 10
                    lvl * (tier + 1);   //scaling unchanged

        } else if (change) {
            return 5*(tier+1)-1 +
                    lvl*(tier+1);   //scaling unchanged

        } else {
            return  3*(tier+1) +    //6 base, down from 10
                    lvl*(tier+1);   //scaling unchanged
        }
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return tier+1 + lvl + hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return tier + lvl;
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (!change) {
            if (Dungeon.hero.buff(BulletUp.class) != null) {
                return 4 * tier + lvl + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
            } else {
                return 4 * tier + lvl;
            }
        } else {
            if (Dungeon.hero.buff(BulletUp.class) != null) {
                return tier + lvl + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
            } else {
                return tier + lvl;
            }
        }
    }

    @Override
    public float abilityChargeUse( Hero hero ) {
        return 0;
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        ApachePistol.changeAbility(hero, this);
    }

    public static void changeAbility(Hero hero, FirearmWeapon wep){
        wep.beforeAbilityUsed(hero);
        if (!change) {
            GLog.w(Messages.get(wep, "mode_close"));
            change = true;
        } else {
            GLog.w(Messages.get(wep, "mode_shot"));
            change = false;
        }
        Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
        hero.sprite.operate(hero.pos);
        wep.afterAbilityUsed(hero);
    }

}