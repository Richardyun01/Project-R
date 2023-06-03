package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollTricksterSprite;

public class Lumi extends Mob {

    {
        spriteClass = GnollTricksterSprite.class;

        EXP = 0;

        state = WANDERING;

        //at half quantity, see createLoot()
        /*
        loot = Generator.Category.MISSILE;
        lootChance = 1f;
        */
    }

    public Lumi() {
        super();

        HP = HT = 5;
        defenseSkill = 10 + 5 * Dungeon.depth;
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.distance(this.pos, Dungeon.hero.pos) <= 2) alerted = true;
        return super.act();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return false;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void aggro(Char ch) {
        //cannot be aggroed to something it can't see
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

}
