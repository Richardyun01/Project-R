package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.watabou.utils.Random;

public class ShopKeeperBoss extends Mob {

    {
        spriteClass = ShopkeeperSprite.class;

        HP = HT = 300;
        defenseSkill = 30; //see damage()
        baseSpeed = 1f;

        EXP = 30;

        state = HUNTING;

        loot = new ScrollOfEnchantment();
        lootChance = 1f;

        properties.add(Property.MINIBOSS);
        this.immunities.add(Paralysis.class);
        this.immunities.add(Terror.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 50, 75 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return this.fieldOfView[enemy.pos];
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        Buff.affect(enemy, Bleeding.class).set(10f);
        Buff.affect(enemy, Vertigo.class, 2f);

        return damage;
    }

    @Override
    public void die( Object cause ) {

        Badges.validateShopkeeperKill();
        Statistics.enemiesSlain += 3000;

        super.die( cause );

    }

}
