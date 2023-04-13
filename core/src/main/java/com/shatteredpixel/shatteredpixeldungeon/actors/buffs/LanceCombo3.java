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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo4;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class LanceCombo3 extends Buff implements ActionIndicator.Action {

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
        if (enemy.buff(LancePredationTracker.class) != null) {
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
    private static final String TRACK_USED = "track_used";
    private static final String TRACK_COOLDOWN = "track_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNT, count);
        bundle.put(TRACK_USED, trackUsed);
        bundle.put(TRACK_COOLDOWN, trackCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt( COUNT );

        trackUsed = bundle.getBoolean(TRACK_USED);
        trackUsed = bundle.getBoolean(TRACK_COOLDOWN);

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
        GameScene.show(new WndCombo4(this));
    }

    public enum ComboMove {
        TRACK       (0, 0xFF00FF00), // skill-1
        PREDATION   (50, 0xFFFF0000); // skill-2

        public int comboReq, tintColor;

        ComboMove(int comboReq, int tintColor){
            this.comboReq = comboReq;
            this.tintColor = tintColor;
        }

        public String desc(int count){
            switch (this){
                default:
                    return Messages.get(this, name()+"_desc");
                case TRACK:
                    return Messages.get(this, name()+"_desc", 15+3*Dungeon.hero.pointsInTalent(Talent.HUNGER_AND_THIRST), 70-4*Dungeon.hero.pointsInTalent(Talent.HUNGER_AND_THIRST));
            }
        }

    }

    private boolean trackUsed = false;
    private boolean trackCooldown = false;

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
        if (move == ComboMove.TRACK) {
            if (Dungeon.hero.buff(Talent.TrackCoolDown.class) == null) {
                trackUsed = true;
                trackCooldown = true;
                Invisibility.dispel();
                SpellSprite.show(Dungeon.hero, SpellSprite.VISION, 1, 0.77f, 0.9f);
                Buff.affect(Dungeon.hero, Talent.TrackCoolDown.class, 70-4*Dungeon.hero.pointsInTalent(Talent.HUNGER_AND_THIRST));
                Mob affected1 = null;
                for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                    if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                        Buff.affect(mob, LancePredationTracker.class, LancePredationTracker.DURATION+3*Dungeon.hero.pointsInTalent(Talent.HUNGER_AND_THIRST));
                        if (mob.buff(LancePredationTracker.class) != null){
                            affected1 = mob;
                        }
                    }
                }
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
            case PREDATION:
                if (enemy.properties().contains(Char.Property.BOSS)) {
                    dmgBonus = 100;
                } else {
                    dmgBonus = enemy.HP;
                }
                break;
            case TRACK:
            default:
                break;
        }

        if (hero.attack(enemy, dmgMulti, dmgBonus, Char.INFINITE_ACCURACY)){
            //special on-hit effects
            switch (moveBeingUsed) {
                case PREDATION:
                    SpellSprite.show(hero, SpellSprite.PREDATION, 1, 0.77f, 0.9f);
                    Mob affected2 = null;
                    for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                        if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                            Buff.affect( mob, Terror.class, Terror.DURATION ).object = hero.id();
                            if (hero.hasTalent(Talent.GREAT_TERROR)) {
                                Buff.affect( mob, Amok.class, 3*hero.pointsInTalent(Talent.GREAT_TERROR) );
                            }
                            if (mob.buff(Terror.class) != null){
                                affected2 = mob;
                            }
                        }
                    }
                    break;
                case TRACK:
                default:
                    //nothing
                    break;
            }
        }

        Invisibility.dispel();

        //Post-attack behaviour
        switch(moveBeingUsed){
            case PREDATION:
                detach();
                Buff.affect(hero, Bless.class, 15);
                Buff.affect(hero, Stamina.class, 15);
                float healFactor;
                if (hero.hasTalent(Talent.FITTEST_SURVIVAL)) {
                    healFactor = Dungeon.hero.HP * 0.4f;
                } else {
                    healFactor = Dungeon.hero.HP * 0.2f;
                }
                healFactor = Math.min( healFactor, hero.HT - hero.HP );
                Dungeon.hero.HP += (int)healFactor;
                if (hero.hasTalent(Talent.FITTEST_SURVIVAL) && hero.pointsInTalent(Talent.FITTEST_SURVIVAL) >= 2) {
                    PotionOfHealing.cure(hero);
                }
                if (hero.hasTalent(Talent.FITTEST_SURVIVAL) && hero.pointsInTalent(Talent.FITTEST_SURVIVAL) >= 3) {
                    Buff.affect(hero, Barrier.class).setShield((int) (0.4f * hero.HT + 10));
                }
                ActionIndicator.clearAction(LanceCombo3.this);
                hero.spendAndNext(hero.attackDelay());
                break;
            case TRACK:
            default:
                detach();
                ActionIndicator.clearAction(LanceCombo3.this);
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
