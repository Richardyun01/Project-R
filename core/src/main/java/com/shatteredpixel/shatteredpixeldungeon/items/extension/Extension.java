
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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class Extension extends KindofMisc {

    protected Buff buff;

    private String gem;

    //rings cannot be 'used' like other equipment, so they ID purely based on exp
    private float levelsToID = 1;

    public Extension() {
        super();
        reset();
    }

    //anonymous Extensions are always IDed, do not affect ID status,
    //and their sprite is replaced by a placeholder if they are not known,
    //useful for items that appear in UIs, or which are only spawned for their effects
    protected boolean anonymous = false;
    public void anonymize(){
        if (!isKnown()) image = ItemSpriteSheet.EXTENSION_HOLDER;
        anonymous = true;
    }

    public void reset() {
        super.reset();
        levelsToID = 1;
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

    public boolean isKnown() {
        return anonymous;
    }

    public void setKnown() {
        if (!anonymous) {
            if (Dungeon.hero.isAlive()) {
                Catalog.setSeen(getClass());
            }
        }
    }

    @Override
    public String name() {
        return isKnown() ? super.name() : Messages.get(Extension.class, gem);
    }

    @Override
    public String info(){

        String desc = isKnown() ? super.desc() : Messages.get(this, "unknown_desc");

        if (cursed && isEquipped( Dungeon.hero )) {
            desc += "\n\n" + Messages.get(Extension.class, "cursed_worn");

        } else if (cursed && cursedKnown) {
            desc += "\n\n" + Messages.get(Extension.class, "curse_known");

        } else if (!isIdentified() && cursedKnown){
            desc += "\n\n" + Messages.get(Extension.class, "not_cursed");

        }

        if (isKnown()) {
            desc += "\n\n" + statsInfo();
        }

        return desc;
    }

    protected String statsInfo(){
        return "";
    }

    @Override
    public Item upgrade() {
        super.upgrade();

        if (Random.Int(3) == 0) {
            cursed = false;
        }

        return this;
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && isKnown();
    }

    @Override
    public Item identify( boolean byHero ) {
        setKnown();
        levelsToID = 0;
        return super.identify(byHero);
    }

    @Override
    public Item random() {
        //+0: 66.67% (2/3)
        //+1: 26.67% (4/15)
        //+2: 6.67%  (1/15)
        int n = 0;
        if (Random.Int(3) == 0) {
            n++;
            if (Random.Int(5) == 0){
                n++;
            }
        }
        level(n);

        //30% chance to be cursed
        if (Random.Float() < 0.3f) {
            cursed = true;
        }

        return this;
    }

    public static HashSet<Class<? extends Extension>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Extension>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == Generator.Category.Extension.classes.length;
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

    private static final String LEVELS_TO_ID    = "levels_to_ID";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEVELS_TO_ID, levelsToID );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        levelsToID = bundle.getFloat( LEVELS_TO_ID );
    }

    public void onHeroGainExp( float levelPercent, Hero hero ){
        if (isIdentified() || !isEquipped(hero)) return;
        levelPercent *= Talent.itemIDSpeedFactor(hero, this);
        //becomes IDed after 1 level
        levelsToID -= levelPercent;
        if (levelsToID <= 0){
            identify();
            GLog.p( Messages.get(Extension.class, "identify", title()) );
            Badges.validateItemLevelAquired( this );
        }
    }

    @Override
    public int buffedLvl() {
        int lvl = super.buffedLvl();
        if (Dungeon.hero.buff(EnhancedExtensions.class) != null){
            lvl++;
        }
        return lvl;
    }

    public static int getBonus(Char target, Class<?extends ExtensionBuff> type){
        if (target.buff(MagicImmune.class) != null) return 0;
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