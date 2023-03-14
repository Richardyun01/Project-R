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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class NotMachineGun extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.NOT_MACHINE_GUN;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 1;
        type = FirearmType.FirearmAuto;
        max_round = 8;
        shot = 2;
        bones = false;

        bullet_image = ItemSpriteSheet.DUAL_BULLET;
    }

    @Override
    public int STRReq(int lvl) {                    //10 base strength req, down from 11
        if (hero.heroClass == HeroClass.NOISE) {
            return STRReq(tier, lvl);
        } else {
            return STRReq(tier, lvl) - 1;
        }
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (Dungeon.level.distance(owner.pos, target.pos) <= 2) {
            return 1.25f;
        } else if (Dungeon.level.distance(owner.pos, target.pos) > 2 && Dungeon.level.distance(owner.pos, target.pos) <= 7) {
            return 1f;
        } else {
            return 0.8f;
        }
    }

    @Override
    public void setMaxRound() {
        max_round = 8 + 2 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE) + 2 * Dungeon.hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY);
    }

    @Override
    public int min(int lvl) {
        return  tier + //base
                lvl;    //level scaling
    }

    @Override
    public int max(int lvl) {
        return  3*(tier+1) +    //base
                lvl*(tier+1);   //level scaling
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return 3 + lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return 3 + lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }

    }

}
