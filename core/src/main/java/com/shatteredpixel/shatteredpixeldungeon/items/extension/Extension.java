
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
/*
package com.shatteredpixel.shatteredpixeldungeon.items.extension;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Extension extends EquipableItem {

    protected Buff buff;

    public Extension() {
        super();
        reset();
    }

    public void reset() {
        super.reset();
    }

    public void activate( Char ch ) {
        if (buff != null){
            buff.detach();
            buff = null;
        }
        buff = buff();
        buff.attachTo( ch );
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

            if (buff != null) {
                buff.detach();
                buff = null;
            }

            return true;

        } else {

            return false;

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public String info(){
        if (cursed && cursedKnown && !isEquipped( Dungeon.hero )) {
            return desc() + "\n\n" + Messages.get(Extension.class, "curse_known");

        } else if (!isIdentified() && cursedKnown && !isEquipped( Dungeon.hero)) {
            return desc()+ "\n\n" + Messages.get(Extension.class, "not_cursed");

        } else {
            return desc();

        }
    }

    protected String statsInfo(){
        return "";
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified();
    }

    @Override
    public Item identify( boolean byHero ) {
        return super.identify(byHero);
    }


    @Override
    public int value() {
        int price = 75;
        if (cursed && cursedKnown) {
            price /= 2;
        }
        if (levelKnown) {
            if (level() > 0) {
                price *= (level() + 1);
            } else if (level() < 0) {
                price /= (1 - level());
            }
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    protected ExtensionBuff buff() {
        return null;
    }


    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
    }

    @Override
    public boolean doEquip( Hero hero ) {

        detach(hero.belongings.backpack);

        if (hero.belongings.extension == null || hero.belongings.extension.doUnequip( hero, true, false )) {

            hero.belongings.extension = this;

            cursedKnown = true;
            if (cursed) {
                equipCursed( hero );
                GLog.n( Messages.get(Extension.class, "equip_cursed") );
            }

            activate(hero);
            Talent.onItemEquipped(hero, this);
            hero.spendAndNext( time2equip( hero ) );
            return true;

        } else {

            collect( hero.belongings.backpack );
            return false;

        }
    }

    @Override
    public int buffedLvl() {
        int lvl = super.buffedLvl();
        return lvl;
    }

    public static int getBonus(Char target, Class<?extends ExtensionBuff> type){
        int bonus = 0;
        for (ExtensionBuff buff : target.buffs(type)) {
            bonus += buff.level();
        }
        return bonus;
    }

    public static int getBuffedBonus(Char target, Class<?extends ExtensionBuff> type){
        if (target.buff(MagicImmune.class) != null) return 0;
        int bonus = 0;
        for (ExtensionBuff buff : target.buffs(type)) {
            bonus += buff.buffedLvl();
        }
        return bonus;
    }

    public int soloBonus(){
        if (cursed){
            return Math.min( 0, Extension.this.level()-2 );
        } else {
            return Extension.this.level()+1;
        }
    }

    public int soloBuffedBonus(){
        if (cursed){
            return Math.min( 0, Extension.this.buffedLvl()-2 );
        } else {
            return Extension.this.buffedLvl()+1;
        }
    }

    public class ExtensionBuff extends Buff {

        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo( target )) {
                //if we're loading in and the hero has partially spent a turn, delay for 1 turn
                if (now() == 0 && cooldown() == 0 && target.cooldown() > 0) spend(TICK);
                return true;
            }
            return false;
        }

        @Override
        public boolean act() {
            spend( TICK );
            return true;
        }

        public int level(){
            return Extension.this.soloBonus();
        }

        public int buffedLvl(){
            return Extension.this.soloBuffedBonus();
        }

    }
}
*/
/**TODO
Camouflage Lv.1~3
Stealth Lv.1~3
Weaponarm Assist Lv.1~3
Additional Ammunition Lv.1~3
**/