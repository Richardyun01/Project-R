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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.HashSet;

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
            return Messages.get(this, "stats_desc", 8+4*buffedLvl(), 2+2*buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 8, 2);
        }
    }

    public static final HashSet<Class> RESISTS = new HashSet<>();

    static {
        RESISTS.addAll(AntiMagic.RESISTS);
    }

    //see Hero.damage for antimagic effects
    public static int drRoll(int level) {
        return Random.NormalIntRange(0, 2 + 2 * level); //2 extra defence, plus 2 per level;
    }

    @Override
    protected void carrollability(Hero hero, Integer target) {
        RoundShield.guardAbility(hero, 3, this);
    }

}