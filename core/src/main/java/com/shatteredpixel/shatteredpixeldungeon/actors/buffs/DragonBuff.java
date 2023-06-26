package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class DragonBuff extends Buff implements ActionIndicator.Action {

    {
        actPriority = BUFF_PRIO - 1;
        announced = false;
    }

    public enum State {
        NORMAL,
        DRAGON
    }
    public DragonBuff.State state = DragonBuff.State.NORMAL;

    @Override
    public boolean act() {
        if (state == State.DRAGON) {
            int damage = 1;
            if (hero.HP > damage) {
                if (hero.buff(BlobImmunity.class) == null) {
                    target.damage( damage, this ); //deals no damage when hero has blob immunity buff
                }
            } else {
                doAction();
            }
        }
        spend( TICK );
        return true;
    }

    public boolean isDragon() {
        if (state == State.NORMAL) {
            return false;
        } else {
            return true;
        }
    }

    public int attackBonus( int excessArmorStr ) {
        if (state == State.DRAGON) {
            return excessArmorStr * Dungeon.hero.pointsInTalent(Talent.OLD_MEMORY_III);
        } else {
            return 0;
        }
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public int icon() {
        if (state == State.NORMAL) {
            return BuffIndicator.WEAPON;
        } else {
            return BuffIndicator.DRAGON;
        }
    }

    @Override
    public void tintIcon(Image icon) {
        if (state == State.NORMAL) {
            icon.hardlight(0xB2E9FF);
        } else {
            icon.hardlight(0xFFFF00);
        }
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        String desc;
        if (state == State.NORMAL) {
            desc = Messages.get(this, "state_normal");
        } else {
            desc = Messages.get(this, "state_dragon");
        }
        return desc;
    }

    private static final String STATE = "state";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STATE, state);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        state = bundle.getEnum(STATE, State.class);
        ActionIndicator.setAction(this);
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        if (state == State.NORMAL) {
            if (((Hero)target).belongings.weapon() != null) {
                return ((Hero)target).belongings.weapon().image;
            } else {
                return ItemSpriteSheet.WEAPON_HOLDER;
            }
            //tint(0xFFFF0000);
        } else {
            return ItemSpriteSheet.MAGNUS_WING;
        }
    }

    @Override
    public int indicatorColor() {
        return 0;
    }

    @Override
    public void doAction() {
        if (state == State.NORMAL) {
            if (hero.buff(DragonCoolDown.class) == null) {
                state = State.DRAGON;
                hero.sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "name") );
                BuffIndicator.refreshHero();
                ActionIndicator.updateIcon();
                Sample.INSTANCE.play( Assets.Sounds.GHOST );
                hero.sprite.centerEmitter().start( Speck.factory( Speck.ORDER ), 0.3f, 3 );
                hero.spendAndNext(Actor.TICK);
            } else {
                GLog.w(Messages.get(this, "not_prepared"));
            }
        } else {
            state = State.NORMAL;
            Buff.affect(hero, DragonCoolDown.class, DragonCoolDown.DURATION * (float)Math.pow(0.85f, hero.pointsInTalent(Talent.OLD_MEMORY_I)) );
            Buff.affect(hero, Vulnerable.class, 5f);
            BuffIndicator.refreshHero();
            ActionIndicator.updateIcon();
            hero.spendAndNext(Actor.TICK);
        }
    }

    public void indicate() {
        ActionIndicator.setAction( this );
    }

    public static class DragonCoolDown extends FlavourBuff{
        {
            type = buffType.NEUTRAL;
            announced = false;
        }
        public static final float DURATION	= 30f;
        public int icon() { return BuffIndicator.TIME; }
        public void tintIcon(Image icon) { icon.hardlight(0x99992E); }
        public float iconFadePercent() { return Math.max(0, (DURATION - visualcooldown()) / DURATION); }
    };

}
