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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LightJunkerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class LightJunker extends Mob {

    {
        spriteClass = LightJunkerSprite.class;

        HP = HT = 5;
        defenseSkill = 3;
        baseSpeed = 0.5f;

        EXP = 10;
        maxLvl = 10;

        loot = Generator.Category.WEAPON;
        lootChance = 0.2f; //by default, see lootChance()

        properties.add(Property.UNDEAD);
        properties.add(Property.INORGANIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 5 );
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (cause == Chasm.class) return;

        boolean heroKilled = false;
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
            if (ch != null && ch.isAlive()) {
                int damage = Math.round(Random.NormalIntRange(20, 40));
                damage = Math.round( damage * AscensionChallenge.statModifier(this));
                damage = Math.max( 0,  damage - (ch.drRoll() + ch.drRoll()) );
                ch.damage( damage, this );
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES );
        }

        if (heroKilled) {
            Dungeon.fail( getClass() );
            GLog.n( Messages.get(Skeleton.class, "explo_kill") );
        }
    }

    @Override
    public float lootChance() {
        //each drop makes future drops 2/3 as likely
        // so loot chance looks like: 1/3, 2/9, 4/27, 8/81, etc.
        return super.lootChance() * (float)Math.pow(2/3f, Dungeon.LimitedDrops.SKELE_WEP.count);
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.SKELE_WEP.count++;
        return super.createLoot();
    }

    @Override
    public int attackSkill( Char target ) {
        return 1;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

}