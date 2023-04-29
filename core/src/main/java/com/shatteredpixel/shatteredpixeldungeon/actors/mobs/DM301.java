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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM200Sprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DM301 extends Mob {

    {
        spriteClass = DM200Sprite.class;

        HP = HT = 999;
        defenseSkill = 12;
        baseSpeed = 0.5f;

        EXP = 25;
        maxLvl = 25;

        loot = Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR);
        lootChance = 1f; //initially, see lootChance()

        properties.add(Property.INORGANIC);
        properties.add(Property.MINIBOSS);

        alignment = Alignment.NEUTRAL;
        state = PASSIVE;

        //HUNTING = new Hunting();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 55 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 250;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(100, 200);
    }

    @Override
    public float lootChance(){
        //each drop makes future drops 1/2 as likely
        // so loot chance looks like: 1/8, 1/16, 1/32, 1/64, etc.
        return super.lootChance() * (float)Math.pow(1/2f, Dungeon.LimitedDrops.DM200_EQUIP.count);
    }

    public Item createLoot() {
        Dungeon.LimitedDrops.DM200_EQUIP.count++;
        //uses probability tables for dwarf city
        if (loot == Generator.Category.WEAPON) {
            return Generator.randomWeapon(5);
        } else {
            return Generator.randomArmor(5);
        }
    }


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (state != PASSIVE && alignment == Alignment.NEUTRAL){
            alignment = Alignment.ENEMY;
        }
    }

    @Override
    public void add(Buff buff) {
        super.add(buff);
        if (buff.type == Buff.buffType.NEGATIVE && alignment == Alignment.NEUTRAL){
            alignment = Alignment.ENEMY;
        }
    }

    @Override
    protected boolean act() {
        if (alignment == Alignment.NEUTRAL && state != PASSIVE){
            alignment = Alignment.ENEMY;
        }
        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        if (alignment != Alignment.NEUTRAL || c != Dungeon.hero){
            return super.interact(c);
        }

        Dungeon.hero.busy();
        Dungeon.hero.sprite.operate(pos);
        if (Dungeon.hero.invisible <= 0
                && Dungeon.hero.buff(Swiftthistle.TimeBubble.class) == null
                && Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) == null){
            return doAttack(Dungeon.hero);
        } else {
            sprite.idle();
            alignment = Alignment.ENEMY;
            Dungeon.hero.spendAndNext(1f);
            return true;
        }
    }

    @Override
    public void onAttackComplete() {
        super.onAttackComplete();
        if (alignment == Alignment.NEUTRAL){
            alignment = Alignment.ENEMY;
            Dungeon.hero.spendAndNext(1f);
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        if (state == PASSIVE){
            alignment = Alignment.ENEMY;
        }
        super.damage(dmg, src);
    }

}
