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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfLesserHealing extends Elixir {

    {
        image = ItemSpriteSheet.POTION_CHLORO;
    }

    @Override
    public void apply( Hero hero ) {
        identify();
        heal( hero );
        Talent.onHealingPotionUsed( hero );
    }

    public static void heal( Char ch ){
        if (ch == Dungeon.hero && Dungeon.isChallenged(Challenges.NO_HEALING)){
            pharmacophobiaProc(Dungeon.hero);
        } else {
            //starts out healing 30 hp, equalizes with hero health total at level 11
            Buff.affect(ch, Healing.class).setHeal((int) (0.5f * ch.HT + 7), 0.125f, 0);
            if (ch == Dungeon.hero){
                GLog.p( Messages.get(PotionOfHealing.class, "heal") );
            }
        }
    }

    public static void pharmacophobiaProc( Hero hero ){
        // harms the hero for ~40% of their max HP in poison
        Buff.affect( hero, Poison.class).set(4 + hero.lvl/2);
    }

    @Override
    public int value() {
        return isKnown() ? 15 * quantity : super.value();
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfHealing.class, Food.class};
            inQuantity = new int[]{1,1};

            cost = 3;

            output = PotionOfLesserHealing.class;
            outQuantity = 4;
        }

    }
}
