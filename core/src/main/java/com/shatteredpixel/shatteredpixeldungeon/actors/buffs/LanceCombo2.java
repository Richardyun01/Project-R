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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo3;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class LanceCombo2 extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;
    }

    private int count = 0;

    @Override
    public int icon() {
        return BuffIndicator.LANCE_COMBO;
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

    public void hit( Char enemy ) {

        count++;
        if (Dungeon.hero.hasTalent(Talent.BURNING_BLOOD) && Random.Int(10) < Dungeon.hero.pointsInTalent(Talent.BURNING_BLOOD)) {
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
    private static final String BLEEDING_USED = "bleeding_used";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNT, count);
        bundle.put(BLEEDING_USED, bleedingBulletUsed);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt( COUNT );
        bleedingBulletUsed = bundle.getBoolean(BLEEDING_USED);

        if (getHighestMove() != null) ActionIndicator.setAction(this);
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.USE_TERCIO;
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
        GameScene.show(new WndCombo3(this));
    }

    public enum ComboMove {
        FAST_DRAW   (10, 0xFF00FF00), // skill-1
        MAD_CHARGE  (20, 0xFFFFFF00),  // skill-2
        BLEEDING_BULLET   (30, 0xFFFF0000); // skill-3

        public int comboReq, tintColor;

        ComboMove(int comboReq, int tintColor){
            this.comboReq = comboReq;
            this.tintColor = tintColor;
        }

        public String desc(int count){
            switch (this) {
                default:
                    return Messages.get(this, name()+"_desc");
                case FAST_DRAW:
                    return Messages.get(this, name()+"_desc", 4+Dungeon.hero.pointsInTalent(Talent.FASTER_HAND));
                case MAD_CHARGE:
                    return Messages.get(this, name()+"_desc", 200+10*Dungeon.hero.pointsInTalent(Talent.RUTHLESS_CHARGE));
            }
        }

    }

    private boolean bleedingBulletUsed = false;

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
        if (move == ComboMove.BLEEDING_BULLET) {
            if (Dungeon.hero.buff(InfiniteBullet.class) == null) {
                bleedingBulletUsed = true;
                Invisibility.dispel();
                SpellSprite.show(Dungeon.hero, SpellSprite.BLOOD, 1, 0.77f, 0.9f);
                Buff.affect(target, LanceBleedingBullet.class, LanceBleedingBullet.DURATION);
                ((Hero)target).spendAndNext(Actor.TICK);
                Dungeon.hero.busy();
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
            case FAST_DRAW:
                dmgMulti = 0f;
                break;
            case MAD_CHARGE:
                dmgMulti = 2f + 0.1f * hero.pointsInTalent(Talent.RUTHLESS_CHARGE);
                break;
        }

        if (hero.attack(enemy, dmgMulti, dmgBonus, Char.INFINITE_ACCURACY)){
            //special on-hit effects
            switch (moveBeingUsed) {
                case FAST_DRAW:
                    if (hero.buff(LanceBleedingBullet.class) == null) {
                        if (!wasAlly) hit(enemy);
                        //trace a ballistica to our target (which will also extend past them
                        Ballistica trajectory = new Ballistica(target.pos, enemy.pos, Ballistica.STOP_TARGET);
                        //trim it to just be the part that goes past them
                        trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                        int dist = 4;
                        WandOfBlastWave.throwChar(enemy, trajectory, dist, true, false, hero.getClass());
                        Buff.affect(enemy, NoEnergy.class).set(3, 3);
                        Buff.affect(hero, InfiniteBullet.class, 4+hero.pointsInTalent(Talent.FASTER_HAND));
                    } else {
                        GLog.n(Messages.get(this, "cannot_use"));
                    }
                    break;
                case MAD_CHARGE:
                    if (hero.hasTalent(Talent.RUTHLESS_CHARGE)) {
                        Buff.affect(enemy, Cripple.class, hero.pointsInTalent(Talent.RUTHLESS_CHARGE));
                    }
                    break;
                case BLEEDING_BULLET:
                default:
                    //nothing
                    break;
            }
        }

        Invisibility.dispel();

        //Post-attack behaviour
        switch(moveBeingUsed){
            default:
                detach();
                ActionIndicator.clearAction(LanceCombo2.this);
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
                    || !Dungeon.level.heroFOV[cell]
                    || target.isCharmedBy( enemy )) {
                GLog.w(Messages.get(Combo.class, "bad_target"));
            } else if (!((Hero)target).canAttack(enemy)){
                switch (moveBeingUsed) {
                    case MAD_CHARGE:
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
                        break;
                    case FAST_DRAW:
                    case BLEEDING_BULLET:
                    default:
                        if (Dungeon.level.distance(target.pos, enemy.pos) > 1){
                            GLog.w(Messages.get(Combo.class, "bad_target"));
                        } else {
                            Ballistica d = new Ballistica(target.pos, enemy.pos, Ballistica.PROJECTILE);
                            if (d.collisionPos == enemy.pos){
                                final int leapPos = d.path.get(d.dist-1);
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
                        }
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

