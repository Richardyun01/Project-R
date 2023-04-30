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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.PulsingCore;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.LinkedList;

public class EvolutionFire extends WellWater {

    @Override
    protected boolean affectHero( Hero hero ) {
        return false;
    }

    @Override
    protected Notes.Landmark record() {
        return Notes.Landmark.EVOLUTION_FIRE;
    }

    //count items that are sacrificed;
    private int itemCount = 0;
    //count special items that should be sacrificed;
    private int specialCount = 0;

    private Item prize;

    public LinkedList<Item> items = new LinkedList<>();

    protected Item affectItem( Item item, int pos ) {

        for (Item itemed : items.toArray( new Item[0] )) {
            if (item instanceof Weapon) {
                int tier = (item instanceof MeleeWeapon) ? ((MeleeWeapon) item).tier : ((FirearmWeapon) item).tier;
                if (tier >= 5) {
                    items.remove();
                    itemCount++;
                    GLog.w( Messages.get(EvolutionFire.class, "worthy"));
                } else {
                    GLog.w( Messages.get(EvolutionFire.class, "unworthy"));
                    return null;
                }
            } else if (item instanceof Armor) {
                int tier = ((Armor) item).tier;
                if (tier >= 5) {
                    items.remove();
                    itemCount++;
                    GLog.w( Messages.get(EvolutionFire.class, "worthy"));
                } else {
                    GLog.w( Messages.get(EvolutionFire.class, "unworthy"));
                    return null;
                }
            } else if (item instanceof PulsingCore) {
                specialCount++;
                GLog.w( Messages.get(EvolutionFire.class, "worthy"));
            } else {
                GLog.w( Messages.get(EvolutionFire.class, "unworthy"));
                return null;
            }
        }

        if (itemCount >= 3 && specialCount >= 2) {
            emitter.parent.add( new Identification( DungeonTilemap.tileCenterToWorld( pos ) ) );
            GLog.w( Messages.get(EvolutionFire.class, "evolution"));
            return prize;
        } else {
            return null;
        }
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    private static final String ITEM_COUNT = "item_count";
    private static final String SPECIAL_COUNT = "SPECIAL_COUNT";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEM_COUNT, itemCount);
        bundle.put(SPECIAL_COUNT, specialCount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        itemCount = bundle.getInt(ITEM_COUNT);
        specialCount = bundle.getInt(SPECIAL_COUNT);
    }

    public void setPrize( Item prize ){
        this.prize = prize;
    }

}
