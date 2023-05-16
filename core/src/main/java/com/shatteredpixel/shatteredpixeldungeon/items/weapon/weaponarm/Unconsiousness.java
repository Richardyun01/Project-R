package com.shatteredpixel.shatteredpixeldungeon.items.weapon.weaponarm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Unconsiousness extends MeleeWeapon {

    {
        image = ItemSpriteSheet.UNCONSIOUSNESS;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1f;

        weaponarm = true;
        tier = 3;
    }

    public static int levelOfWeapon = 0;

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //12 base, down from 15
                lvl*(tier+1);   //scaling unchanged
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                //deals 50% toward max to max on surprise, instead of min to max.
                int diff = max() - min();
                int damage = augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff*0.5f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Random.IntRange(0, exStr);
                }
                return damage;
            }
        }
        return super.damageRoll(owner);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        levelOfWeapon = this.level();
        return super.proc(attacker, defender, damage);
    }

    @Override
    public float abilityChargeUse( Hero hero ) {
        return 2*super.abilityChargeUse(hero);
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        Dagger.sneakAbility(hero, 5, this);
    }

}
