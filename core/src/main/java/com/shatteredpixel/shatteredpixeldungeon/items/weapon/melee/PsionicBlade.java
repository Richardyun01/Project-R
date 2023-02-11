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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class PsionicBlade extends MeleeWeapon {

    {
        image = ItemSpriteSheet.PSIONIC_BLADE1;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 1;

        bones = false;
        unique = true;
    }

    @Override
    public int min(int lvl) {
        int dmg = 5 + Dungeon.hero.lvl;
        return Math.max(0, dmg);
    }

    @Override
    public int max(int lvl) {
        int[] dmg_table = {20, 32, 44, 56, 68, 80, 92};
        int dmg = dmg_table[Dungeon.hero.lvl / 5];
        return Math.max(0, dmg);
    }

    @Override
    public int level() {
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;

        if (level >= 5)
            image = ItemSpriteSheet.PSIONIC_BLADE3;
        else if (level >= 2)
            image = ItemSpriteSheet.PSIONIC_BLADE2;
        else
            image = ItemSpriteSheet.PSIONIC_BLADE1;

        return level;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        if (level() >= 5)
            image = ItemSpriteSheet.PSIONIC_BLADE3;
        else if (level() >= 2)
            image = ItemSpriteSheet.PSIONIC_BLADE2;
        else
            image = ItemSpriteSheet.PSIONIC_BLADE1;
    }

    @Override
    public int buffedLvl() {
        //level isn't affected by buffs/debuffs
        return level();
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped (Dungeon.hero)){
            desc += " ";
            if (level() < 1)
                desc += Messages.get(this, "desc_1");
            else if (level() < 5)
                desc += Messages.get(this, "desc_2");
            else
                desc += Messages.get(this, "desc_3");
        }

        return desc;
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

}
