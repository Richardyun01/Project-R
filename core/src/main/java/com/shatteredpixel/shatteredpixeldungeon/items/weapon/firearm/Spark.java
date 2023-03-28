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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Spark extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.SPARK;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        type = FirearmType.FirearmEnergy2;
        max_round = 3;

        firearm = true;
        firearmEnergy = true;

        bullet_image = ItemSpriteSheet.NOTHING;
        bullet_sound = Assets.Sounds.ZAP;
    }

    @Override
    public void setMaxRound() {
        max_round = 3 + 1 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE) + 1 * Dungeon.hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY);
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        return 1.25f;
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (int)(((tier+9) + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (int)(((tier+9) + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY)));
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (int)(((tier+13) + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (int)(((tier+13) + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero))*(1 - 0.1 * hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY)));
        }
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Vega.shootAbility(hero, this);
    }

}