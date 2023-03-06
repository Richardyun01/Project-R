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
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

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
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier, lvl)-1; //10 base strength req, down from 11
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
        if (hero.heroClass == HeroClass.NOISE) {
            return  4*(tier+1) + 2 +   //8 base, down from 10
                    lvl*(tier+1);   //scaling unchanged
        } else {
            return  4*(tier+1) +    //8 base, down from 10
                    lvl*(tier+1);   //scaling unchanged
        }
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return tier + lvl + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return tier + lvl;
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return 4 * tier + lvl + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return 4 * tier + lvl;
        }
    }

}