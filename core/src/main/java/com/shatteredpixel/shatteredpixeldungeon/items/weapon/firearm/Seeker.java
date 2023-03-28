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


import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Seeker extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.SEEKER;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        type = FirearmType.FirearmPrecision;
        max_round = 1;

        firearm = true;
        firearmPrecision = true;

        bullet_image = ItemSpriteSheet.SNIPER_BULLET;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (owner instanceof Hero &&
                owner.buff(Tat.PrecisionShot.class) != null &&
                owner.buff(MeleeWeapon.Charger.class) != null &&
                owner.buff(Tat.PrecisionShot.class).onUse &&
                owner.buff(MeleeWeapon.Charger.class).charges >= 1) {
            owner.buff(MeleeWeapon.Charger.class).charges--;
            return INFINITE_ACCURACY;
        } else {
            return Dungeon.level.adjacent(owner.pos, target.pos) ? 0f : 2f;
        }
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Tat.shootAbility(hero, this);
    }

}