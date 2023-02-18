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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Defender extends MeleeWeapon {

    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0xA6F6FF );

    {
        image = ItemSpriteSheet.DEFENDER;

        tier = 6;
    }

    @Override
    public int max(int lvl) {
        return  Math.round(4f*(tier-1)) +     //20 base
                lvl*(tier-2);                   //+4 per level
    }

    @Override
    public int defenseFactor( Char owner ) {
        return 8+4*buffedLvl();    //8 extra defence, plus 4 per level;
    }

    public String statsInfo(){
        if (isIdentified()){
            return Messages.get(this, "stats_desc", 8+4*buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 4);
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }

    public int proc(Weapon weapon, Char attacker, int damage) {

        int level = Math.max( 0, weapon.buffedLvl() );

        float procChance = (level+4f)/(level+40f);
        if (Random.Float() < procChance){
            Blocking.BlockBuff b = Buff.affect(attacker, Blocking.BlockBuff.class);
            b.setShield(attacker.HT/10);
            attacker.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 5);
        }

        return damage;
    }

}