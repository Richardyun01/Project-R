/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class WinterStorm extends FlavourBuff {

    public static final float DURATION	= 20f;

    {
        type = Buff.buffType.POSITIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.WINTERSTORM;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    @Override
    public void fx(boolean on) {
        if (on) {
            target.sprite.aura(0xFFE4E4E4);
            target.sprite.add(CharSprite.State.CHILLED);
        } else {
            target.sprite.remove(CharSprite.State.CHILLED);
            target.sprite.clearAura();
        }
    }

    {
        immunities.add( Frost.class );
        immunities.add( Chill.class );
        immunities.add( Burning.class );
        immunities.add( Cripple.class );
        immunities.add( Bleeding.class );
        immunities.add( Poison.class );
        immunities.add( Paralysis.class );
        immunities.add( Hex.class );
        immunities.add( Vulnerable.class );
        immunities.add( Blindness.class );
        immunities.add( Charm.class );
        immunities.add( Weakness.class );
        immunities.add( Ooze.class );
        immunities.add( Corrosion.class );
        immunities.addAll(new BlobImmunity().immunities());
        immunities.add( Levitation.class );
    }

}
