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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
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

    {

        defaultAction = AC_ZAP;
        usesTargeting = true;

        image = ItemSpriteSheet.LUNGE;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.0f;
        tier = 2;

    }

    public static final String AC_ZAP = "ZAP";
    private static final String CHARGE = "energy";
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
                    lunge.fx(ballistica, new Callback() {
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
    private int energy = 3;
    private int energychargeCap = 3;
    protected int collisionProperties = 6;

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

    @Override
    public String defaultAction() {
        return AC_ZAP;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_ZAP)) {
            if (this.energy > 0) {
                if (!this.cursed) {
                    this.cursedKnown = true;
                    GameScene.selectCell(zapper);
                    return;
                }
                ((Burning) Buff.affect(Dungeon.hero, Burning.class)).reignite(Dungeon.hero, 4.0f);
                this.cursedKnown = true;
                this.energy--;
            } else {
                GLog.w(Messages.get(this, "fizzles"));
            }
        }
        if (action.equals(AC_ABILITY)) {
            if (!isEquipped(hero)) {
                if (hero.hasTalent(Talent.SWIFT_EQUIP)){
                    if (hero.buff(Talent.SwiftEquipCooldown.class) == null
                            || hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()){
                        execute(hero, AC_EQUIP);
                    } else {
                        GLog.w(Messages.get(MeleeWeapon.class, "ability_need_equip"));
                        usesTargeting = false;
                    }
                } else {
                    GLog.w(Messages.get(MeleeWeapon.class, "ability_need_equip"));
                    usesTargeting = false;
                }
            } else if (STRReq() > hero.STR()){
                GLog.w(Messages.get(MeleeWeapon.class, "ability_low_str"));
                usesTargeting = false;
            } else if (hero.belongings.weapon == this &&
                    (Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge) < abilityChargeUse(hero, null)) {
                GLog.w(Messages.get(MeleeWeapon.class, "ability_no_charge"));
                usesTargeting = false;
            } else if (hero.belongings.secondWep == this &&
                    (Buff.affect(hero, Charger.class).secondCharges + Buff.affect(hero, Charger.class).secondPartialCharge) < abilityChargeUse(hero, null)) {
                GLog.w(Messages.get(MeleeWeapon.class, "ability_no_charge"));
                usesTargeting = false;
            } else {

                if (targetingPrompt() == null) {
                    carrollAbility(hero, hero.pos);
                    updateQuickslot();
                } else {
                    usesTargeting = useTargeting();
                    GameScene.selectCell(new CellSelector.Listener() {
                        @Override
                        public void onSelect(Integer cell) {
                            if (cell != null) {
                                carrollAbility(hero, cell);
                                updateQuickslot();
                            }
                        }

                        @Override
                        public String prompt() {
                            return targetingPrompt();
                        }
                    });
                }
            }
        }
    }

    public String statsInfo() {
        return Messages.get((Object) this, "stats_desc", Integer.valueOf(buffedLvl() + 2), Integer.valueOf((buffedLvl() * 2) + 11));
    }

    public void SPCharge(int charge) {
        if (Random.Int(16) < 2) {
            int chargeTemp2 = this.energy + charge;
            this.energy = chargeTemp2;
            int chargeTemp3 = this.energychargeCap;
            if (chargeTemp3 < chargeTemp2) {
                this.energy = chargeTemp3;
            }
            updateQuickslot();
        }
    }

    public String status() {
        return this.energy + "/" + this.energychargeCap;
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, this.energy);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        int i = this.energychargeCap;
        if (i > 0) {
            this.energy = Math.min(i, bundle.getInt(CHARGE));
        } else {
            this.energy = bundle.getInt(CHARGE);
        }
    }

    public void fx(Ballistica ballistica, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent, 0, curUser.sprite, ballistica.collisionPos.intValue(), callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    public boolean tryToZap(Hero hero, int i) {
        if (hero.buff(MagicImmune.class) != null) {
            GLog.w(Messages.get((Object) this, "no_magic", new Object[0]), new Object[0]);
            return false;
        } else if (this.energy >= 1) {
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
        this.energy--;
        updateQuickslot();
        curUser.spendAndNext(1.0f);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        Dagger.sneakAbility(hero, 6, this);
    }

}