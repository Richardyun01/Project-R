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
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLesserHealing;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo5;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class CarrollCombo extends Buff implements ActionIndicator.Action {

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
                Random.Int(20) < Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP)) {
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
        return Messages.get(this, "desc", count);
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
    public Image actionIcon() {
        Image icon;
        if (((Hero)target).belongings.weapon() != null){
            icon = new ItemSprite(((Hero)target).belongings.weapon().image, null);
        } else {
            icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.WEAPON_HOLDER; }});
        }

        icon.tint(getHighestMove().tintColor);
        return icon;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndCombo5(this));
    }

    public enum ComboMove {
        SUPPLY   (10, 0xFF00FF00), // skill-1
        RECON  (20, 0xFFFFFF00),  // skill-2
        ANNHILATION   (30, 0xFFFF0000); // skill-3

        public int comboReq, tintColor;

        ComboMove(int comboReq, int tintColor){
            this.comboReq = comboReq;
            this.tintColor = tintColor;
        }

        public String desc(int count){
            return Messages.get(this, name()+"_desc");
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

    public void useMove(ComboMove move){
        if (move == ComboMove.SUPPLY) {
            if (Dungeon.hero.buff(Talent.SupplyCoolDown.class) == null) {
                new PotionOfLesserHealing().collect();
                new SmallRation().collect();
                Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(CarrollCombo.class, "add", new Object[0]), new Object[0]);
                Buff.affect(Dungeon.hero, Talent.SupplyCoolDown.class, 1000-100*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                ((Hero) target).spendAndNext(Actor.TICK);
                Dungeon.hero.busy();
                detach();
            } else {
                GLog.n(Messages.get(this, "cannot_use"));
            }
        } else if (move == ComboMove.RECON) {
            if (Dungeon.hero.buff(Talent.ReconCoolDown.class) == null) {
                Buff.affect(Dungeon.hero, Awareness.class, 2f);
                Buff.affect(Dungeon.hero, MindVision.class, 2f);
                Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(CarrollCombo.class, "rec", new Object[0]), new Object[0]);
                Buff.affect(Dungeon.hero, Talent.ReconCoolDown.class, 2000-200*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                Sample.INSTANCE.play(Assets.Sounds.READ);
                ((Hero) target).spendAndNext(Actor.TICK);
                detach();
            } else {
                GLog.n(Messages.get(this, "cannot_use"));
            }
        } else if (move == ComboMove.ANNHILATION){
            if (Dungeon.hero.buff(Talent.AnnihilationCoolDown.class) == null && Dungeon.hero.buff(MindVision.class) == null) {
                for (Mob mob : (Mob[]) Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob.alignment != Char.Alignment.ALLY &&
                        Dungeon.level.heroFOV[mob.pos]) {
                        if (mob.properties().contains(Char.Property.BOSS) ||
                            mob.properties().contains(Char.Property.MINIBOSS)) {
                            mob.damage(Random.NormalIntRange(100, 101), Dungeon.hero);
                        } else {
                            mob.damage(Random.NormalIntRange(5000, 8000), Dungeon.hero);
                        }
                    }
                }
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                GameScene.flash(-2130706433);
                Camera.main.shake(2.0f, 0.5f);
                Buff.affect(Dungeon.hero, Talent.AnnihilationCoolDown.class, 3000-300*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIP));
                ((Hero) target).spendAndNext(Actor.TICK);
                detach();
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
            case SUPPLY:
            case RECON:
            case ANNHILATION:
                dmgMulti = 0f;
                break;
        }

        if (hero.attack(enemy, dmgMulti, dmgBonus, Char.INFINITE_ACCURACY)){
            //special on-hit effects
            switch (moveBeingUsed) {
                case SUPPLY:
                case RECON:
                case ANNHILATION:
                default:
                    //nothing
                    break;
            }
        }

        Invisibility.dispel();

        //Post-attack behaviour
        switch(moveBeingUsed){
            case SUPPLY:
            case RECON:
            case ANNHILATION:
            default:
                detach();
                ActionIndicator.clearAction(CarrollCombo.this);
                hero.spendAndNext(hero.attackDelay());
                break;
        }

    }

    private CellSelector.Listener listener = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            final Char enemy = Actor.findChar( cell );
            if (enemy == null
                    || enemy == target
                    || !Dungeon.level.heroFOV[cell]) {
                GLog.w(Messages.get(Combo.class, "bad_target"));

            } else if (!((Hero)target).canAttack(enemy)){
                Ballistica c = new Ballistica(target.pos, enemy.pos, Ballistica.PROJECTILE);
                if (c.collisionPos == enemy.pos){
                    final int leapPos = c.path.get(c.dist-1);
                    if (!Dungeon.level.passable[leapPos]){
                        GLog.w(Messages.get(Combo.class, "bad_target"));
                    } else {
                        Dungeon.hero.busy();
                        target.sprite.jump(target.pos, leapPos, new Callback() {
                            @Override
                            public void call() {
                                target.move(leapPos);
                                Dungeon.level.occupyCell(target);
                                Dungeon.observe();
                                GameScene.updateFog();
                                target.sprite.attack(cell, new Callback() {
                                    @Override
                                    public void call() {
                                        doAttack(enemy);
                                    }
                                });
                            }
                        });
                    }
                } else {
                    GLog.w(Messages.get(Combo.class, "bad_target"));
                }
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
