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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Peretoria extends NPC {

    {
        spriteClass = StatueSprite.class;

        alignment = Alignment.ALLY;
        intelligentAlly = true;
        state = HUNTING;

        //before other mobs
        actPriority = MOB_PRIO + 1;
    }

    public Peretoria() {
        HP = HT = 75 + 10*Dungeon.hero.pointsInTalent(Talent.ELITE_GUARD);
        defenseSkill = 15;
        if (Dungeon.hero.hasTalent(Talent.ADVANCED_TROOPER) && Dungeon.hero.pointsInTalent(Talent.ADVANCED_TROOPER) >= 1) {
            baseSpeed = 1.5f;
        } else {
            baseSpeed = 1f;
        }
    }

    private Hero hero;

    private int turnCounter = 0;

    @Override
    protected boolean act() {
        turnCounter++;
        if (turnCounter >= 50) {
            die(null);
            return true;
        }
        return super.act();
    }

    private static final String TURN_COUNTER = "turn_counter";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(TURN_COUNTER, turnCounter);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        turnCounter = bundle.getInt(TURN_COUNTER);
    }

    @Override
    public String description() {
        String desc = super.description();
        desc += "\n\n" + Messages.get(this, "turns_left", 50-turnCounter);
        return desc;
    }

    @Override
    public int damageRoll() {
        int damage;

        if (Dungeon.hero.hasTalent(Talent.ELITE_GUARD)) {
            damage = Random.NormalIntRange( 12+3*Dungeon.hero.pointsInTalent(Talent.ELITE_GUARD), 35+7*Dungeon.hero.pointsInTalent(Talent.ELITE_GUARD) );
        } else {
            damage = Random.NormalIntRange( 12, 35 );
        }

        return (damage+1);
    }

    @Override
    public int attackSkill( Char target ) {
        return 35;
    }

    @Override
    public int defenseSkill(Char enemy) {
        if (hero != null) {
            int baseEvasion = 4 + hero.lvl;
            int heroEvasion = hero.defenseSkill(enemy);

            return super.defenseSkill(enemy) * (baseEvasion + heroEvasion);
        } else {
            return 0;
        }
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (Dungeon.hero.hasTalent(Talent.ADVANCED_TROOPER) && Dungeon.hero.pointsInTalent(Talent.ADVANCED_TROOPER) >= 3) {
            return true;
        }
        return super.canAttack(enemy);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(10, 20);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (enemy instanceof Mob) {
            ((Mob)enemy).aggro( this );
        }

        return damage;
    }

    /*
    private float buildToDamage = 0f;
    @Override
    public boolean act() {
        buildToDamage += target.HT/200f;

        int damage = (int)buildToDamage;
        buildToDamage -= damage;

        if (damage > 0)
            target.damage(damage, this);

        spend(TICK);

        return true;
    }
    */

    {
        if (Dungeon.hero.hasTalent(Talent.ADVANCED_TROOPER) && Dungeon.hero.pointsInTalent(Talent.ADVANCED_TROOPER) >= 2) {
            immunities.add( ToxicGas.class );
            immunities.add( CorrosiveGas.class );
            immunities.add( Burning.class );
            immunities.add( AllyBuff.class );
        } else {
            immunities.add( AllyBuff.class );
        }
    }

}