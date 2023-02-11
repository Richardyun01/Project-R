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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Scripture extends MeleeWeapon {

    {
        image = ItemSpriteSheet.SCRIPTURE;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1.1f;

        tier = 3;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (attacker.buff(Bless.class) == null) {
            Buff.affect( attacker, Bless.class, 2f);
        } else if (attacker.buff(Adrenaline.class) == null) {
            Buff.affect( attacker, Adrenaline.class, 2f);
        } else if (attacker.buff(Barrier.class) == null) {
            int barrAmt = 1;
            barrAmt = Math.min( barrAmt, attacker.HT - attacker.HP );
            if (barrAmt > 0 && attacker.isAlive()) {
                Buff.affect(hero, Barrier.class).setShield((int) (2));
                attacker.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( barrAmt ) );
            }
        } else {
            int healAmt = 1;
            healAmt = Math.min( healAmt, attacker.HT - attacker.HP );
            if (healAmt > 0 && attacker.isAlive()) {
                attacker.HP += healAmt;
                attacker.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 1 );
                attacker.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( healAmt ) );
            }
        }
        return super.proc( attacker, defender, damage );
    }

    @Override
    public int max(int lvl) {
        return  4*(tier) +    //12 base, down from 20
                lvl*(tier);
    }

}