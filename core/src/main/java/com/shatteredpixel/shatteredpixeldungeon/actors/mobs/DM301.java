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

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LanceBleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.PulsingCore;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM301Sprite;
import com.watabou.utils.Random;

public class DM301 extends Mob {

    {
        spriteClass = DM301Sprite.class;

        HP = HT = 999;
        defenseSkill = 12;
        baseSpeed = 0.5f;

        EXP = 25;
        maxLvl = 25;

        loot = PulsingCore.class;
        lootChance = 1f; //initially, see lootChance()

        properties.add(Property.INORGANIC);
        properties.add(Property.MINIBOSS);

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 100, 150 );
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
    public void die( Object cause ) {

        /*
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && Statistics.spawnersAlive == 4){
            Badges.validateBossChallengeCompleted();
        } else {
            Statistics.qualifiedForBossChallengeBadge = false;
        }
        */
        Statistics.enemiesSlain += 2000;

        super.die( cause );

    }

    {
        immunities.add(Frost.class);
        immunities.add(Burning.class);
        immunities.add(Poison.class);
        immunities.add(Corrosion.class);
        immunities.add(Ooze.class);
        immunities.add(LanceBleeding.class);
        immunities.addAll(new BlobImmunity().immunities());

        immunities.addAll( AntiMagic.RESISTS );
    }

}
