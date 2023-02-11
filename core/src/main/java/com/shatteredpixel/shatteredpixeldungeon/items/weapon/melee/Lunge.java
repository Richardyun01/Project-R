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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Lunge extends MeleeWeapon {

    public static final String AC_ZAP = "ZAP";
    private static final String CHARGE = "arts";
    protected static CellSelector.Listener zapper = new CellSelector.Listener() {
        public void onSelect(Integer num) {
            if (num != null && (Lunge.curItem instanceof Lunge)) {
                final Lunge lunge = (Lunge) Lunge.curItem;
                final Ballistica ballistica = new Ballistica(Lunge.curUser.pos, num.intValue(), 7);
                int intValue = ballistica.collisionPos.intValue();
                if (num.intValue() == Lunge.curUser.pos || intValue == Lunge.curUser.pos) {
                    GLog.i(Messages.get(Lunge.class, "self_target", new Object[0]), new Object[0]);
                    return;
                }
                Lunge.curUser.sprite.zap(intValue);
                if (Actor.findChar(num.intValue()) != null) {
                    QuickSlotButton.target(Actor.findChar(num.intValue()));
                } else {
                    QuickSlotButton.target(Actor.findChar(intValue));
                }
                if (lunge.tryToZap(Lunge.curUser, num.intValue())) {
                    lunge.mo7894fx(ballistica, new Callback() {
                        public void call() {
                            lunge.onZap(ballistica);
                        }
                    });
                }
            }
        }

        public String prompt() {
            return Messages.get(Lunge.class, "prompt", new Object[0]);
        }
    };
    private int arts = 3;
    private int artschargeCap = 3;
    protected int collisionProperties = 6;

    public Lunge() {
        this.image = ItemSpriteSheet.LUNGE;
        this.hitSound = Assets.Sounds.HIT;
        this.hitSoundPitch = 1.0f;
        this.tier = 2;
        this.usesTargeting = true;
        this.defaultAction = "ZAP";
    }

    public int max(int i) {
        return (this.tier * 5) + (i * this.tier);
    }

    public int proc(Char charR, Char charR2, int i) {
        SPCharge(1);
        return super.proc(charR, charR2, i);
    }

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add("ZAP");
        return actions;
    }

    public void execute(Hero hero, String str) {
        super.execute(hero, str);
        if (str.equals("ZAP") && this.arts > 0) {
            if (!this.cursed) {
                this.cursedKnown = true;
                GameScene.selectCell(zapper);
                return;
            }
            ((Burning) Buff.affect(Dungeon.hero, Burning.class)).reignite(Dungeon.hero, 4.0f);
            this.cursedKnown = true;
            this.arts--;
        }
    }

    public String statsInfo() {
        return Messages.get((Object) this, "stats_desc", Integer.valueOf(buffedLvl() + 2), Integer.valueOf((buffedLvl() * 2) + 11));
    }

    public void SPCharge(int i) {
        if (Random.Int(17) < 2) {
            int i2 = this.arts + i;
            this.arts = i2;
            int i3 = this.artschargeCap;
            if (i3 < i2) {
                this.arts = i3;
            }
            updateQuickslot();
        }
    }

    public String status() {
        return this.arts + "/" + this.artschargeCap;
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, this.arts);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        int i = this.artschargeCap;
        if (i > 0) {
            this.arts = Math.min(i, bundle.getInt(CHARGE));
        } else {
            this.arts = bundle.getInt(CHARGE);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: fx */
    public void mo7894fx(Ballistica ballistica, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent, 0, curUser.sprite, ballistica.collisionPos.intValue(), callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    public boolean tryToZap(Hero hero, int i) {
        if (hero.buff(MagicImmune.class) != null) {
            GLog.w(Messages.get((Object) this, "no_magic", new Object[0]), new Object[0]);
            return false;
        } else if (this.arts >= 1) {
            return true;
        } else {
            GLog.w(Messages.get((Object) this, "fizzles", new Object[0]), new Object[0]);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onZap(Ballistica ballistica) {
        Char findChar = Actor.findChar(ballistica.collisionPos.intValue());
        if (findChar != null) {
            findChar.damage(Random.Int(buffedLvl() + 2, (buffedLvl() * 2) + 11), this);
            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1.0f, Random.Float(0.87f, 1.15f));
            findChar.sprite.burst(-1, (buffedLvl() / 2) + 2);
        } else {
            Dungeon.level.pressCell(ballistica.collisionPos.intValue());
        }
        this.arts--;
        updateQuickslot();
        curUser.spendAndNext(1.0f);
    }

}