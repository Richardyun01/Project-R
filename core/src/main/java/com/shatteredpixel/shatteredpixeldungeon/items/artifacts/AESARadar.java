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

public class AESARadar extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_RADAR;

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
        return new AESARadar.radarCharge();
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped (Dungeon.hero)) {
            desc += "\n\n";
            if (cursed)
                desc += Messages.get(this, "desc_cursed");
            else
                desc += Messages.get(this, "desc_equipped");
        }

        return desc;
    }

    public class radarCharge extends ArtifactBuff {

        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo( target )) {

                boolean sighted = target.buff(Blindness.class) == null && target.buff(Shadows.class) == null
                        && target.buff(TimekeepersHourglass.timeStasis.class) == null && target.isAlive();

                if (sighted) {
                    if (target instanceof Hero) {
                        target.viewDistance += 1 * level() + 1;
                        ((Hero) target).act();
                        GLog.p(Messages.get(this, "attachto"));
                    }
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

}