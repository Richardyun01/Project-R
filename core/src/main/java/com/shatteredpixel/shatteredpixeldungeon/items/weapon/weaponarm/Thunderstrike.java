/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.weaponarm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Mace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Thunderstrike extends MeleeWeapon {

    {
        image = ItemSpriteSheet.THUNDERSTRIKE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.8f;

        weaponarm = true;

        tier=5;
    }

    @Override
    public int max(int lvl) {
        return  4*(tier-1)-2 +
                lvl*(tier-1);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        int mobCount;
        if (attacker instanceof Hero) {
            mobCount = 1;
            for (Mob mob : (Mob[]) Dungeon.level.mobs.toArray(new Mob[0])) {
                if (Dungeon.level.adjacent(mob.pos, defender.pos) && mob.alignment != Char.Alignment.ALLY) {
                    mob.damage(Dungeon.hero.damageRoll() - Math.max(defender.drRoll(), defender.drRoll()), this);
                    mobCount++;
                }
            }
        } else {
            mobCount = 1;
        }
        if (mobCount == 1) {
            damage *= 2;
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        Mace.heavyBlowAbility(hero, target, 1.5f, this);
    }

}
