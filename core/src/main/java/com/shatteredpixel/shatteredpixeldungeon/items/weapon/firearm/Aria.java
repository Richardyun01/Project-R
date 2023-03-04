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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Aria extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.ARIA;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 6;
        type = FirearmType.FirearmPrecision;
        max_round = 1;

        bullet_image = ItemSpriteSheet.SNIPER_BULLET;
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (int)(((3*tier) + (6*lvl) + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1+0.075*Dungeon.hero.pointsInTalent(Talent.FIRE_PREPARATION))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (int)(((3*tier) + (6*lvl) + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1+0.075*Dungeon.hero.pointsInTalent(Talent.FIRE_PREPARATION)));
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (int)(((10*tier) + (10*lvl) + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1+0.075*Dungeon.hero.pointsInTalent(Talent.FIRE_PREPARATION))) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (int)(((10*tier) + (10*lvl) + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)) * (1+0.075*Dungeon.hero.pointsInTalent(Talent.FIRE_PREPARATION)));
        }
    }

    @Override
    public void setReloadTime() {
        reload_time = 3f * RingOfReload.reloadMultiplier(Dungeon.hero);
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        return Dungeon.level.adjacent(owner.pos, target.pos) ? 0f : 2f;
    }

/*
    @Override
    protected void onThrow(int cell) {
        Ballistica aim = new Ballistica(hero.pos, cell, Ballistica.PROJECTILE);
        curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( aim.path.get(aim.dist) )));
    }
*/
}
