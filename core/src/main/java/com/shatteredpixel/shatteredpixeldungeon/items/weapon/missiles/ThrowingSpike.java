/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class ThrowingSpike extends MissileWeapon {

    {
        image = ItemSpriteSheet.THROWING_SPIKE;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.2f;

        bones = false;

        tier = 1;
    }

    boolean circleBackhit = false;

    @Override
    protected float adjacentAccFactor(Char owner, Char target) {
        if (circleBackhit){
            circleBackhit = false;
            return 1.5f;
        }
        return super.adjacentAccFactor(owner, target);
    }

    @Override
    protected void rangedHit(Char enemy, int cell) {
        decrementDurability();
        if (durability > 0){
            Buff.append(Dungeon.hero, ThrowingSpike.CircleBack.class).setup(this, cell, Dungeon.hero.pos, Dungeon.depth);
        }
    }

    @Override
    protected void rangedMiss(int cell) {
        parent = null;
        Buff.append(Dungeon.hero, ThrowingSpike.CircleBack.class).setup(this, cell, Dungeon.hero.pos, Dungeon.depth);
    }

    public static class CircleBack extends Buff {

        {
            revivePersists = true;
        }

        private ThrowingSpike spike;
        private int thrownPos;
        private int returnPos;
        private int returnDepth;

        private int left;

        public void setup( ThrowingSpike spike, int thrownPos, int returnPos, int returnDepth){
            this.spike = spike;
            this.thrownPos = thrownPos;
            this.returnPos = returnPos;
            this.returnDepth = returnDepth;
            left = 3;
        }

        public int returnPos(){
            return returnPos;
        }

        public MissileWeapon cancel(){
            detach();
            return spike;
        }

        public int activeDepth(){
            return returnDepth;
        }

        @Override
        public boolean act() {
            if (returnDepth == Dungeon.depth){
                left--;
                if (left <= 0){
                    final Char returnTarget = Actor.findChar(returnPos);
                    final Char target = this.target;
                    MissileSprite visual = ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class));
                    visual.reset( thrownPos,
                            returnPos,
                            spike,
                            new Callback() {
                                @Override
                                public void call() {
                                    if (returnTarget == target){
                                        if (target instanceof Hero && spike.doPickUp((Hero) target)) {
                                            //grabbing the spike takes no time
                                            ((Hero) target).spend(-TIME_TO_PICK_UP);
                                        } else {
                                            Dungeon.level.drop(spike, returnPos).sprite.drop();
                                        }

                                    } else if (returnTarget != null){
                                        spike.circleBackhit = true;
                                        if (((Hero)target).shoot( returnTarget, spike )) {
                                            spike.decrementDurability();
                                        }
                                        if (spike.durability > 0) {
                                            Dungeon.level.drop(spike, returnPos).sprite.drop();
                                        }

                                    } else {
                                        Dungeon.level.drop(spike, returnPos).sprite.drop();
                                    }
                                    ThrowingSpike.CircleBack.this.next();
                                }
                            });
                    visual.alpha(0f);
                    float duration = Dungeon.level.trueDistance(thrownPos, returnPos) / 20f;
                    target.sprite.parent.add(new AlphaTweener(visual, 1f, duration));
                    detach();
                    return false;
                }
            }
            spend( TICK );
            return true;
        }

        private static final String SPIKE = "spike";
        private static final String THROWN_POS = "thrown_pos";
        private static final String RETURN_POS = "return_pos";
        private static final String RETURN_DEPTH = "return_depth";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(SPIKE, spike);
            bundle.put(THROWN_POS, thrownPos);
            bundle.put(RETURN_POS, returnPos);
            bundle.put(RETURN_DEPTH, returnDepth);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            spike = (ThrowingSpike) bundle.get(SPIKE);
            thrownPos = bundle.getInt(THROWN_POS);
            returnPos = bundle.getInt(RETURN_POS);
            returnDepth = bundle.getInt(RETURN_DEPTH);
        }
    }

}
