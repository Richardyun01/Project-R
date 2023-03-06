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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Kaleidoscope extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.KALEIDOSCOPE;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        type = FirearmType.FirearmEnergy2;
        max_round = 3;// + 1 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE);;
        ACC = 1.25f;

        bullet_image = ItemSpriteSheet.NOTHING;
        bullet_sound = Assets.Sounds.ZAP;
    }

    @Override
    public void setMaxRound() {
        max_round = 3 + 1 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE) + 1 * Dungeon.hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY);
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (int)(((tier+6) + lvl*3 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (int)(((tier+6) + lvl*3 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY)));
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (int)(((tier+10) + lvl*3 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (int)(((tier+10) + lvl*3 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY)));
        }
    }

}
