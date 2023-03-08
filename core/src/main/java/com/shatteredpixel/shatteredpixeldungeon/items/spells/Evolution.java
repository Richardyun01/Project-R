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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.AAWSM;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Aria;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.BigBarrel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Cleanser;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Fencer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FrostGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Gungnir;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Hush;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Hydra;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Karasawa;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Lauria;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Madness;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.MaxThunder;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Spark;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.SuperShotgun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Supernova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.ThinLine;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Trench;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Volcano;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Defender;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarpBlade;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Evolution extends InventorySpell{

    {
        image = ItemSpriteSheet.ALCHEMIZE;

        bones = false;
    }

    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return (item instanceof Greatsword ||
                item instanceof Greatshield ||
                item instanceof Glaive ||
                item instanceof Hush ||
                item instanceof Lauria ||
                item instanceof ThinLine ||
                item instanceof SuperShotgun ||
                item instanceof Hydra ||
                item instanceof AAWSM ||
                item instanceof Spark ||
                item instanceof Supernova ||
                item instanceof Cleanser ||
                item instanceof FrostGun);
    }

    @Override
    protected void onItemSelected(Item item) {

        Item result = changeItem(item);

        if (result == null){
            if (item instanceof FirearmWeapon && ((FirearmWeapon) item).checkLoader() != null) {
                GLog.n(Messages.get(this, "cant_loader"));
            } else {
                //This shouldn't ever trigger
                GLog.n( Messages.get(this, "nothing") );
            }
            curItem.collect( curUser.belongings.backpack );
        } else {
            if (item.isEquipped(Dungeon.hero)){
                item.cursed = false; //to allow it to be unequipped
                ((EquipableItem)item).doUnequip(Dungeon.hero, false);
                ((EquipableItem)result).doEquip(Dungeon.hero);
            } else {
                item.detach(Dungeon.hero.belongings.backpack);
                if (!result.collect()){
                    Dungeon.level.drop(result, curUser.pos).sprite.drop();
                }
            }
            if (result.isIdentified()){
                Catalog.setSeen(result.getClass());
            }
            Transmuting.show(curUser, item, result);
            curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
            GLog.p( Messages.get(this, "evolve") );
        }

    }

    public static Item changeItem( Item item ){
        if (item instanceof Greatsword ||
            item instanceof Glaive ||
            item instanceof Greatshield) {
            return changeWeapon((Weapon) item);
        } else if ((item instanceof Hush ||
                   item instanceof Lauria ||
                   item instanceof SuperShotgun ||
                   item instanceof ThinLine ||
                   item instanceof Hydra ||
                   item instanceof AAWSM ||
                   item instanceof Spark ||
                   item instanceof Karasawa ||
                   item instanceof Cleanser ||
                   item instanceof FrostGun) && ((FirearmWeapon) item).checkLoader() == null) {
            return changeWeapon((Weapon) item);
        } else {
            return null;
        }
    }

    private static Weapon changeWeapon( Weapon w ) {
        Weapon n;

        if (w instanceof Greatsword) {
            n = new WarpBlade();
        } else if (w instanceof Glaive) {
            n = new Gungnir();
        } else if (w instanceof Greatshield) {
            n = new Defender();
        } else if (w instanceof Hush) {
            n = new Fencer();
        } else if (w instanceof Lauria) {
            n = new Aria();
        } else if (w instanceof ThinLine) {
            n = new Trench();
        } else if (w instanceof SuperShotgun) {
            n = new MaxThunder();
        } else if (w instanceof Hydra || w instanceof AAWSM) {
            n = new BigBarrel();
        } else if (w instanceof Spark || w instanceof Karasawa) {
            n = new Supernova();
        } else if (w instanceof Cleanser) {
            n = new Volcano();
        } else if (w instanceof FrostGun) {
            n = new Madness();
        } else {
            return null;
        }

        int level = w.level();
        if (w.curseInfusionBonus) level--;
        if (level > 0) {
            n.upgrade( level );
        } else if (level < 0) {
            n.degrade( -level );
        }

        n.enchantment = w.enchantment;
        n.curseInfusionBonus = w.curseInfusionBonus;
        n.masteryPotionBonus = w.masteryPotionBonus;
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        n.augment = w.augment;

        /*
        if (w instanceof FirearmWeapon && ((FirearmWeapon) w).checkLoader() != null) {
            ((FirearmWeapon) n).affixLoader(new SpeedLoader());
        }
        */

        return n;
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
        return 120;
    }

}
