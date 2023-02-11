/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class NotMachineGun extends Firearm {

    {
        image = ItemSpriteSheet.NOT_MACHINE_GUN;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 1;

        bones = false;
    }

    private static final String TXT_STATUS = "%d/%d";

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //8 base, down from 10
                lvl*(tier);
    }

    @Override
    public int min(int lvl) {
        return  tier +
                lvl*(tier);
    }

    @Override
    public int Bulletmax(int lvl) {
        return  2*(tier+1) +
                lvl*(tier) +
                RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int Bulletmin(int lvl) {
        return  2*(tier) +
                lvl*(tier) +
                RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {

            if (!isEquipped( hero )) {
                usesTargeting = false;
                GLog.w(Messages.get(this, "not_equipped"));
            }
        }
        if (action.equals(AC_RELOAD)) {
            max_round = 8;
            if (round == max_round){
                GLog.w(Messages.get(this, "already_loaded"));
            } else {
                reload();
            }
        }
    }

    public void reload() {
        max_round = 8;
        curUser.spend(reload_time);
        curUser.busy();
        Sample.INSTANCE.play(Assets.Sounds.UNLOCK, 2, 1.1f);
        curUser.sprite.operate(curUser.pos);
        round = Math.max(max_round, round);

        GLog.i(Messages.get(this, "reloading"));

        updateQuickslot();
    }

    public int getRound() { return this.round; }

    @Override
    public String status() {
        max_round = 8;
        return Messages.format(TXT_STATUS, round, max_round);
    }

    public class Bullet extends MissileWeapon {

        {
            image = ItemSpriteSheet.DUAL_BULLET;

            hitSound = Assets.Sounds.PUFF;
            tier = 2;
        }

        @Override
        public int buffedLvl(){
            return NotMachineGun.this.buffedLvl();
        }

        @Override
        public int damageRoll(Char owner) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            int bulletdamage = Random.NormalIntRange(Bulletmin(NotMachineGun.this.buffedLvl()),
                    Bulletmax(NotMachineGun.this.buffedLvl()));

            if (owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()) {
                bulletdamage = Math.round(bulletdamage * (1f + 0.15f * ((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM)));
            }

            return bulletdamage;
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return NotMachineGun.this.hasEnchant(type, owner);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
            if (NotMachineGun.this.enchantment == null
                    && Random.Int(3) < hero.pointsInTalent(Talent.SHARED_ENCHANTMENT)
                    && hero.buff(MagicImmune.class) == null
                    && bow != null
                    && bow.enchantment != null) {
                return bow.enchantment.proc(this, attacker, defender, damage);
            } else {
                return NotMachineGun.this.proc(attacker, defender, damage);
            }
        }

        @Override
        public float accuracyFactor(Char owner, Char target) {
            float accFactor = super.accuracyFactor(owner, target);
            return accFactor;
        }

        @Override
        public int STRReq(int lvl) {
            return NotMachineGun.this.STRReq();
        }

        @Override
        protected void onThrow( int cell ) {
            for (int i=1; i<=2; i++) {                                                           //i<=n에서 n이 반복하는 횟수, 즉 발사 횟수
                if (round <= 0) {
                    break;
                }
                Char enemy = Actor.findChar(cell);
                if (enemy == null || enemy == curUser) {
                    parent = null;
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                } else {
                    if (!curUser.shoot(enemy, this)) {
                        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    }
                }

                round --;
            }
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                if (mob.paralysed <= 0
                        && Dungeon.level.distance(curUser.pos, mob.pos) <= 4
                        && mob.state != mob.HUNTING) {
                    mob.beckon( curUser.pos );
                }
            }
            updateQuickslot();
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }

        @Override
        public void cast(final Hero user, final int dst) {
            super.cast(user, dst);
        }
    }

}
