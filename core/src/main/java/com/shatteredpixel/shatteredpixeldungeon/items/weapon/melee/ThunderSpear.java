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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ThunderSpear extends MeleeWeapon {

    {
        image = ItemSpriteSheet.THUNDERSPEAR;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
        DLY = 2f; //0.5x speed
    }

    @Override
    public int max(int lvl) {
        return  5*(tier+1) +
                lvl*(tier+1);   //scaling unchanged
    }

    @Override
    public int min(int lvl) {
        return  tier+1 +
                lvl*(tier-1);
    }

    private boolean doubleattack = true;

    public int proc(Char charR, Char charR2, int i) {
        if (this.doubleattack) {
            this.doubleattack = false;
            if (!charR.attack(charR2)) {
                this.doubleattack = true;
            } else {
                charR2.sprite.bloodBurstA(charR2.sprite.center(), 4);
                charR2.sprite.flash();
                boolean z = charR instanceof Hero;
                if (z && Dungeon.hero.subClass == HeroSubClass.GLADIATOR) {
                    ((Combo) Buff.affect(charR, Combo.class)).bounshit(charR2);
                }
            }
        } else {
            this.doubleattack = true;
        }
        if ((charR instanceof Hero) && Dungeon.hero.belongings.getItem(ChaliceOfBlood.class) != null && ((ChaliceOfBlood) ((Hero) charR).belongings.getItem(ChaliceOfBlood.class)).isEquipped(Dungeon.hero) && Random.Int(20) < 1) {
            i = (int) (((float) i) * 1.5f);
        }
        return super.proc(charR, charR2, i);
    }

}