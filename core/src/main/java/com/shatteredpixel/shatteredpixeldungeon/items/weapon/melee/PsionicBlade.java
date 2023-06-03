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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SatelliteCannon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PsionicBlade extends MeleeWeapon {

    {
        image = ItemSpriteSheet.PSIONIC_BLADE1;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 1;

        bones = false;
        unique = true;
    }

    public static final String AC_ZAP     	= "zap";
    public static final String AC_BLAST    	= "blast";
    public static final String AC_ANKH  	= "ankh";
    private static final String CHARGE1     = "energy1";
    private static final String CHARGE2     = "energy2";
    private static final String ANKHCOUNT   = "ankh";
    private int energyLibrarian = 100;
    private int energyDevastator = 3;
    private int AnkhCount = 0;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            if (Dungeon.hero.subClass == HeroSubClass.LIBRARIAN) {
                actions.add("zap");
            } else if (Dungeon.hero.subClass == HeroSubClass.DEVASTATOR) {
                actions.add("blast");
            } else if (Dungeon.hero.subClass == HeroSubClass.IMMORTAL) {
                actions.add("ankh");
            }
        }
        return actions;
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
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ANKHCOUNT, AnkhCount);
        bundle.put(CHARGE1, ACC);
        bundle.put(CHARGE2, DLY);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        AnkhCount = bundle.getInt(ANKHCOUNT);
        ACC = bundle.getFloat(CHARGE1);
        DLY = bundle.getFloat(CHARGE2);

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
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ZAP)) {
            usesTargeting = true;
            curUser = hero;
            curItem = this;
            GameScene.selectCell(spell);
        } else if (action.equals(AC_BLAST)) {
            usesTargeting = true;
            curUser = hero;
            curItem = this;
            GameScene.selectCell(spell);
        } else if (action.equals(AC_ANKH)) {

        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (Dungeon.hero.subClass == HeroSubClass.LIBRARIAN) {
            switch (Random.Int(6)) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
            }
        } else if (Dungeon.hero.subClass == HeroSubClass.DEVASTATOR) {
            int mobCount;
            if (attacker instanceof Hero) {
                mobCount = 1;
                for (Mob mob : (Mob[]) Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (Dungeon.level.adjacent(mob.pos, defender.pos) && mob.alignment != Char.Alignment.ALLY) {
                        mob.damage(Dungeon.hero.damageRoll() - Math.max(defender.drRoll(), defender.drRoll()), this);
                        mobCount++;
                    }
                }
            } else {
                mobCount = 1;
            }
            if (mobCount == 1) {
                damage *= 1.5f;
            }
        } else if (Dungeon.hero.subClass == HeroSubClass.IMMORTAL) {
            switch (AnkhCount) {
                case 1:
                case 2:
                case 3:
                case 4:
                default:
                    break;
            }
        } else {

        }

        return super.proc(attacker, defender, damage);
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

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(SatelliteCannon.class, "prompt");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return canUseItem(item);
        }

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                if (item instanceof Ankh) {
                    AnkhCount++;
                    selectItem(item);
                } else {
                    GLog.w(Messages.get(this, "itemfail", new Object[0]), new Object[0]);
                }
            }
        }

        public void selectItem( Item item ) {
            Hero hero = Dungeon.hero;
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
            hero.busy();
            hero.spend( Actor.TICK );

            item.detach(hero.belongings.backpack);
            GLog.i( Messages.get(this, "absorb_item") );
        }

        public boolean canUseItem(Item item){
            return (item instanceof Ankh);
        }
    };

    private CellSelector.Listener spell = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer cell ) {
            if (cell != null) {
                int c = cell;
                if (Dungeon.level.map[c] != Terrain.WALL && Dungeon.level.heroFOV[c]) {
                    if (Dungeon.level.pit[c]) {
                        GameScene.add(Blob.seed(c, 2, Freezing.class));
                    } else {
                        if (Dungeon.level.water[c]) {
                            if (buffedLvl() >= 10) {
                                GameScene.add(Blob.seed(c, 300 + 20 * buffedLvl(), Blizzard.class));
                            }
                            GameScene.add(Blob.seed(c, 20+buffedLvl(), Freezing.class));
                        } else {
                            if (buffedLvl() >= 10) {
                                GameScene.add(Blob.seed(c, 150 + 10 * buffedLvl(), Blizzard.class));
                            }
                            GameScene.add(Blob.seed(c, 5+buffedLvl(), Freezing.class));
                        }
                        Sample.INSTANCE.play(Assets.Sounds.SHATTER);
                        Splash.at( c, 0xFFB2D6FF, 5);
                    }
                } else {
                    GLog.w( Messages.get(this, "cannot_cast"));
                }
                Buff.affect(hero, FrostImbue.class, Math.min(10+2*buffedLvl(), 50));
                //Buff.affect(hero, SpellBookCoolDown.class, Math.max(100f-5*buffedLvl(), 50f));
                Invisibility.dispel();
                curUser.spend( Actor.TICK );
                curUser.busy();
                ((HeroSprite)curUser.sprite).read();
                Sample.INSTANCE.play(Assets.Sounds.READ);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

}
