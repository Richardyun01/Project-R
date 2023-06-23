package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ReactiveShield extends Item{

    {
        image = ItemSpriteSheet.SHIELD;

        defaultAction = AC_USE;
        usesTargeting = false;

        bones = false;
        unique = true;
    }

    private static final String AC_USE = "USE";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_USE );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {

        super.execute( hero, action );

        if (hero.buff(ReactiveShieldCooldown.class) != null) {
            GLog.w(Messages.get(this, "cooldown"));
        } else {
            Buff.affect(curUser, ReactiveShieldBuff.class, ReactiveShieldBuff.DURATION);
            Buff.affect(curUser, ReactiveShieldCooldown.class, ReactiveShieldCooldown.DURATION-5*hero.pointsInTalent(Talent.SHIELD_RECHARGE));
            Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
        }

    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return -1;
    }

    public static class ReactiveShieldBuff extends FlavourBuff {

        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        public static final float DURATION = 5f;

        @Override
        public int icon() {
            return BuffIndicator.DEFENDING;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xDFFF40);
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

    public static class ReactiveShieldCooldown extends FlavourBuff {

        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        public static final float DURATION = 200f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xDFFF40);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - 5*Dungeon.hero.pointsInTalent(Talent.SHIELD_RECHARGE) - visualcooldown()) / DURATION);
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
