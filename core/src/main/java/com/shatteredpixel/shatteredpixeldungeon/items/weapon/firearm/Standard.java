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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Standard extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.STANDARD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        type = FirearmType.FirearmAuto;
        max_round = 12;// + 3 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE);;
        shot = 3;

        firearm = true;
        firearmAuto = true;

        bullet_image = ItemSpriteSheet.DUAL_BULLET;
    }

    @Override
    public void setMaxRound() {
        max_round = 12 + 3 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE);
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
    protected void carrollAbility(Hero hero, Integer target ) {
        ShortCarbine.shootAbility(hero, this);
    }

}
