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

package com.shatteredpixel.shatteredpixeldungeon.actors;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AfterImageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BountyTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BulletHell;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bunker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CenobiteEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CommandBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DragonBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fury;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LanceBleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LanceBleedingBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LancePredationTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MurakumoCharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NoEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StarburstBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StimpackAdrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WinterStorm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.carroll.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.magnus.Thruster;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SatelliteCannon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Aria;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.ArmRifle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Fencer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FrostGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Gungnir;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Kaleidoscope;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Karasawa;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Madness;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.NotMachineGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Revolver;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.ShortCarbine;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Spark;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Standard;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Supernova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.ThinLine;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Trench;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Vega;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Murakumo;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RipperWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SurrationSaw;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarpBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class Char extends Actor {
	
	public int pos = 0;
	
	public CharSprite sprite;
	
	public int HT;
	public int HP;
	
	protected float baseSpeed	= 1;
	protected PathFinder.Path path;

	public int paralysed	    = 0;
	public boolean rooted		= false;
	public boolean flying		= false;
	public int invisible		= 0;
	
	//these are relative to the hero
	public enum Alignment{
		ENEMY,
		NEUTRAL,
		ALLY,
		ALLATTACK
	}
	public Alignment alignment;
	
	public int viewDistance	= 8;
	
	public boolean[] fieldOfView = null;
	
	private LinkedHashSet<Buff> buffs = new LinkedHashSet<>();
	
	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );

		//throw any items that are on top of an immovable char
		if (properties().contains(Property.IMMOVABLE)){
			throwItems();
		}
		return false;
	}

	protected void throwItems(){
		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null && heap.type == Heap.Type.HEAP) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable[n] && !Dungeon.level.avoid[n]);
			Item item = heap.peek();
			if (!(item instanceof Tengu.BombAbility.BombItem || item instanceof Tengu.ShockerAbility.ShockerItem)){
				Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
			}
		}
	}

	public String name(){
		return Messages.get(this, "name");
	}

	public boolean canInteract(Char c){
		if (Dungeon.level.adjacent( pos, c.pos )){
			return true;
		} else if (c instanceof Hero
				&& alignment == Alignment.ALLY
				&& Dungeon.level.distance(pos, c.pos) <= 2*Dungeon.hero.pointsInTalent(Talent.ALLY_WARP)){
			return true;
		} else {
			return false;
		}
	}
	
	//swaps places by default
	public boolean interact(Char c){

		//don't allow char to swap onto hazard unless they're flying
		//you can swap onto a hazard though, as you're not the one instigating the swap
		if (!Dungeon.level.passable[pos] && !c.flying){
			return true;
		}

		//can't swap into a space without room
		if (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[c.pos]
			|| c.properties().contains(Property.LARGE) && !Dungeon.level.openSpace[pos]){
			return true;
		}

		//we do a little raw position shuffling here so that the characters are never
		// on the same cell when logic such as occupyCell() is triggered
		int oldPos = pos;
		int newPos = c.pos;

		//warp instantly with allies in this case
		if (c == Dungeon.hero && Dungeon.hero.hasTalent(Talent.ALLY_WARP)){
			PathFinder.buildDistanceMap(c.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
			if (PathFinder.distance[pos] == Integer.MAX_VALUE){
				return true;
			}
			pos = newPos;
			c.pos = oldPos;
			ScrollOfTeleportation.appear(this, newPos);
			ScrollOfTeleportation.appear(c, oldPos);
			Dungeon.observe();
			GameScene.updateFog();
			return true;
		}

		//can't swap places if one char has restricted movement
		if (rooted || c.rooted || buff(Vertigo.class) != null || c.buff(Vertigo.class) != null){
			return true;
		}

		c.pos = oldPos;
		moveSprite( oldPos, newPos );
		move( newPos );

		c.pos = newPos;
		c.sprite.move( newPos, oldPos );
		c.move( oldPos );
		
		c.spend( 1 / c.speed() );

		if (c == Dungeon.hero){
			if (Dungeon.hero.subClass == HeroSubClass.FREERUNNER){
				Buff.affect(Dungeon.hero, Momentum.class).gainStack();
			}

			Dungeon.hero.busy();
		}

		if (c == Dungeon.hero){
			if (hero.subClass == HeroSubClass.DRAGON && hero.buff(DragonBuff.class) == null) {
				Buff.affect(hero, DragonBuff.class).indicate();
			}

			Dungeon.hero.busy();
		}
		
		return true;
	}
	
	protected boolean moveSprite( int from, int to ) {
		
		if (sprite.isVisible() && (Dungeon.level.heroFOV[from] || Dungeon.level.heroFOV[to])) {
			sprite.move( from, to );
			return true;
		} else {
			sprite.turnTo(from, to);
			sprite.place( to );
			return true;
		}
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(Assets.Sounds.HIT, 1, pitch);
	}

	public boolean blockSound( float pitch ) {
		return false;
	}
	
	protected static final String POS       = "pos";
	protected static final String TAG_HP    = "HP";
	protected static final String TAG_HT    = "HT";
	protected static final String TAG_SHLD  = "SHLD";
	protected static final String BUFFS	    = "buffs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );
		
		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );
		
		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachTo( this );
			}
		}
	}

	final public boolean attack( Char enemy ){
		return attack(enemy, 1f, 0f, 1f);
	}
	
	public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {

		if (enemy == null) return false;
		
		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

		if (enemy == Dungeon.hero && Dungeon.hero.damageInterrupt){
			Dungeon.hero.interrupt();
		}

		if (enemy.isInvulnerable(getClass())) {

			if (visibleFight) {
				enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "invulnerable") );

				Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1f, Random.Float(0.96f, 1.05f));
			}

			return false;

		} else if (hit( this, enemy, accMulti, false )) {
			
			int dr = Math.round(enemy.drRoll() * AscensionChallenge.statModifier(enemy));

			Barkskin bark = enemy.buff(Barkskin.class);
			if (bark != null)   dr += Random.NormalIntRange( 0 , bark.level() );
			
			if (this instanceof Hero){
				Hero h = (Hero)this;
				if (h.belongings.weapon() instanceof MissileWeapon
						&& h.subClass == HeroSubClass.SNIPER
						&& !Dungeon.level.adjacent(h.pos, enemy.pos)){
					dr = 0;
				}
				if (h.belongings.weapon instanceof RipperWeapon ||
					h.belongings.weapon instanceof WarpBlade) {
					dr = 0;
				}
				if (h.belongings.weapon instanceof Vega.Bullet   ||
					h.belongings.weapon instanceof Karasawa.Bullet ||
					h.belongings.weapon instanceof Kaleidoscope.Bullet ||
					h.belongings.weapon instanceof Spark.Bullet ||
					h.belongings.weapon instanceof Supernova.Bullet) {
					dr = 0;
				}
				if (h.belongings.weapon instanceof Fencer.Bullet) {
					dr *= 0.5f;
				}
				SatelliteCannon satelliteCannon = (SatelliteCannon) hero.belongings.getItem(SatelliteCannon.class);
				if (satelliteCannon != null &&
					(hero.belongings.weapon instanceof SatelliteCannon.SpiritArrow) &&
					Dungeon.hero.pointsInTalent(Talent.ASSAULT_CELL) >= 2 &&
					satelliteCannon.EatItem >= 50) {
					dr = 0;
				}
				if (hero.hasTalent(Talent.PRECISE_STRIKE) && Random.Int(10) < hero.pointsInTalent(Talent.PRECISE_STRIKE)) {
					dr = 0;
				}
				if (hero.hasTalent(Talent.GRIND_DEFENCE) && hero.buff(StarburstBuff.class) != null) {
					if (Random.Int(4) < hero.pointsInTalent(Talent.GRIND_DEFENCE)) {
						dr = 0;
					}
				}
				if (hero instanceof Hero &&
						hero.buff(Revolver.APShot.class) != null &&
						hero.buff(MeleeWeapon.Charger.class) != null &&
						hero.buff(Revolver.APShot.class).onUse &&
						hero.buff(MeleeWeapon.Charger.class).charges >= 1) {
					dr = 0;
				}

				if (((Hero) this).subClass == HeroSubClass.BOUNTYHUNTER && enemy.buff(BountyTracker.Bounty.class) != null) {
					dmgMulti *= 1.15f;
				}

				if (h.buff(CenobiteEnergy.CenobiteAbility.UnarmedAbilityTracker.class) != null){
					dr = 0;
				} else if (h.subClass == HeroSubClass.CENOBITE) {
					//3 turns with standard attack delay
					Buff.prolong(h, CenobiteEnergy.CenobiteAbility.JustHitTracker.class, 4f);
				}
			}

			if (this instanceof Hero
					&& hero.hasTalent(Talent.SELF_PROPHECY)
					&& hero.buff(Talent.ProphecyCoolDown.class) == null
					&& (hero.belongings.weapon() instanceof MagesStaff)) {
				Buff.affect(hero, Talent.ProphecyCoolDown.class, 10f);
			}

			if (this instanceof Hero && hero.hasTalent(Talent.SHOCK_AND_AWE)) {
				if (hero.belongings.weapon() instanceof FirearmWeapon.Bullet) {
					Buff.affect(hero, BulletHell.class).hit();
				}
			}

			if (Dungeon.hero.subClass == HeroSubClass.BUNKER){
				Buff.affect(Dungeon.hero, Bunker.class).gainStack();
			}

			if (this instanceof Hero && hero.hasTalent(Talent.TRAMPLE) && hero.buff(Talent.TrampleCoolDown.class) == null) {
				Buff.affect(enemy, Paralysis.class, 1+hero.pointsInTalent(Talent.TRAMPLE));
				Buff.affect(hero, Talent.TrampleCoolDown.class, 40-10*hero.pointsInTalent(Talent.TRAMPLE));
			}

			if (this.alignment == Alignment.ALLY && !(this instanceof Hero) && hero.hasTalent(Talent.CHARISMA)) {
				dr *= Math.pow(1.1f, hero.pointsInTalent(Talent.CHARISMA));
			}

			if (this instanceof Hero && hero.hasTalent(Talent.SOUL_DOMINATION) && Random.Int(10) < hero.pointsInTalent(Talent.SOUL_DOMINATION)){
				Buff.affect(enemy, Charm.class, Charm.DURATION).object = hero.id();
			}

			if (this instanceof Hero && hero.hasTalent(Talent.RADIUS_BLAST) && hero.buff(Talent.RadiusBlastCooldown.class) == null) {
				for (Mob mob : (Mob[]) Dungeon.level.mobs.toArray(new Mob[0])) {
					if (Dungeon.level.adjacent(mob.pos, enemy.pos) && mob.alignment != Char.Alignment.ALLY) {
						mob.damage(Dungeon.hero.damageRoll() - Math.max(enemy.drRoll(), enemy.drRoll()), this);
					}
				}
				Buff.affect(hero, Talent.RadiusBlastCooldown.class, 110-10*hero.pointsInTalent(Talent.RADIUS_BLAST));
			}

			Dungeon.hero.busy();

			//we use a float here briefly so that we don't have to constantly round while
			// potentially applying various multiplier effects
			float dmg;
			Preparation prep = buff(Preparation.class);
			if (prep != null){
				dmg = prep.damageRoll(this);
				if (this == Dungeon.hero && Dungeon.hero.hasTalent(Talent.BOUNTY_HUNTER)) {
					Buff.affect(Dungeon.hero, Talent.BountyHunterTracker.class, 0.0f);
				}
			} else {
				dmg = damageRoll();
			}

			dmg = Math.round(dmg*dmgMulti);

			Berserk berserk = buff(Berserk.class);
			if (berserk != null) dmg = berserk.damageFactor(dmg);

			if (buff( Fury.class ) != null) {
				dmg *= 1.5f;
			}

			if (this instanceof Hero && hero.belongings.weapon != null && hero.belongings.weapon() instanceof MissileWeapon) {
				if (this instanceof Hero) {
					if (Dungeon.hero.belongings.weapon() instanceof Aria.Bullet) {
						//int distance = Dungeon.level.distance(hero.pos, enemy.pos) - 1;
						float multiplier = Math.min(4f, (float)Math.pow(1.2f, dr + 1));
						dmg = Math.round(dmg * multiplier);
					}
					if (Dungeon.hero.hasTalent(Talent.SHAPED_WARHEAD)) {
						Hero h = (Hero)this;
						if (h.belongings.weapon() instanceof Vega.Bullet ||
							h.belongings.weapon() instanceof Karasawa.Bullet ||
							h.belongings.weapon() instanceof Supernova.Bullet ||
							h.belongings.weapon() instanceof FrostGun.Bullet ||
							h.belongings.weapon() instanceof Madness.Bullet) {
							float multiplier = Math.min((float)(1f + 0.2*hero.pointsInTalent(Talent.SHAPED_WARHEAD)), (float)Math.pow(1f, dr + 1));
							dmg = Math.round(dmg * multiplier);
						}
					}
				}
			}

			if (this instanceof Hero && hero.buff(MurakumoCharge.class) != null && hero.belongings.weapon() instanceof Murakumo) {
				dmg *= (1f + hero.buff(MurakumoCharge.class).getDamageFactor());
				Buff.detach(this, MurakumoCharge.class);
			}

			if (this instanceof Hero && buff(BulletHell.class) != null) {
				Hero h = (Hero)this;
				if (h.belongings.weapon() instanceof NotMachineGun.Bullet ||
					h.belongings.weapon() instanceof ShortCarbine.Bullet ||
					h.belongings.weapon() instanceof ArmRifle.Bullet ||
					h.belongings.weapon() instanceof Standard.Bullet ||
					h.belongings.weapon() instanceof ThinLine.Bullet ||
					h.belongings.weapon() instanceof Trench.Bullet) {
					if (Random.Int(100) < buff(BulletHell.class).getCount()/2) {
						Buff.affect(enemy, Paralysis.class, 1f);
					}
				} else if (h.belongings.weapon() instanceof Kaleidoscope.Bullet ||
						   h.belongings.weapon() instanceof Spark.Bullet) {
					dmg *= (1 + 0.01f * buff(BulletHell.class).getCount());
				}
			}

			if (this instanceof Hero &&
				buff(LanceBleedingBullet.class) != null &&
				hero.belongings.weapon() instanceof FirearmWeapon.Bullet) {
				Buff.affect(enemy, LanceBleeding.class).set(hero.belongings.weapon.max()/(4- hero.pointsInTalent(Talent.BAD_BLOOD)));
			}

			if (this instanceof Hero && buff(StarburstBuff.class) != null && hero.hasTalent(Talent.WARPED_BLADE)) {
				new Fadeleaf().activate(enemy);
				if (Random.Int(4) < hero.pointsInTalent(Talent.WARPED_BLADE)) {
					Buff.affect(enemy, MagicalSleep.class);
				}
			}

			if (hero.buff(Gungnir.TwilightStance.class) != null) {
				dmg *= 2;
			}

			if (enemy.buff(CenobiteEnergy.CenobiteAbility.Meditate.MeditateResistance.class) != null){
				dmg *= 0.2f;
			}

			if (hero.buff(WinterStorm.class) != null && !(hero.belongings.weapon.bullet)) {
				dmg *= 1.5;
				if (hero.hasTalent(Talent.IMPULSE_STRIKE) && hero.pointsInTalent(Talent.IMPULSE_STRIKE) >= 3) {
					if (Random.Int(20) == 0) {
						dmg = enemy.HT;
					}
				}
			}

			if (this instanceof Hero && hero.hasTalent(Talent.INERITA) &&
				level.adjacent(enemy.pos, hero. pos) &&
				hero.belongings.armor != null &&
				hero.belongings.attackingWeapon() instanceof MeleeWeapon) {
				dmg += hero.belongings.armor.DRMax()*(0.005f+0.005f*hero.pointsInTalent(Talent.INERITA));
			}

			if (this instanceof Hero && hero.hasTalent(Talent.BREACHING_SEQUENCE) &&
				(Dungeon.level.map[enemy.pos] == Terrain.DOOR || Dungeon.level.map[enemy.pos] == Terrain.OPEN_DOOR)) {
				dmg *= (1 + 0.2*hero.pointsInTalent(Talent.BREACHING_SEQUENCE));
			}

			for (ChampionEnemy buff : buffs(ChampionEnemy.class)){
				dmg *= buff.meleeDamageFactor();
			}

			dmg *= AscensionChallenge.statModifier(this);

			//flat damage bonus is applied after positive multipliers, but before negative ones
			dmg += dmgBonus;

			//friendly endure
			Endure.EndureTracker endure = buff(Endure.EndureTracker.class);
			if (endure != null) dmg = endure.damageFactor(dmg);

			//enemy endure
			endure = enemy.buff(Endure.EndureTracker.class);
			if (endure != null){
				dmg = endure.adjustDamageTaken(dmg);
			}

			if (enemy.buff(ScrollOfChallenge.ChallengeArena.class) != null){
				dmg *= 0.67f;
			}

			if ( buff(Weakness.class) != null ){
				dmg *= 0.67f;
			}

			if (this != hero && alignment == Alignment.ALLY) {
				if (hero.hasTalent(Talent.COMMAND_SYSTEM)) {
					if (Dungeon.level.distance(hero.pos, this.pos) <= 1+2*hero.pointsInTalent(Talent.COMMAND_SYSTEM)) {
						dmg = (int)(dmg * 1.1f);
					}
				}
			}

			if (this.alignment == Alignment.ALLY && !(this instanceof Hero) && hero.hasTalent(Talent.CHARISMA)) {
				dmg *= Math.pow(1.07f, hero.pointsInTalent(Talent.CHARISMA));
			}

			if (this instanceof Hero && hero.hasTalent(Talent.ARMED_ARMOR)) {
				dmg *= (1 + 0.12f*hero.pointsInTalent(Talent.ARMED_ARMOR));
			}
			
			int effectiveDamage = enemy.defenseProc( this, Math.round(dmg) );
			effectiveDamage = Math.max( effectiveDamage - dr, 0 );

			if (enemy.buff(Viscosity.ViscosityTracker.class) != null){
				effectiveDamage = enemy.buff(Viscosity.ViscosityTracker.class).deferDamage(effectiveDamage);
				enemy.buff(Viscosity.ViscosityTracker.class).detach();
			}

			//vulnerable specifically applies after armor reductions
			if ( enemy.buff( Vulnerable.class ) != null){
				effectiveDamage *= 1.33f;
			}
			
			effectiveDamage = attackProc( enemy, effectiveDamage );
			
			if (visibleFight) {
				if (effectiveDamage > 0 || !enemy.blockSound(Random.Float(0.96f, 1.05f))) {
					hitSound(Random.Float(0.87f, 1.15f));
				}
			}

			// If the enemy is already dead, interrupt the attack.
			// This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.
			if (!enemy.isAlive()){
				return true;
			}

			enemy.damage( effectiveDamage, this );

			if (buff(FireImbue.class) != null)  buff(FireImbue.class).proc(enemy);
			if (buff(FrostImbue.class) != null) buff(FrostImbue.class).proc(enemy);

			if (enemy.isAlive() && enemy.alignment != alignment && prep != null && prep.canKO(enemy)){
				enemy.HP = 0;
				if (!enemy.isAlive()) {
					enemy.die(this);
				} else {
					//helps with triggering any on-damage effects that need to activate
					enemy.damage(-1, this);
					DeathMark.processFearTheReaper(enemy);
				}
				enemy.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Preparation.class, "assassinated"));
			}

			Talent.CombinedLethalityTriggerTracker combinedLethality = buff(Talent.CombinedLethalityTriggerTracker.class);
			if (combinedLethality != null){
				if ( enemy.isAlive() && enemy.alignment != alignment && !Char.hasProp(enemy, Property.BOSS)
						&& !Char.hasProp(enemy, Property.MINIBOSS) && this instanceof Hero &&
						(enemy.HP/(float)enemy.HT) <= 0.4f*((Hero)this).pointsInTalent(Talent.COMBINED_LETHALITY)/3f) {
					enemy.HP = 0;
					if (!enemy.isAlive()) {
						enemy.die(this);
					} else {
						//helps with triggering any on-damage effects that need to activate
						enemy.damage(-1, this);
						DeathMark.processFearTheReaper(enemy);
					}
					enemy.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Talent.CombinedLethalityTriggerTracker.class, "executed"));
				}
				combinedLethality.detach();
			}

			enemy.sprite.bloodBurstA( sprite.center(), effectiveDamage );
			enemy.sprite.flash();

			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {
					
					if (this == Dungeon.hero) {
						return true;
					}

					if (this instanceof WandOfLivingEarth.EarthGuardian
							|| this instanceof MirrorImage || this instanceof PrismaticImage){
						Badges.validateDeathFromFriendlyMagic();
					}
					Dungeon.fail( this );
					GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
					
				} else if (this == Dungeon.hero) {
					GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())) );
				}
			}
			
			return true;
			
		} else {

			enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
			if (visibleFight) {
				//TODO enemy.defenseSound? currently miss plays for monks/crab even when they parry
				Sample.INSTANCE.play(Assets.Sounds.MISS);
			}
			
			return false;
			
		}

	}

	public static int INFINITE_ACCURACY = 1_000_000;
	public static int INFINITE_EVASION = 1_000_000;

	final public static boolean hit( Char attacker, Char defender, boolean magic ) {
		return hit(attacker, defender, magic ? 2f : 1f, magic);
	}

	public static boolean hit( Char attacker, Char defender, float accMulti, boolean magic ) {
		float acuStat = attacker.attackSkill( defender );
		float defStat = defender.defenseSkill( attacker );

		if (defender instanceof Hero && ((Hero) defender).damageInterrupt){
			((Hero) defender).interrupt();
		}

		//invisible chars always hit (for the hero this is surprise attacking)
		if (attacker.invisible > 0 && attacker.canSurpriseAttack()){
			acuStat = INFINITE_ACCURACY;
		}
		if (defender.buff(CenobiteEnergy.CenobiteAbility.Focus.FocusBuff.class) != null && !magic){
			defStat = INFINITE_EVASION;
			defender.buff(CenobiteEnergy.CenobiteAbility.Focus.FocusBuff.class).detach();
			Buff.affect(defender, CenobiteEnergy.CenobiteAbility.Focus.FocusActivation.class, 0);
		}

		//if accuracy or evasion are large enough, treat them as infinite.
		//note that infinite evasion beats infinite accuracy
		if (defStat >= INFINITE_EVASION){
			return false;
		} else if (acuStat >= INFINITE_ACCURACY){
			return true;
		}

		float acuRoll = Random.Float( acuStat );
		if (attacker.buff(Bless.class) != null) acuRoll *= 1.25f;
		if (attacker.buff(  Hex.class) != null) acuRoll *= 0.8f;
		if (attacker.buff( Daze.class) != null) acuRoll *= 0.5f;
		if (attacker.buff(Talent.TrackCoolDown.class) != null &&
			defender.buff(LancePredationTracker.class) != null) acuRoll *= INFINITE_ACCURACY;
		for (ChampionEnemy buff : attacker.buffs(ChampionEnemy.class)){
			acuRoll *= buff.evasionAndAccuracyFactor();
		}
		if (attacker != hero && attacker.alignment == Alignment.ALLY) {
			if (hero.hasTalent(Talent.COMMAND_SYSTEM)) {
				if (Dungeon.level.distance(hero.pos, attacker.pos) <= 1+2*hero.pointsInTalent(Talent.COMMAND_SYSTEM)) {
					acuRoll *= 1.1f;
				}
			}
		}
		if (attacker == hero &&
			attacker.buff(WinterStorm.class) != null &&
			hero.hasTalent(Talent.IMPULSE_STRIKE) &&
			hero.pointsInTalent(Talent.IMPULSE_STRIKE) >= 1) {
			if (Random.Int(3) < 1) {
				acuRoll *= INFINITE_ACCURACY;
			}
		}
		acuRoll *= AscensionChallenge.statModifier(attacker);

		float defRoll = Random.Float( defStat );
		if (defender.buff(					Bless.class) != null) 	defRoll *= 1.25f;
		if (defender.buff( 		   AfterImageBuff.class) != null) 	defRoll *= INFINITE_EVASION;
		if (defender.buff( Gungnir.TwilightStance.class) != null) 	defRoll *= INFINITE_EVASION;
		if (defender.buff(	 		  		  Hex.class) != null) 	defRoll *= 0.8f;
		//total evasion increase is 10%/21%/33%/46%, based on points in talent
		if (defender.buff(	Thruster.ThrusterBuff.class) != null && hero.hasTalent(Talent.LIGHTWEIGHT_BOOSTER)) {
			defRoll *= Math.pow(1.1f, Dungeon.hero.pointsInTalent(Talent.LIGHTWEIGHT_BOOSTER));
		}
		for (ChampionEnemy buff : defender.buffs(ChampionEnemy.class)){
			defRoll *= buff.evasionAndAccuracyFactor();
		}
		defRoll *= AscensionChallenge.statModifier(defender);

		return (acuRoll * accMulti) >= defRoll;
	}
	
	public int attackSkill( Char target ) {
		return 0;
	}
	
	public int defenseSkill( Char enemy ) {
		return 0;
	}
	
	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}
	
	public int drRoll() {
		return 0;
	}
	
	public int damageRoll() {
		return 1;
	}
	
	//TODO it would be nice to have a pre-armor and post-armor proc.
	// atm attack is always post-armor and defence is already pre-armor
	
	public int attackProc( Char enemy, int damage ) {
		for (ChampionEnemy buff : buffs(ChampionEnemy.class)){
			buff.onAttackProc( enemy );
		}
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage ) {

		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb( damage );
		}

		return damage;
	}
	
	public float speed() {
		float speed = baseSpeed;
		if (buff(Cripple.class) != null) speed /= 2f;
		if (buff(Stamina.class) != null) speed *= 1.5f;
		if (buff(Adrenaline.class) != null) speed *= 2f;
		if (buff(StimpackAdrenaline.class) != null) speed *= 2f;
		if (buff(Haste.class) != null) speed *= 3f;
		if (buff(Dread.class) != null) speed *= 2f;
		if (buff(Murakumo.RushStance.class) != null) speed *= 1.5f;
		if (buff(AfterImageBuff.class) != null && hero.hasTalent(Talent.IMAGE_WALKING))
			speed *= 1f + 0.15f * hero.pointsInTalent(Talent.IMAGE_WALKING);
		if (buff(WinterStorm.class) != null) {
			if ((Dungeon.level.map[this.pos] == Terrain.DOOR || Dungeon.level.map[this.pos] == Terrain.OPEN_DOOR))
				speed *= 0.5f;
		}
		if (buff(CommandBuff.class) != null) speed *= 1.25f;
		DragonBuff dragonBuff = hero.buff(DragonBuff.class);
		if (dragonBuff != null && dragonBuff.isDragon()) {
			speed *= 1.5f;
		}
		if (buff(Thruster.ThrusterBuff.class) != null) speed *= 2f + 0.25f*hero.pointsInTalent(Talent.LIGHTWEIGHT_BOOSTER);
		return speed;
	}

	//currently only used by invisible chars, or by the hero
	public boolean canSurpriseAttack(){
		return true;
	}
	
	//used so that buffs(Shieldbuff.class) isn't called every time unnecessarily
	private int cachedShield = 0;
	public boolean needsShieldUpdate = true;
	
	public int shielding(){
		if (!needsShieldUpdate){
			return cachedShield;
		}
		
		cachedShield = 0;
		for (ShieldBuff s : buffs(ShieldBuff.class)){
			cachedShield += s.shielding();
		}
		needsShieldUpdate = false;
		return cachedShield;
	}
	
	public void damage( int dmg, Object src ) {
		
		if (!isAlive() || dmg < 0) {
			return;
		}
		if(isInvulnerable(src.getClass())){

			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
			return;
		}

		for (ChampionEnemy buff : buffs(ChampionEnemy.class)){
			dmg = (int) Math.ceil(dmg * buff.damageTakenFactor());
		}

		if (!(src instanceof LifeLink) && buff(LifeLink.class) != null){
			HashSet<LifeLink> links = buffs(LifeLink.class);
			for (LifeLink link : links.toArray(new LifeLink[0])){
				if (Actor.findById(link.object) == null){
					links.remove(link);
					link.detach();
				}
			}
			dmg = (int)Math.ceil(dmg / (float)(links.size()+1));
			for (LifeLink link : links){
				Char ch = (Char)Actor.findById(link.object);
				ch.damage(dmg, link);
				if (!ch.isAlive()){
					link.detach();
				}
			}
		}

		Terror t = buff(Terror.class);
		if (t != null){
			t.recover();
		}
		Dread d = buff(Dread.class);
		if (d != null){
			d.recover();
		}
		Charm c = buff(Charm.class);
		if (c != null){
			c.recover(src);
		}
		if (this.buff(Frost.class) != null){
			Buff.detach( this, Frost.class );
		}
		if (this.buff(MagicalSleep.class) != null){
			Buff.detach(this, MagicalSleep.class);
		}
		if (this.buff(Doom.class) != null){
			if (!isImmune(Doom.class)) {
				dmg *= 1.67;
			} else {
				dmg *= 2;
			}

		}
		if (alignment != Alignment.ALLY && this.buff(DeathMark.DeathMarkTracker.class) != null){
			dmg *= 1.25f;
		}
		if (alignment == Alignment.ALLY && this.buff(CommandBuff.class) != null){
			dmg *= 1.33f;
		}
		
		Class<?> srcClass = src.getClass();
		if (isImmune( srcClass )) {
			dmg = 0;
		} else {
			dmg = Math.round( dmg * resist( srcClass ));
		}
		
		//TODO improve this when I have proper damage source logic
		if (AntiMagic.RESISTS.contains(src.getClass()) && buff(ArcaneArmor.class) != null){
			dmg -= Random.NormalIntRange(0, buff(ArcaneArmor.class).level());
			if (dmg < 0) dmg = 0;
		}

		if (buff(Sickle.HarvestBleedTracker.class) != null){
			if (isImmune(Bleeding.class)){
				sprite.showStatus(CharSprite.POSITIVE, Messages.titleCase(Messages.get(this, "immune")));
				buff(Sickle.HarvestBleedTracker.class).detach();
				return;
			}
			Bleeding b = buff(Bleeding.class);
			if (b == null){
				b = new Bleeding();
			}
			b.announced = false;
			b.set(dmg*buff(Sickle.HarvestBleedTracker.class).bleedFactor, Sickle.HarvestBleedTracker.class);
			b.attachTo(this);
			sprite.showStatus(CharSprite.WARNING, Messages.titleCase(b.name()) + " " + (int)b.level());
			buff(Sickle.HarvestBleedTracker.class).detach();
			return;
		}
		if (buff(SurrationSaw.SawingBleedTracker.class) != null){
			if (isImmune(Bleeding.class)){
				sprite.showStatus(CharSprite.POSITIVE, Messages.titleCase(Messages.get(this, "immune")));
				buff(SurrationSaw.SawingBleedTracker.class).detach();
				return;
			}
			Bleeding b = buff(Bleeding.class);
			if (b == null){
				b = new Bleeding();
			}
			b.announced = false;
			b.set(dmg*buff(SurrationSaw.SawingBleedTracker.class).bleedFactor, SurrationSaw.SawingBleedTracker.class);
			b.attachTo(this);
			sprite.showStatus(CharSprite.WARNING, Messages.titleCase(b.name()) + " " + (int)b.level());
			buff(SurrationSaw.SawingBleedTracker.class).detach();
			return;
		}
		
		if (buff( Paralysis.class ) != null) {
			buff( Paralysis.class ).processDamage(dmg);
		}

		Endure.EndureTracker endure = buff(Endure.EndureTracker.class);
		if (endure != null){
			dmg = endure.enforceDamagetakenLimit(dmg);
		}

		int shielded = dmg;
		//FIXME: when I add proper damage properties, should add an IGNORES_SHIELDS property to use here.
		if (!(src instanceof Hunger)){
			for (ShieldBuff s : buffs(ShieldBuff.class)){
				dmg = s.absorbDamage(dmg);
				if (dmg == 0) break;
			}
		}
		shielded -= dmg;
		HP -= dmg;

		if (HP > 0 && buff(Grim.GrimTracker.class) != null){

			float finalChance = buff(Grim.GrimTracker.class).maxChance;
			finalChance *= (float)Math.pow( ((HT - HP) / (float)HT), 2);

			if (Random.Float() < finalChance) {
				int extraDmg = Math.round(HP*resist(Grim.class));
				dmg += extraDmg;
				HP -= extraDmg;

				sprite.emitter().burst( ShadowParticle.UP, 5 );
				if (!isAlive() && buff(Grim.GrimTracker.class).qualifiesForBadge){
					Badges.validateGrimWeapon();
				}
			}
		}
		if (HP < 0 && src instanceof Char && (alignment != Alignment.ENEMY || alignment == Alignment.ALLATTACK)){
			if (((Char) src).buff(Kinetic.KineticTracker.class) != null){
				int dmgToAdd = -HP;
				dmgToAdd -= ((Char) src).buff(Kinetic.KineticTracker.class).conservedDamage;
				dmgToAdd = Math.round(dmgToAdd * RingOfArcana.enchantPowerMultiplier((Char) src));
				if (dmgToAdd > 0) {
					Buff.affect((Char) src, Kinetic.ConservedDamage.class).setBonus(dmgToAdd);
				}
				((Char) src).buff(Kinetic.KineticTracker.class).detach();
			}
		}
		
		if (sprite != null) {
			sprite.showStatus(HP > HT / 2 ?
							CharSprite.WARNING :
							CharSprite.NEGATIVE,
					Integer.toString(dmg + shielded));
		}

		if (HP < 0) HP = 0;

		if (!isAlive()) {
			die( src );
		} else if (HP == 0 && buff(DeathMark.DeathMarkTracker.class) != null){
			DeathMark.processFearTheReaper(this);
		}
	}
	
	public void destroy() {
		HP = 0;
		Actor.remove( this );

		for (Char ch : Actor.chars().toArray(new Char[0])){
			if (ch.buff(Charm.class) != null && ch.buff(Charm.class).object == id()){
				ch.buff(Charm.class).detach();
			}
			if (ch.buff(Dread.class) != null && ch.buff(Dread.class).object == id()){
				ch.buff(Dread.class).detach();
			}
			if (ch.buff(Terror.class) != null && ch.buff(Terror.class).object == id()){
				ch.buff(Terror.class).detach();
			}
			if (ch.buff(SnipersMark.class) != null && ch.buff(SnipersMark.class).object == id()){
				ch.buff(SnipersMark.class).detach();
			}
			if (ch.buff(Talent.FollowupStrikeTracker.class) != null
					&& ch.buff(Talent.FollowupStrikeTracker.class).object == id()){
				ch.buff(Talent.FollowupStrikeTracker.class).detach();
			}
			if (ch.buff(Talent.DeadlyFollowupTracker.class) != null
					&& ch.buff(Talent.DeadlyFollowupTracker.class).object == id()){
				ch.buff(Talent.DeadlyFollowupTracker.class).detach();
			}
		}
	}
	
	public void die( Object src ) {
		destroy();
		if (src != Chasm.class) sprite.die();
	}

	//we cache this info to prevent having to call buff(...) in isAlive.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and thread coordination implications
	public boolean deathMarked = false;
	
	public boolean isAlive() {
		return HP > 0 || deathMarked;
	}

	@Override
	protected void spendConstant(float time) {
		TimekeepersHourglass.timeFreeze freeze = buff(TimekeepersHourglass.timeFreeze.class);
		if (freeze != null) {
			freeze.processTime(time);
			return;
		}

		Swiftthistle.TimeBubble bubble = buff(Swiftthistle.TimeBubble.class);
		if (bubble != null){
			bubble.processTime(time);
			return;
		}

		super.spendConstant(time);
	}

	@Override
	protected void spend( float time ) {

		float timeScale = 1f;
		if (buff( Slow.class ) != null) {
			timeScale *= 0.5f;
			//slowed and chilled do not stack
		} else if (buff( Chill.class ) != null) {
			timeScale *= buff( Chill.class ).speedFactor();
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 2.0f;
		}
		
		super.spend( time / timeScale );
	}
	
	public synchronized LinkedHashSet<Buff> buffs() {
		return new LinkedHashSet<>(buffs);
	}
	
	@SuppressWarnings("unchecked")
	//returns all buffs assignable from the given buff class
	public synchronized <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}

	@SuppressWarnings("unchecked")
	//returns an instance of the specific buff class, if it exists. Not just assignable
	public synchronized  <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (b.getClass() == c) {
				return (T)b;
			}
		}
		return null;
	}

	public synchronized boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}

	public synchronized boolean add( Buff buff ) {

		if (buff(PotionOfCleansing.Cleanse.class) != null) { //cleansing buff
			if (buff.type == Buff.buffType.NEGATIVE
					&& !(buff instanceof AllyBuff)
					&& !(buff instanceof LostInventory)){
				return false;
			}
		}

		if (sprite != null && buff(Challenge.SpectatorFreeze.class) != null){
			return false; //can't add buffs while frozen and game is loaded
		}

		buffs.add( buff );
		if (Actor.chars().contains(this)) Actor.add( buff );

		if (sprite != null && buff.announced) {
			switch (buff.type) {
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, Messages.titleCase(buff.name()));
					break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.NEGATIVE, Messages.titleCase(buff.name()));
					break;
				case NEUTRAL:
				default:
					sprite.showStatus(CharSprite.NEUTRAL, Messages.titleCase(buff.name()));
					break;
			}
		}

		return true;

	}
	
	public synchronized void remove( Buff buff ) {
		
		buffs.remove( buff );
		Actor.remove( buff );

	}
	
	public synchronized void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs( buffClass )) {
			remove( buff );
		}
	}
	
	@Override
	protected synchronized void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[buffs.size()])) {
			buff.detach();
		}
	}
	
	public synchronized void updateSpriteState() {
		for (Buff buff:buffs) {
			buff.fx( true );
		}
	}
	
	public float stealth() {
		return 0;
	}

	public final void move( int step ) {
		move( step, true );
	}

	//travelling may be false when a character is moving instantaneously, such as via teleportation
	public void move( int step, boolean travelling ) {

		if (travelling && Dungeon.level.adjacent( step, pos ) && buff( Vertigo.class ) != null) {
			sprite.interruptMotion();
			int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			if (!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos])
					|| (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[newPos])
					|| Actor.findChar( newPos ) != null)
				return;
			else {
				sprite.move(pos, newPos);
				step = newPos;
			}
		}

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

		pos = step;
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.level.heroFOV[pos];
		}
		
		Dungeon.level.occupyCell(this );
	}
	
	public int distance( Char other ) {
		return Dungeon.level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}
	
	public void onAttackComplete() {
		next();
	}
	
	public void onOperateComplete() {
		next();
	}
	
	protected final HashSet<Class> resistances = new HashSet<>();
	
	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Class effect ){
		HashSet<Class> resists = new HashSet<>(resistances);
		for (Property p : properties()){
			resists.addAll(p.resistances());
		}
		for (Buff b : buffs()){
			resists.addAll(b.resistances());
		}
		
		float result = 1f;
		for (Class c : resists){
			if (c.isAssignableFrom(effect)){
				result *= 0.5f;
			}
		}
		return result * RingOfElements.resist(this, effect);
	}
	
	protected final HashSet<Class> immunities = new HashSet<>();
	
	public boolean isImmune(Class effect ){
		HashSet<Class> immunes = new HashSet<>(immunities);
		for (Property p : properties()){
			immunes.addAll(p.immunities());
		}
		for (Buff b : buffs()){
			immunes.addAll(b.immunities());
		}
		
		for (Class c : immunes){
			if (c.isAssignableFrom(effect)){
				return true;
			}
		}
		return false;
	}

	//similar to isImmune, but only factors in damage.
	//Is used in AI decision-making
	public boolean isInvulnerable( Class effect ){
		return buff(Challenge.SpectatorFreeze.class) != null;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		HashSet<Property> props = new HashSet<>(properties);
		//TODO any more of these and we should make it a property of the buff, like with resistances/immunities
		if (buff(ChampionEnemy.Giant.class) != null) {
			props.add(Property.LARGE);
		}
		return props;
	}

	public enum Property{
		BOSS ( new HashSet<Class>( Arrays.asList(Grim.class, GrimTrap.class, ScrollOfRetribution.class, ScrollOfPsionicBlast.class, NoEnergy.class)),
				new HashSet<Class>( Arrays.asList(AllyBuff.class, Dread.class) )),
		MINIBOSS ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(AllyBuff.class, Dread.class, NoEnergy.class) )),
		BOSS_MINION,
		UNDEAD,
		DEMONIC,
		INORGANIC ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class) )),
		FIERY ( new HashSet<Class>( Arrays.asList(WandOfFireblast.class, Elemental.FireElemental.class)),
				new HashSet<Class>( Arrays.asList(Burning.class, Blazing.class))),
		ICY ( new HashSet<Class>( Arrays.asList(WandOfFrost.class, Elemental.FrostElemental.class)),
				new HashSet<Class>( Arrays.asList(Frost.class, Chill.class))),
		ACIDIC ( new HashSet<Class>( Arrays.asList(Corrosion.class)),
				new HashSet<Class>( Arrays.asList(Ooze.class))),
		ELECTRIC ( new HashSet<Class>( Arrays.asList(WandOfLightning.class, Shocking.class, Potential.class, Electricity.class, ShockingDart.class, Elemental.ShockElemental.class )),
				new HashSet<Class>()),
		LARGE,
		IMMOVABLE;
		
		private HashSet<Class> resistances;
		private HashSet<Class> immunities;
		
		Property(){
			this(new HashSet<Class>(), new HashSet<Class>());
		}
		
		Property( HashSet<Class> resistances, HashSet<Class> immunities){
			this.resistances = resistances;
			this.immunities = immunities;
		}
		
		public HashSet<Class> resistances(){
			return new HashSet<>(resistances);
		}
		
		public HashSet<Class> immunities(){
			return new HashSet<>(immunities);
		}

	}

	public static boolean hasProp( Char ch, Property p){
		return (ch != null && ch.properties().contains(p));
	}
}
