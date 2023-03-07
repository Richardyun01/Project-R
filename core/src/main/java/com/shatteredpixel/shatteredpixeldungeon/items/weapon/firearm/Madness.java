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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Madness extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.MADNESS;
        hitSound = Assets.Sounds.BLAST;
        hitSoundPitch = 0.8f;

        tier = 6;
        type = FirearmType.FirearmEtc1;
        max_round = 1;

        bullet_image = ItemSpriteSheet.KOJIMA_PARTICLE;
        bullet_sound = Assets.Sounds.BLAST;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        return 2f;
    }

    @Override
    public void setReloadTime() {
        if (loader != null) {
            reload_time = 50f * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 50f;
        }
    }

    @Override
    public int Bulletmin(int lvl) {
        return 200 + (lvl*25);
    }

    @Override
    public int Bulletmax(int lvl) {
        return 200 + (lvl*25);
    }

}