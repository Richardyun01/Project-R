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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FirearmWeapon extends MeleeWeapon {

    public enum FirearmType {
        FirearmPistol,
        FirearmPrecision,
        FirearmAuto,
        FirearmShotgun,
        FirearmExplosive,
        FirearmEnergy1,
        FirearmEnergy2,
        FirearmEtc;
    }

    public FirearmType type;

    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_RELOAD    = "RELOAD";

    public int max_round;
    public int round = 0;
    public int shot = 1;
    public float reload_time;
    public int bullet_image = ItemSpriteSheet.SINGLE_BULLET;
    public String bullet_sound = Assets.Sounds.PUFF;

    private static final String TXT_STATUS = "%d/%d";
    private static final String ROUND = "round";
    private static final String MAX_ROUND = "max_round";
    private static final String RELOAD_TIME = "reload_time";

    public int tier;

    public void setReloadTime() {
        reload_time = 2f * RingOfReload.reloadMultiplier(Dungeon.hero);
    }

    @Override
    public int min(int lvl) {
        return  tier +  //base
                lvl;    //level scaling
    }

    @Override
    public int max(int lvl) {
        return  3*(tier+1) +    //base
                lvl*(tier+1);   //level scaling
    }

    public int Bulletmin(int lvl) {
        switch (type) {
            case FirearmPrecision:
                return 3 * tier + lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            case FirearmShotgun:
                return tier + lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            case FirearmExplosive:
                return (tier+5) + lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            case FirearmEnergy1:
            case FirearmEnergy2:
                return tier + lvl;
            case FirearmPistol:
            case FirearmAuto:
            case FirearmEtc:
            default:
                return tier + lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    public int Bulletmax(int lvl) {
        switch (type) {
            case FirearmPrecision:
                return 6 * (tier+3) + lvl * (tier+3) + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            case FirearmAuto:
                return 2 * (tier) + lvl * (tier) + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            case FirearmShotgun:
                return (tier*2) + lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            case FirearmExplosive:
                return 8 * (tier+6) + lvl * (tier+6) + RingOfSharpshooting.levelDamageBonus(hero);
            case FirearmEnergy1:
            case FirearmEnergy2:
                return Math.round((3 * (tier + 1) + lvl * 3 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)));
            case FirearmEtc:
                return 5 * (tier + 1) + lvl * 3 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
            case FirearmPistol:
            default:
                return 5 * (tier) + lvl * (tier) + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    public int STRReq(int lvl){
        return STRReq(tier, lvl);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_ROUND, max_round);
        bundle.put(ROUND, round);
        bundle.put(RELOAD_TIME, reload_time);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        max_round = bundle.getInt(MAX_ROUND);
        round = bundle.getInt(ROUND);
        reload_time = bundle.getFloat(RELOAD_TIME);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            actions.add(AC_SHOOT);
            actions.add(AC_RELOAD);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {

            if (!isEquipped( hero )) {
                usesTargeting = false;
                GLog.w(Messages.get(this, "not_equipped"));
            } else {
                setReloadTime();
                if (round <= 0) {
                    reload();
                } else {
                    usesTargeting = true;
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                }
            }
        }
        if (action.equals(AC_RELOAD)) {
            if (false) { //if (Dungeon.hero.hasTalent(Talent.LARGER_MAGAZINE)) {
                max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.LARGER_MAGAZINE);
            }
            if (false) { //if (Dungeon.hero.hasTalent(Talent.DRUM_MAGAZINE)) {
                max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.DRUM_MAGAZINE);
            }
            if (round == max_round) {
                GLog.w(Messages.get(this, "already_loaded"));
            } else {
                reload();
            }
        }
    }

    public void reload() {
        if (false) { //if (Dungeon.hero.hasTalent(Talent.LARGER_MAGAZINE)) {
            max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.LARGER_MAGAZINE);
        }
        if (false) { //if (Dungeon.hero.hasTalent(Talent.DRUM_MAGAZINE)) {
            max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.DRUM_MAGAZINE);
        }
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
        if (false) { //if (Dungeon.hero.hasTalent(Talent.LARGER_MAGAZINE)) {
            max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.LARGER_MAGAZINE);
        }
        if (false) { //if (Dungeon.hero.hasTalent(Talent.DRUM_MAGAZINE)) {
            max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.DRUM_MAGAZINE);
        }

        return Messages.format(TXT_STATUS, round, max_round);
    }

    @Override
    public int damageRoll(Char owner) {
        int damage = augment.damageFactor(super.damageRoll(owner));

        if (owner instanceof Hero) {
            int exStr = ((Hero)owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange( 0, exStr );
            }
        }

        return damage;
    }                           //초과 힘에 따른 추가 데미지

    @Override
    public String info() {

        if (false) { //if (Dungeon.hero.hasTalent(Talent.LARGER_MAGAZINE)) {
            max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.LARGER_MAGAZINE);
        }
        if (false) { //if (Dungeon.hero.hasTalent(Talent.DRUM_MAGAZINE)) {
            max_round += 2f; // * Dungeon.hero.pointsInTalent(Talent.DRUM_MAGAZINE);
        }
        setReloadTime();
        String info = desc();

        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.STR() > STRReq()){
                info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            }
            info += "\n\n" + Messages.get(FirearmWeapon.class, "stats_known",
                    Bulletmin(FirearmWeapon.this.buffedLvl()),
                    Bulletmax(FirearmWeapon.this.buffedLvl()),
                    round, max_round,
                    new DecimalFormat("#.##").format(reload_time));
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
            info += "\n\n" + Messages.get(FirearmWeapon.class, "stats_unknown",
                    Bulletmin(0),
                    Bulletmax(0),
                    round, max_round, new DecimalFormat("#.##").format(reload_time));
        }

        String statsInfo = statsInfo();
        if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

        switch (augment) {
            case SPEED:
                info += " " + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += " " + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        return info;
    }

    public String statsInfo(){
        return Messages.get(this, "stats_desc");
    }

    @Override
    protected float baseDelay(Char owner) {
        float delay = augment.delayFactor(this.DLY);
        if (owner instanceof Hero) {
            int encumbrance = STRReq() - ((Hero)owner).STR();
            if (encumbrance > 0){
                delay *= Math.pow( 1.2, encumbrance );
            }
        }
        return delay;
    }

    public FirearmWeapon.Bullet knockBullet(){
        return new FirearmWeapon.Bullet();
    }

    public float accuracyFactorBullet(Char owner, Char target) {
        return 1f;
    }

    public void onThrowBulletFirearmExplosive( int cell ) {
        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
        ArrayList<Char> affected = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS9) {
            int c = cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 4);
                }
                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                }
                Char ch = Actor.findChar(c);
                if (ch != null) {
                    affected.add(ch);
                }
            }
        }
        Sample.INSTANCE.play( Assets.Sounds.BLAST );
    }

    /*
    public void onThrowBulletFirearmEnergy2( int cell ) {
        Ballistica aim = new Ballistica(hero.pos, cell, Ballistica.PROJECTILE); //Always Projecting and no distance limit, see MissileWeapon.throwPos
        ArrayList<Char> chars = new ArrayList<>();
        int maxDist = maxDistance+maxDistanceBonus;
        int dist = Math.min(aim.dist, maxDist);
        int cells = aim.path.get(Math.min(aim.dist, dist));
        boolean terrainAffected = false;
        for (int c : aim.subPath(1, maxDist)) {

            Char ch;
            if ((ch = Actor.findChar( c )) != null) {
                chars.add( ch );
            }

            if (Dungeon.level.flamable[c]) {

                Dungeon.level.destroy( c );
                GameScene.updateMap( c );
                terrainAffected = true;

            }

            CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
        }
        if (terrainAffected) {
            Dungeon.observe();
        }
        curUser.sprite.parent.add(new Beam().DeathRay(curUser.sprite.center(), Dungeon.raisedTileCenterToWorld( cells )));
        for (Char ch : chars) {
            int damage = damageRoll(hero);
            ch.damage(damage, hero);
            ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
            ch.sprite.flash();
        }
        round--;
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.paralysed <= 0
                    && Dungeon.level.distance(curUser.pos, mob.pos) <= 4
                    && mob.state != mob.HUNTING) {
                mob.beckon( curUser.pos );
            }
        }
        Invisibility.dispel();
        updateQuickslot();
    }
    */

    public class Bullet extends MissileWeapon {

        {
            image = bullet_image;
            hitSound = bullet_sound;
        }

        @Override
        public int buffedLvl() {
            return FirearmWeapon.this.buffedLvl();
        }

        @Override
        public int damageRoll(Char owner) {
            //Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            int bulletdamage = Random.NormalIntRange(Bulletmin(FirearmWeapon.this.buffedLvl()),
                    Bulletmax(FirearmWeapon.this.buffedLvl()));

            if (owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()) {
                bulletdamage = Math.round(bulletdamage * (1f + 0.15f * ((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM)));
            }

            switch (type) {
                case FirearmEnergy1:
                case FirearmEnergy2:
                    if (hero.buff(Recharging.class) != null) {
                        bulletdamage *= 1.2f;
                    }
                    if (hero.buff(ArtifactRecharge.class) != null) {
                        bulletdamage *= 1.2f;
                    }
                    if (enemy instanceof Eye) {
                        bulletdamage *= 0.75f;
                    }
                    break;
                case FirearmPistol:
                case FirearmPrecision:
                case FirearmAuto:
                case FirearmShotgun:
                case FirearmExplosive:
                case FirearmEtc:
                default:
                    break;
            }

            return bulletdamage;
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return FirearmWeapon.this.hasEnchant(type, owner);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
            if (FirearmWeapon.this.enchantment == null
                    && Random.Int(3) < hero.pointsInTalent(Talent.SHARED_ENCHANTMENT)
                    && hero.buff(MagicImmune.class) == null
                    && bow != null
                    && bow.enchantment != null) {
                return bow.enchantment.proc(this, attacker, defender, damage);
            } else {
                return FirearmWeapon.this.proc(attacker, defender, damage);
            }
        }

        @Override
        public float delayFactor(Char user) {
            return FirearmWeapon.this.delayFactor(user);
        }

        @Override
        public float accuracyFactor(Char owner, Char target) {
            return accuracyFactorBullet(owner, target);
        }

        @Override
        public int STRReq(int lvl) {
            return FirearmWeapon.this.STRReq();
        }

        @Override
        protected void onThrow( int cell ) {
            switch (type) {
                case FirearmExplosive:
                    ArrayList<Char> targets = new ArrayList<>();
                    if (Actor.findChar(cell) != null) targets.add(Actor.findChar(cell));
                    for (int i : PathFinder.NEIGHBOURS8){
                        if (Actor.findChar(cell + i) != null) targets.add(Actor.findChar(cell + i));
                    }
                    for (Char target : targets){
                        curUser.shoot(target, this);
                        if (target == hero && !target.isAlive()){
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(FirearmWeapon.class, "ondeath"));
                        }
                    }
                    onThrowBulletFirearmExplosive(cell);
                    round--;
                    break;
                case FirearmPistol:
                case FirearmPrecision:
                case FirearmAuto:
                case FirearmShotgun:
                case FirearmEtc:
                default:
                    for (int i = 0; i < shot; i++) {
                        if (round <= 0) break;
                        round--;

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
                    }
                    break;
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
            switch (type) {
                case FirearmExplosive:
                    Sample.INSTANCE.play( Assets.Sounds.PUFF, 1, Random.Float(0.33f, 0.66f) );
                    break;
                case FirearmEnergy1:
                case FirearmEnergy2:
                    Sample.INSTANCE.play( Assets.Sounds.ZAP, 1, Random.Float(0.33f, 0.66f) );
                    break;
                case FirearmPistol:
                case FirearmPrecision:
                case FirearmAuto:
                case FirearmShotgun:
                case FirearmEtc:
                default:
                    Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
                    break;
            }
        }

        @Override
        public void cast(final Hero user, final int dst) {
            super.cast(user, dst);
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    reload();
                } else {
                    knockBullet().cast(curUser, target);
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    @Override
    public int value() {
        int price = 20 * tier;
        if (hasGoodEnchant()) {
            price *= 1.5;
        }
        if (cursedKnown && (cursed || hasCurseEnchant())) {
            price /= 2;
        }
        if (levelKnown && level() > 0) {
            price *= (level() + 1);
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

}
