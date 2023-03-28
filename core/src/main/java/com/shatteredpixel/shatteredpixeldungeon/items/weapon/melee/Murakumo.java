package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Murakumo extends MeleeWeapon{

    {
        image = ItemSpriteSheet.MURAKUMO;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.2f;

        tier = 5;
    }

    @Override
    public int max(int lvl) {
        return  3*(tier) +    //15 base
                lvl*(tier);     //scaling +5 per +1
    }

    @Override
    public float abilityChargeUse(Hero hero) {
        return 2*super.abilityChargeUse(hero);
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        beforeAbilityUsed(hero);
        Buff.prolong(hero, Murakumo.RushStance.class, 10f); //4 turns as using the ability is instant
        hero.sprite.operate(hero.pos);
        hero.next();
        afterAbilityUsed(hero);
    }

    public static class RushStance extends FlavourBuff {

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_EVASIVE;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (6 - visualcooldown()) / 6);
        }
    }

}
