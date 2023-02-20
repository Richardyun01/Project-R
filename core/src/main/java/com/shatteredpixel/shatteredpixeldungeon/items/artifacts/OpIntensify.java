package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Shadows;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class OpIntensify extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_PLUS;

        levelCap = 10;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new OpIntensify.artifactPlus();
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped (Dungeon.hero)) {
            desc += "\n\n";
            if (cursed)
                desc += Messages.get(this, "desc_cursed");
            else
                desc += Messages.get(this, "desc_equipped_1");
            if (level() >= 10) desc += Messages.get(this, "desc_equipped_4");
            if (level() >= 7)  desc += Messages.get(this, "desc_equipped_3");
            if (level() >= 3)  desc += Messages.get(this, "desc_equipped_2");
        }

        return desc;
    }

    public class artifactPlus extends ArtifactBuff {

        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo( target )) {

                boolean sighted = target.buff(Blindness.class) == null && target.buff(Shadows.class) == null
                        && target.buff(TimekeepersHourglass.timeStasis.class) == null && target.isAlive();

                if (sighted) {
                    if (target instanceof Hero) {
                        target.viewDistance += 2;
                        ((Hero) target).act();
                    }
                }

                if (level() >= 3) {

                }
                if (level() >= 7) {

                }
                if (level() >= 10) {

                }

                return true;
            }
            return false;
        }

        public void gainExp( float levelPortion ) {
            if (cursed || target.buff(MagicImmune.class) != null || levelPortion == 0) return;

            exp += Math.round(levelPortion*100);

            if (exp > 100+level()*100 && level() < levelCap){
                exp -= 100+level()*100;
                GLog.p( Messages.get(this, "levelup") );
                upgrade();
            }

        }
    }

    public class Accuracy extends ArtifactBuff {
    }

}
