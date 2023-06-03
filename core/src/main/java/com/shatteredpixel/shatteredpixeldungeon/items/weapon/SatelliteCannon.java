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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.HuntressArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class SatelliteCannon extends Weapon {

    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_FEED      = "FEED";
    private static final String ITEMS       = "EatItem";

    public ArrayList<Class> seeds = new ArrayList<>();
    public ArrayList<Class> stones = new ArrayList<>();
    public ArrayList<Class> weapons = new ArrayList<>();
    public ArrayList<Class> armors = new ArrayList<>();

    public Class curItemEffect = null;

    {
        image = ItemSpriteSheet.SATELLITE_CANNON;

        defaultAction = AC_SHOOT;
        usesTargeting = true;

        unique = true;
        bones = false;
    }

    public boolean sniperSpecial = false;
    public float sniperSpecialBonusDamage = 0f;

    public int EatItem = 0;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_EQUIP);
        actions.add(AC_SHOOT);
        actions.add(AC_FEED);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {
            curUser = hero;
            curItem = this;
            GameScene.selectCell( shooter );
        } else if (action.equals(AC_FEED)) {
            GameScene.selectItem(itemSelector);
        }
    }

    private static Class[] harmfulPlants = new Class[]{
            Blindweed.class, Firebloom.class, Icecap.class, Sorrowmoss.class,  Stormvine.class
    };

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (attacker.buff(NaturesPower.naturesPowerTracker.class) != null && !sniperSpecial){

            Actor.add(new Actor() {
                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {

                    if (Random.Int(12) < ((Hero)attacker).pointsInTalent(Talent.NATURES_WRATH)){
                        Plant plant = (Plant) Reflection.newInstance(Random.element(harmfulPlants));
                        plant.pos = defender.pos;
                        plant.activate( defender.isAlive() ? defender : null );
                    }

                    if (!defender.isAlive()){
                        NaturesPower.naturesPowerTracker tracker = attacker.buff(NaturesPower.naturesPowerTracker.class);
                        if (tracker != null){
                            tracker.extend(((Hero) attacker).pointsInTalent(Talent.WILD_MOMENTUM));
                        }
                    }

                    Actor.remove(this);
                    return true;
                }
            });

        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    public String info() {
        String info = desc();

        info += "\n\n" + Messages.get( SatelliteCannon.class, "stats",
                Math.round(augment.damageFactor(min())),
                Math.round(augment.damageFactor(max())),
                STRReq());

        if (STRReq() > Dungeon.hero.STR()) {
            info += " " + Messages.get(Weapon.class, "too_heavy");
        } else if (Dungeon.hero.STR() > STRReq()){
            info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
        }

        switch (augment) {
            case SPEED:
                info += "\n\n" + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += "\n\n" + Messages.get(Weapon.class, "stronger");
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

        info += "\n\n" + Messages.get(SatelliteCannon.class, "item", Integer.valueOf(this.EatItem));

        info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

        return info;
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(1, lvl); //tier 1
    }

    @Override
    public int min(int lvl) {
        int dmg = 1 + Dungeon.hero.lvl/5
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                + (curseInfusionBonus ? 1 + Dungeon.hero.lvl/30 : 0)
                + EatItem/6;
        return Math.max(0, dmg);
    }

    @Override
    public int max(int lvl) {
        int dmg = 6 + (int)(Dungeon.hero.lvl/2.5f)
                + 2*RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                + (curseInfusionBonus ? 2 + Dungeon.hero.lvl/15 : 0)
                + EatItem/4;
        return Math.max(0, dmg);
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return knockArrow().targetingPos(user, dst);
    }

    private int targetPos;

    @Override
    public int damageRoll(Char owner) {
        int damage = augment.damageFactor(super.damageRoll(owner));

        if (owner instanceof Hero) {
            int exStr = ((Hero)owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange( 0, exStr );
            }
        }

        if (sniperSpecial){
            damage = Math.round(damage * (1f + sniperSpecialBonusDamage));

            switch (augment){
                case NONE:
                    damage = Math.round(damage * 0.667f);
                    break;
                case SPEED:
                    damage = Math.round(damage * 0.5f);
                    break;
                case DAMAGE:
                    //as distance increases so does damage, capping at 3x:
                    //1.20x|1.35x|1.52x|1.71x|1.92x|2.16x|2.43x|2.74x|3.00x
                    int distance = Dungeon.level.distance(owner.pos, targetPos) - 1;
                    float multiplier = Math.min(3f, 1.2f * (float)Math.pow(1.125f, distance));
                    damage = Math.round(damage * multiplier);
                    break;
            }
        }

        return damage;
    }

    @Override
    protected float baseDelay(Char owner) {
        if (sniperSpecial){
            switch (augment){
                case NONE: default:
                    return 0f;
                case SPEED:
                    return 1f;
                case DAMAGE:
                    return 2f;
            }
        } else{
            return super.baseDelay(owner);
        }
    }

    @Override
    protected float speedMultiplier(Char owner) {
        float speed = super.speedMultiplier(owner);
        if (owner.buff(NaturesPower.naturesPowerTracker.class) != null){
            // +33% speed to +50% speed, depending on talent points
            speed += ((8 + ((Hero)owner).pointsInTalent(Talent.GROWING_POWER)) / 24f);
        }
        return speed;
    }

    @Override
    public int level() {
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
        if (curseInfusionBonus) level += 1 + level/6;
        return level;
    }

    @Override
    public int buffedLvl() {
        //level isn't affected by buffs/debuffs
        return level();
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public SpiritArrow knockArrow(){
        return new SpiritArrow();
    }

    public class SpiritArrow extends MissileWeapon {

        {
            image = ItemSpriteSheet.NOTHING;

            hitSound = Assets.Sounds.ZAP;
            projectingProperty = true;
        }

        @Override
        public Emitter emitter() {
            if (Dungeon.hero.buff(NaturesPower.naturesPowerTracker.class) != null && !sniperSpecial){
                Emitter e = new Emitter();
                e.pos(5, 5);
                e.fillTarget = false;
                e.pour(LeafParticle.GENERAL, 0.01f);
                return e;
            } else {
                return super.emitter();
            }
        }

        @Override
        public int damageRoll(Char owner) {
            return SatelliteCannon.this.damageRoll(owner);
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return SatelliteCannon.this.hasEnchant(type, owner);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            if (Dungeon.hero.pointsInTalent(Talent.ASSAULT_CELL) >= 1 && EatItem >= 30) {
                Buff.prolong(defender, Slow.class, 2f);
            }

            return SatelliteCannon.this.proc(attacker, defender, damage);
        }

        protected float procChanceMultiplier( Char attacker ) {
            float multi = RingOfArcana.enchantPowerMultiplier(attacker);
            if (attacker instanceof Hero && EatItem >= 80 && ((Hero)attacker).pointsInTalent(Talent.ASSAULT_CELL) == 3) {
                multi += 0.5f;
            }

            return multi;
        }

        @Override
        public float delayFactor(Char user) {
            return SatelliteCannon.this.delayFactor(user);
        }

        @Override
        public float accuracyFactor(Char owner, Char target) {
            if (sniperSpecial && SatelliteCannon.this.augment == Augment.DAMAGE){
                return Float.POSITIVE_INFINITY;
            } else {
                return super.accuracyFactor(owner, target);
            }
        }

        @Override
        public int STRReq(int lvl) {
            return SatelliteCannon.this.STRReq(lvl);
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );

            if (enemy == null || enemy == curUser) {
                parent = null;
                Splash.at( cell, 0xCC99FFFF, 1 );
            } else {
                if (!curUser.shoot( enemy, this )) {
                    Splash.at(cell, 0xCC99FFFF, 1);
                }
                if (sniperSpecial && SatelliteCannon.this.augment != Augment.SPEED) sniperSpecial = false;
            }
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.LIGHTNING, 1, Random.Float(0.87f, 1.15f) );
        }

        int flurryCount = -1;
        Actor flurryActor = null;

        @Override
        public void cast(final Hero user, final int dst) {
            final int cell = throwPos( user, dst );
            SatelliteCannon.this.targetPos = cell;
            if (sniperSpecial && SatelliteCannon.this.augment == Augment.SPEED){
                if (flurryCount == -1) flurryCount = 3;

                final Char enemy = Actor.findChar( cell );

                if (enemy == null){
                    user.spendAndNext(castDelay(user, dst));
                    sniperSpecial = false;
                    flurryCount = -1;

                    if (flurryActor != null){
                        flurryActor.next();
                        flurryActor = null;
                    }
                    return;
                }
                QuickSlotButton.target(enemy);

                final boolean last = flurryCount == 1;

                user.busy();

                throwSound();

                ((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
                        reset(user.sprite,
                                cell,
                                this,
                                new Callback() {
                                    @Override
                                    public void call() {
                                        if (enemy.isAlive()) {
                                            curUser = user;
                                            onThrow(cell);
                                        }

                                        if (last) {
                                            user.spendAndNext(castDelay(user, dst));
                                            sniperSpecial = false;
                                            flurryCount = -1;
                                        }

                                        if (flurryActor != null){
                                            flurryActor.next();
                                            flurryActor = null;
                                        }
                                    }
                                });

                user.sprite.zap(cell, new Callback() {
                    @Override
                    public void call() {
                        flurryCount--;
                        if (flurryCount > 0){
                            Actor.add(new Actor() {

                                {
                                    actPriority = VFX_PRIO-1;
                                }

                                @Override
                                protected boolean act() {
                                    flurryActor = this;
                                    int target = QuickSlotButton.autoAim(enemy, SpiritArrow.this);
                                    if (target == -1) target = cell;
                                    cast(user, target);
                                    Actor.remove(this);
                                    return false;
                                }
                            });
                            curUser.next();
                        }
                    }
                });

            } else {
                if (user.hasTalent(Talent.ORBITAL_BOMBARDMENT)
                        && user.buff(Talent.OrbitShotCooldown.class) == null) {
                    int throwPos = throwPos(user, dst);
                    if (Actor.findChar(throwPos) == null) {
                        for (Mob mob : (Mob[]) Dungeon.level.mobs.toArray(new Mob[0])) {
                            if (Dungeon.level.adjacent(mob.pos, throwPos) && mob.alignment != Char.Alignment.ALLY) {
                                mob.damage((int) (((float) damageRoll(user)) * ((((float) user.pointsInTalent(Talent.ORBITAL_BOMBARDMENT))*0.5f) + 0.5f)), this);
                                CellEmitter.center(mob.pos).burst(BlastParticle.FACTORY, 10);
                            }
                        }
                        Buff.affect(user, Talent.OrbitShotCooldown.class, (float)(35-(user.pointsInTalent(Talent.ORBITAL_BOMBARDMENT)*5)));
                    }
                }

                if (user.hasTalent(Talent.SEER_SHOT)
                        && user.buff(Talent.SeerShotCooldown.class) == null){
                    int shotPos = throwPos(user, dst);
                    if (Actor.findChar(shotPos) == null) {
                        RevealedArea a = Buff.affect(user, RevealedArea.class, 5 * user.pointsInTalent(Talent.SEER_SHOT));
                        a.depth = Dungeon.depth;
                        a.pos = shotPos;
                        Buff.affect(user, Talent.SeerShotCooldown.class, 20f);
                    }
                }

                super.cast(user, dst);
            }
        }
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
                if (SatelliteCannon.this.EatItem < 204) {
                    if (item instanceof Plant.Seed || item instanceof Runestone) {
                        EatItem++;
                        selectItem(item);
                    } else if (item instanceof Weapon || item instanceof Armor) {
                        EatItem += 3;
                        selectItem(item);
                    } else if (item instanceof ScrollOfUpgrade) {
                        EatItem += 5;
                        selectItem(item);
                    }
                } else {
                    GLog.w(Messages.get(SatelliteCannon.class, "itemfail", new Object[0]), new Object[0]);
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
            GLog.i( Messages.get(SatelliteCannon.class, "absorb_item") );
        }

        public boolean canUseItem(Item item){
            return (item instanceof Plant.Seed ||
                    item instanceof Runestone ||
                    item instanceof Weapon ||
                    item instanceof Armor ||
                    item instanceof ScrollOfUpgrade)
                    && !(item instanceof MissileWeapon)
                    && !(item instanceof SpiritBow)
                    && !(item instanceof SatelliteCannon)
                    && !(item instanceof HuntressArmor)
                    && !seeds.contains(item.getClass());
        }
    };

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
		public void onSelect( Integer target ) {
			if (target != null) {
				final Ballistica shot = new Ballistica( curUser.pos, target, Ballistica.SUPER_BOLT );
				int cell = shot.collisionPos;

                knockArrow().cast(curUser, target);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));
			}
		}

        @Override
        public String prompt() {
            return Messages.get(SatelliteCannon.class, "prompt");
        }
    };

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEMS, this.EatItem);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.EatItem = bundle.getInt(ITEMS);
    }
}

