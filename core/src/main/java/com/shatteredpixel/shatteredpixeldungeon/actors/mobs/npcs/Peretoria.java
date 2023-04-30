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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MirrorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
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
        if (hero.hasTalent(Talent.ADVANCED_TROOPER) && hero.pointsInTalent(Talent.ADVANCED_TROOPER) >= 1) {
            baseSpeed = 1.5f;
        } else {
            baseSpeed = 1f;
        }
    }

    private Hero hero;
    private int heroID;
    public int armTier;

    @Override
    protected boolean act() {

        if ( hero == null ){
            hero = (Hero)Actor.findById(heroID);
            if ( hero == null ){
                die(null);
                sprite.killAndErase();
                return true;
            }
        }

        if (hero.tier() != armTier){
            armTier = hero.tier();
            ((MirrorSprite)sprite).updateArmor( armTier );
        }

        return super.act();
    }

    private static final String HEROID	= "hero_id";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( HEROID, heroID );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        heroID = bundle.getInt( HEROID );
    }

    /*
    public void duplicate( Hero hero ) {
        this.hero = hero;
        heroID = this.hero.id();
    }
    */

    @Override
    public int damageRoll() {
        int damage = Random.NormalIntRange( 12+3*hero.pointsInTalent(Talent.ELITE_GUARD), 35+7*hero.pointsInTalent(Talent.ELITE_GUARD) );;
        return (damage+1);
    }

    @Override
    public int attackSkill( Char target ) {
        return hero.attackSkill(target);
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
    public float attackDelay() {
        return hero.attackDelay(); //handles ring of furor
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (hero.hasTalent(Talent.ADVANCED_TROOPER) && hero.pointsInTalent(Talent.ADVANCED_TROOPER) >= 3) {
            return true;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, hero.belongings.weapon().defenseFactor(this));
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (enemy instanceof Mob) {
            ((Mob)enemy).aggro( this );
        }
        if (hero.belongings.weapon() != null){
            damage = hero.belongings.weapon().proc( this, enemy, damage );
            if (!enemy.isAlive() && enemy == Dungeon.hero){
                Dungeon.fail(getClass());
                GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
            }
            return damage;
        } else {
            return damage;
        }
    }

    @Override
    public CharSprite sprite() {
        CharSprite s = super.sprite();

        hero = (Hero)Actor.findById(heroID);
        if (hero != null) {
            armTier = hero.tier();
        }
        ((MirrorSprite)s).updateArmor( armTier );
        return s;
    }

    {
        if (hero.hasTalent(Talent.ADVANCED_TROOPER) && hero.pointsInTalent(Talent.ADVANCED_TROOPER) >= 1) {
            immunities.add( ToxicGas.class );
            immunities.add( CorrosiveGas.class );
            immunities.add( Burning.class );
            immunities.add( AllyBuff.class );
        }
    }

}