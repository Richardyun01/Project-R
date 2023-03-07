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

package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Gungnir;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Reiterpallasch;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class SpeedLoader extends Item {

    public static final String AC_AFFIX = "AFFIX";

    {
        image = ItemSpriteSheet.SPEED_LOADER;

        cursedKnown = levelKnown = true;
        unique = true;
        bones = false;

        defaultAction = AC_AFFIX;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_AFFIX);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_AFFIX)){
            curItem = this;
            GameScene.selectItem(gunSelector);
        }
    }

    @Override
    //scroll of upgrade can be used directly once, same as upgrading armor the seal is affixed to then removing it.
    public boolean isUpgradable() {
        if (hero.hasTalent(Talent.ADVANCED_ACCESSORY) && hero.pointsInTalent(Talent.ADVANCED_ACCESSORY) >= 2) {
            return level() == 0;
        } else {
            return false;
        }
    }

    protected static WndBag.ItemSelector gunSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return  Messages.get(SpeedLoader.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof FirearmWeapon && !(item instanceof Reiterpallasch) && !(item instanceof Gungnir);
        }

        @Override
        public void onSelect( Item item ) {
            SpeedLoader loader = (SpeedLoader) curItem;
            if (item != null && (item instanceof FirearmWeapon && !(item instanceof Reiterpallasch) && !(item instanceof Gungnir))) {
                FirearmWeapon gun = (FirearmWeapon)item;
                if (!gun.levelKnown){
                    GLog.w(Messages.get(SpeedLoader.class, "unknown_gun"));

                } else if (gun.cursed){
                    GLog.w(Messages.get(SpeedLoader.class, "cursed_gun"));

                } else {
                    GLog.p(Messages.get(SpeedLoader.class, "affix"));
                    Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                    Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
                    gun.affixLoader((SpeedLoader)curItem);
                    curItem.detach(Dungeon.hero.belongings.backpack);
                }
            }
        }
    };

    public static float reloadMultiplier() {
        float fastReload = 0.5f;
        if (hero.hasTalent(Talent.ADVANCED_ACCESSORY) && hero.pointsInTalent(Talent.ADVANCED_ACCESSORY) >= 1) {
            fastReload -= 0.1f;
        }

        return fastReload;
    }

}

