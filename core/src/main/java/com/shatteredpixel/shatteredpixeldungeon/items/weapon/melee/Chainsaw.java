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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Chainsaw extends MeleeWeapon {

    {
        image = ItemSpriteSheet.CHAINSAW;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.5f;

        tier = 4;
        ACC = 0.75f; //0.75x accuracy
        DLY = 0.5f; //2x speed

    }

    @Override
    public int max(int lvl) {
        return  Math.round(3*(tier+2)) +        //18 base, down from 25
                lvl*Math.round(0.5f*(tier+2));  //+3 per level, down from +5
    }

    @Override
    public float abilityChargeUse(Hero hero, Char target) {
        return 2*super.abilityChargeUse(hero, target);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        SurrationSaw.sawAbility(hero, target, 0.05f, this);
    }

}
