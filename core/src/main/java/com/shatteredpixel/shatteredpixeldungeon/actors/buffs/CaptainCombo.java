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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLesserHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo5;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CaptainCombo extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;
    }

    private int count = 0;

    @Override
    public int icon() {
        return BuffIndicator.CARROLL_COMBO;
    }

    @Override
    public void tintIcon(Image icon) {
        ComboMove move = getHighestMove();
        if (move != null){
            icon.hardlight(move.tintColor & 0x00FFFFFF);
        } else {
            icon.resetColor();
        }
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)1);
    }

    public void kill( Char enemy ) {

        count++;
        if (Dungeon.hero.hasTalent(Talent.ENHANCED_SHIP) &&
                Random.Int(10) < Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP)) {
            count++;
        }

        if ((getHighestMove() != null)) {

            ActionIndicator.setAction( this );

            GLog.p( Messages.get(this, "combo", count) );

        }

        BuffIndicator.refreshHero(); //refresh the buff visually on-hit

    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public boolean act() {
        spend(TICK);
        return true;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", count, 0.3f*count);
    }

    private static final String COUNT = "count";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNT, count);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt( COUNT );

        if (getHighestMove() != null) ActionIndicator.setAction(this);
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.USE_SHIP;
    }

    @Override
    public Visual secondaryVisual() {
        BitmapText txt = new BitmapText(PixelScene.pixelFont);
        txt.text( Integer.toString(count) );
        txt.hardlight(CharSprite.POSITIVE);
        txt.measure();
        return txt;
    }

    @Override
    public int indicatorColor() {
        return 0x660000;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndCombo5(this));
    }

    public enum ComboMove {
        CAS         (5, 0xFF00FF00),  // skill-1
        SUPPLYI     (10, 0xFFCCFF00), // skill-2
        RECON       (20, 0xFFFFFF00), // skill-3
        SUPPLYII    (30, 0xFFFFCC00), // skill-4
        ANNIHILATION(40, 0xFFFF0000); // skill-5

        public int comboReq, tintColor;

        ComboMove(int comboReq, int tintColor){
            this.comboReq = comboReq;
            this.tintColor = tintColor;
        }

        public String desc(int count){
            switch (this){
                default:
                    return Messages.get(this, name()+"_desc");
                case CAS:
                    return Messages.get(this, name()+"_desc", 50-5*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                case SUPPLYI:
                    return Messages.get(this, name()+"_desc", 500-50*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                case RECON:
                    return Messages.get(this, name()+"_desc", 1000-100*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                case SUPPLYII:
                    return Messages.get(this, name()+"_desc", 1500-150*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                case ANNIHILATION:
                    return Messages.get(this, name()+"_desc", 2500-250*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
            }
        }

    }

    public ComboMove getHighestMove(){
        ComboMove best = null;
        for (ComboMove move : ComboMove.values()){
            if (count >= move.comboReq){
                best = move;
            }
        }
        return best;
    }

    public int getComboCount(){
        return count;
    }

    public boolean canUseMove(ComboMove move){
        return move.comboReq <= count;
    }

    public void useMove(ComboMove move) {
        if (move == ComboMove.CAS) {
            if (Dungeon.hero.buff(Talent.CasCoolDown.class) == null) {
                moveBeingUsed = move;
                GameScene.selectCell(listener);
            } else {
                GLog.n(Messages.get(this, "cannot_use"));
            }
        } else if (move == ComboMove.SUPPLYI) {
            if (Dungeon.hero.buff(Talent.SupplyICoolDown.class) == null) {
                new PotionOfLesserHealing().collect();
                new SmallRation().collect();
                if (Dungeon.hero.pointsInTalent(Talent.ADVANCED_SYSTEM) >= 3) {
                    int n = Random.Int(10);
                    switch (n) {
                        case 0:
                            new StoneOfIntuition().collect();
                            break;
                        case 1:
                            new StoneOfClairvoyance().collect();
                            break;
                        case 2:
                            new Stormvine.Seed().collect();
                            break;
                        case 3:
                            new Icecap.Seed().collect();
                            break;
                        case 4:
                            new ThrowingStone().collect();
                            break;
                        case 5:
                            new PotionOfLesserHealing().collect();
                            break;
                        case 6:
                            new SmallRation().collect();
                            break;
                        case 7:
                            new Honeypot.ShatteredPot().collect();
                            break;
                        case 8:
                            new Torch().collect();
                            break;
                        case 9:
                            new ThrowingKnife().collect();
                            break;
                    }
                }
                Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(CaptainCombo.class, "add", new Object[0]), new Object[0]);
                Buff.affect(Dungeon.hero, Talent.SupplyICoolDown.class, 500-50*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                ((Hero) target).spendAndNext(Actor.TICK);
                Dungeon.hero.busy();
                count -= 10;
            } else {
                GLog.n(Messages.get(this, "cannot_use"));
            }
        } else if (move == ComboMove.RECON) {
            if (Dungeon.hero.buff(Talent.ReconCoolDown.class) == null) {
                Buff.affect(Dungeon.hero, Awareness.class, 2f);
                Buff.affect(Dungeon.hero, MindVision.class, 2f);
                Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(CaptainCombo.class, "rec", new Object[0]), new Object[0]);
                Buff.affect(Dungeon.hero, Talent.ReconCoolDown.class, 1000 - 100 * Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                Sample.INSTANCE.play(Assets.Sounds.READ);
                ((Hero) target).spendAndNext(Actor.TICK);
                count -= 20;
            } else {
                GLog.n(Messages.get(this, "cannot_use"));
            }
        } else if (move == ComboMove.SUPPLYII) {
            if (Dungeon.hero.buff(Talent.SupplyIICoolDown.class) == null) {
                new PotionOfHealing().collect();
                new Pasty().collect();
                if (Dungeon.hero.pointsInTalent(Talent.ADVANCED_SYSTEM) >= 3) {
                    int n = Random.Int(10);
                    switch (n) {
                        case 0:
                            new ScrollOfIdentify().collect();
                            break;
                        case 1:
                            new ScrollOfRemoveCurse().collect();
                            break;
                        case 2:
                            new ScrollOfMagicMapping().collect();
                            break;
                        case 3:
                            new Sungrass.Seed().collect();
                            break;
                        case 4:
                            new Bomb().collect();
                            break;
                        case 5:
                            new PotionOfHealing().collect();
                            break;
                        case 6:
                            new Pasty().collect();
                            break;
                        case 7:
                            new Honeypot().collect();
                            break;
                        case 8:
                            new Stylus().collect();
                            break;
                        case 9:
                            new PotionOfMindVision().collect();
                            break;
                    }
                }
                Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(CaptainCombo.class, "add", new Object[0]), new Object[0]);
                Buff.affect(Dungeon.hero, Talent.SupplyIICoolDown.class, 1500-150*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                ((Hero) target).spendAndNext(Actor.TICK);
                Dungeon.hero.busy();
                count -= 30;
            } else {
                GLog.n(Messages.get(this, "cannot_use"));
            }
        } else if (move == ComboMove.ANNIHILATION){
            if (Dungeon.hero.buff(Talent.AnnihilationCoolDown.class) == null && Dungeon.hero.buff(MindVision.class) == null) {
                for (Mob mob : (Mob[]) Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob.alignment != Char.Alignment.ALLY &&
                        Dungeon.level.heroFOV[mob.pos]) {
                        if ((mob.properties().contains(Char.Property.BOSS) ||
                            mob.properties().contains(Char.Property.MINIBOSS)) && Dungeon.hero.pointsInTalent(Talent.ADVANCED_SYSTEM)>=1) {
                            mob.damage(Random.NormalIntRange(100, 101), Dungeon.hero);
                        } else {
                            mob.damage(Random.NormalIntRange(5000, 8000), Dungeon.hero);
                        }
                    }
                }
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                GameScene.flash(-2130706433);
                Camera.main.shake(2.0f, 0.5f);
                Buff.affect(Dungeon.hero, Talent.AnnihilationCoolDown.class, 2500-250*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                ((Hero) target).spendAndNext(Actor.TICK);
                count -= 40;
            } else {
                GLog.n(Messages.get(this, "cannot_use"));
            }
        } else {
            moveBeingUsed = move;
            GameScene.selectCell(listener);
        }
    }

    private static ComboMove moveBeingUsed;

    private void doAttack(final Char enemy) {

        AttackIndicator.target(enemy);

        boolean wasAlly = enemy.alignment == target.alignment;
        Hero hero = (Hero) target;

        float dmgMulti = 1f;
        int dmgBonus = 0;

        //variance in damage dealt
        switch (moveBeingUsed) {
            case CAS:
            case SUPPLYI:
            case RECON:
            case SUPPLYII:
            case ANNIHILATION:
                dmgMulti = 0f;
                break;
        }

        if (hero.attack(enemy, dmgMulti, dmgBonus, Char.INFINITE_ACCURACY)){
            //special on-hit effects
            switch (moveBeingUsed) {
                case CAS:
                    for (int n : PathFinder.NEIGHBOURS9) {
                        int c = enemy.pos + n;
                        if (Dungeon.level.heroFOV[c]) {
                            CellEmitter.get(c).burst(SmokeParticle.FACTORY, 3);
                            CellEmitter.center(c).burst(BlastParticle.FACTORY, 3);
                        }
                        if (Dungeon.level.flamable[c]) {
                            Dungeon.level.destroy(c);
                            GameScene.updateMap(c);
                        }
                        Char ch = Actor.findChar(c);
                        if (ch != null && ch != hero) {
                            int damage;
                            if (hero.pointsInTalent(Talent.ADVANCED_SYSTEM) >= 2) {
                                damage = 55;
                            } else {
                                damage = 35;
                            }
                            ch.damage(damage, this);
                        }
                    }
                    break;
                case SUPPLYI:
                case RECON:
                case SUPPLYII:
                case ANNIHILATION:
                default:
                    //nothing
                    break;
            }
        }

        Invisibility.dispel();

        //Post-attack behaviour
        switch(moveBeingUsed){
            case CAS:
                Buff.affect(Dungeon.hero, Talent.CasCoolDown.class, 50-5*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                count -= 5;
                hero.spendAndNext(hero.attackDelay());
                break;
            case SUPPLYI:
            case RECON:
            case SUPPLYII:
            case ANNIHILATION:
            default:
                detach();
                ActionIndicator.clearAction(CaptainCombo.this);
                hero.spendAndNext(hero.attackDelay());
                break;
        }

    }

    private CellSelector.Listener listener = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            final Char enemy = Actor.findChar( cell );
            if (enemy == Dungeon.hero){
                GLog.w(Messages.get(this, "bad_target"));
                return;
            }
            if (enemy == null
                    || enemy == target
                    || !Dungeon.level.heroFOV[cell]) {
                GLog.w(Messages.get(Combo.class, "bad_target"));

            } else {
                Dungeon.hero.busy();
                target.sprite.attack(cell, new Callback() {
                    @Override
                    public void call() {
                        doAttack(enemy);
                    }
                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Combo.class, "prompt");
        }
    };
}
