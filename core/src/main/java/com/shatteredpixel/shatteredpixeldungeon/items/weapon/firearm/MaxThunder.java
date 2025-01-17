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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class MaxThunder extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.MAX_THUNDER;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 6;
        type = FirearmType.FirearmShotgun;
        max_round = 10;
        shot = 10;

        firearm = true;
        firearmShotgun = true;

        bullet_image = ItemSpriteSheet.TRIPLE_BULLET;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (owner instanceof Hero &&
                owner.buff(Blunderbust.SlugShot.class) != null &&
                owner.buff(MeleeWeapon.Charger.class) != null &&
                owner.buff(Blunderbust.SlugShot.class).onUse &&
                owner.buff(MeleeWeapon.Charger.class).charges >= 1) {
            return 1f;
        } else {
            return Dungeon.level.adjacent(owner.pos, target.pos) ? 1.5f : 0f;
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        Buff.affect(defender, Burning.class).reignite(defender, 8f);
        int burnDamage = Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 );
        defender.damage( Math.round(burnDamage * 0.67f), this );

        return super.proc(attacker, defender, damage);
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Blunderbust.shootAbility(hero, this);
    }

}
