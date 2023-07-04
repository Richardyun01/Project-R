package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.magnus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class Thruster extends ArmorAbility {

    {
        baseChargeUse = 35f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {

        Buff.prolong(hero, ThrusterBuff.class, ThrusterBuff.DURATION);

        if (hero.hasTalent(Talent.PARATROOPER)) {
            Buff.affect(hero, Bless.class, 5*hero.pointsInTalent(Talent.PARATROOPER));
        }

        hero.sprite.operate(hero.pos);
        hero.spendAndNext(Actor.TICK);

        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
        Invisibility.dispel();
    }

    @Override
    public int icon() {
        return HeroIcon.THRUSTER;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.ANTI_GRAVITY_PARA, Talent.PARATROOPER, Talent.LIGHTWEIGHT_BOOSTER, Talent.HEROIC_ENERGY};
    }

    public static class ThrusterBuff extends FlavourBuff {

        {
            type = buffType.POSITIVE;
            announced = false;
        }

        public static final float DURATION = 20f;

        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo( target )) {
                target.flying = true;
                Roots.detach( target, Roots.class );
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void detach() {
            target.flying = false;
            super.detach();
            if (Dungeon.hero.hasTalent(Talent.ANTI_GRAVITY_PARA)) {
                Buff.affect(Dungeon.hero, FeatherFall.FeatherBuff.class, 2*Dungeon.hero.pointsInTalent(Talent.ANTI_GRAVITY_PARA));
            }
            //only press tiles if we're current in the game screen
            if (ShatteredPixelDungeon.scene() instanceof GameScene) {
                Dungeon.level.occupyCell(target );
            }
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.LEVITATING);
            else target.sprite.remove(CharSprite.State.LEVITATING);
        }

        @Override
        public int icon() {
            return BuffIndicator.THRUST;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }
}
