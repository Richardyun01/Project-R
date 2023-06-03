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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockHighSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WarlockHigh extends Warlock {

    private static final float TIME_TO_ZAP	= 1f;

    {
        spriteClass = WarlockHighSprite.class;

        HP = HT = 80;
        defenseSkill = 20;

        EXP = 15;
        maxLvl = 21;

        loot = Generator.Category.POTION;
        lootChance = 1f;

        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 21 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(2, 8);
    }

    protected void zap() {
        spend( TIME_TO_ZAP );

        Invisibility.dispel(this);
        if (hit( this, enemy, true )) {
            //TODO would be nice for this to work on ghost/statues too
            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.prolong( enemy, Degrade.class, Degrade.DURATION );
                Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
                Buff.prolong( enemy, Vulnerable.class, Vulnerable.DURATION );
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = Random.NormalIntRange( 15, 24 );
            dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
            enemy.damage( dmg, new DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Badges.validateDeathFromEnemyMagic();
                Dungeon.fail( this );
                GLog.n( Messages.get(this, "bolt_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }
}
