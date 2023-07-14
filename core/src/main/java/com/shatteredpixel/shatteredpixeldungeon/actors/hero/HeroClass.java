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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artilia.DeepStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artilia.FrostWind;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artilia.OffenceOrder;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.carroll.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.carroll.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.carroll.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.lance.AfterImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.lance.BloodWine;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.lance.Starburst;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.magnus.ImmobileTrap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.magnus.InrushBarrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.magnus.Thruster;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noise.DangerClose;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noise.SentryGun;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noise.Stimpack;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.PrincessMirror;
import com.shatteredpixel.shatteredpixeldungeon.items.ReactiveShield;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.IceBox;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.ApachePistol;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.NotMachineGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CommonBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Krystallos;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RuinSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR, HeroSubClass.SPECOPS ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK, HeroSubClass.ORACLE ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER, HeroSubClass.HITMAN ),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN, HeroSubClass.POLARIS ),
	NOISE( HeroSubClass.TRIGGERHAPPY, HeroSubClass.DEMOLITIONIST, HeroSubClass.BUNKER),
	LANCE( HeroSubClass.PHALANX, HeroSubClass.TERCIO, HeroSubClass.VLAD ),
	CARROLL( HeroSubClass.CHALLENGER, HeroSubClass.BOUNTYHUNTER, HeroSubClass.CENOBITE ),
	MAGNUS( HeroSubClass.DEFENDER, HeroSubClass.CAPTAIN, HeroSubClass.DRAGON ),
	//MAGNUS( HeroSubClass.DEFENDER, HeroSubClass.CAPTAIN, HeroSubClass.DRAGON ),
	ARTILIA( HeroSubClass.PERETORIA, HeroSubClass.VALKYRIE, HeroSubClass.WINTERSTORM );
	//VANGUARD( HeroSubClass.LIBRARIAN, HeroSubClass.DEVASTATOR, HeroSubClass.IMMORTAL );

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		new IceBox().collect();
		Dungeon.LimitedDrops.ICE_BOX.drop();

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new ScrollOfIdentify().identify();
		new Pickaxe().identify();

		if (Dungeon.isChallenged(Challenges.EASY_MODE)) {
			new PotionOfHealing().identify();
			new PotionOfStrength().identify();
			new PotionOfExperience().identify();
			new ScrollOfUpgrade().identify();
			new ScrollOfRemoveCurse().identify();
			new ScrollOfTransmutation().identify();
		}
		if (Dungeon.isChallenged(Challenges.EXCAVATION)) {
			RingOfWealth excavation = new RingOfWealth();
			(hero.belongings.ring = excavation).identify();
			hero.belongings.ring.activate( hero );
		}
//erase this start
		/**
		PotionOfStrength strpotion = new PotionOfStrength();
		strpotion.quantity(10).collect();

		PlateArmor plate = new PlateArmor();
		plate.upgrade(100).collect();
		new KingsCrown().collect();
		new TengusMask().collect();
		new Supernova().collect();
		new SuperShotgun().collect();

		ScrollOfUpgrade upscroll = new ScrollOfUpgrade();
		upscroll.quantity(30).collect();
		ScrollOfIdentify ident = new ScrollOfIdentify();
		ident.quantity(31).collect();
		PotionOfExperience enchan = new PotionOfExperience();
		enchan.quantity(100).collect();
		MeatPie augm = new MeatPie();
		augm.quantity(100).collect();
		new Ankh().collect();
		new RingOfEnergy().collect();
		Hush plate = new Hush();
		plate.upgrade(8).collect();
		 **/
//erase this finish
		new TengusMask().collect();
		new KingsCrown().collect();
		PotionOfExperience enchan = new PotionOfExperience();
		enchan.quantity(100).collect();
		RingOfEnergy plate = new RingOfEnergy();
		plate.upgrade(100).collect();

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case NOISE:
				initNoise( hero );
				break;

			case LANCE:
				initLance( hero );
				break;

			case CARROLL:
				initCarroll( hero );
				break;

			case MAGNUS:
				initMagnus( hero );
				break;

			case ARTILIA:
				initArtilia( hero );
				break;
		}

		for (int s = 0; s < QuickSlot.SIZE; s++){
			if (Dungeon.quickslot.getItem(s) == null){
				Dungeon.quickslot.setSlot(s, waterskin);
				break;
			}
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case NOISE:
				return Badges.Badge.MASTERY_NOISE;
			case LANCE:
				return Badges.Badge.MASTERY_LANCE;
			case CARROLL:
				return Badges.Badge.MASTERY_CARROLL;
			case MAGNUS:
				return Badges.Badge.MASTERY_MAGNUS;
			case ARTILIA:
				return Badges.Badge.MASTERY_ARTILIA;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
		new RingOfMight().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
		new RingOfEnergy().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
		new RingOfHaste().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
		new RingOfSharpshooting().identify();
	}

	private static void initNoise( Hero hero ) {

		NotMachineGun gun = new NotMachineGun();
		(hero.belongings.weapon = gun).identify();

		if (hero.belongings.weapon != null && hero.belongings.weapon instanceof FirearmWeapon){
			((FirearmWeapon) hero.belongings.weapon).affixLoader(new SpeedLoader());
		}

		Dungeon.quickslot.setSlot(0, gun);

		new PotionOfHaste().identify();
		new ScrollOfTeleportation().identify();
		new RingOfReload().identify();
	}

	private static void initLance( Hero hero ) {

		(hero.belongings.weapon = new RuinSpear()).identify();

		new PotionOfParalyticGas().identify();
		new ScrollOfTerror().identify();
		new RingOfFuror().identify();
	}

	private static void initCarroll( Hero hero ) {

		ApachePistol gun = new ApachePistol();
		(hero.belongings.weapon = gun).identify();

		ThrowingSpike spikes = new ThrowingSpike();
		spikes.quantity(2).collect();

		Dungeon.quickslot.setSlot(0, gun);
		Dungeon.quickslot.setSlot(1, spikes);

		new PotionOfStrength().identify();
		new ScrollOfTransmutation().identify();
		new RingOfAccuracy().identify();
	}

	private static void initMagnus( Hero hero ) {

		(hero.belongings.weapon = new CommonBlade()).identify();

		ReactiveShield shield = new ReactiveShield();
		shield.identify().collect();

		Dungeon.quickslot.setSlot(0, shield);

		new PotionOfExperience().identify();
		new ScrollOfRemoveCurse().identify();
		new RingOfElements().identify();
	}

	private static void initArtilia( Hero hero ) {

		(hero.belongings.weapon = new Krystallos()).identify();

		PrincessMirror mirror = new PrincessMirror();
		mirror.identify().collect();

		Dungeon.quickslot.setSlot(0, mirror);

		new PotionOfFrost().identify();
		new ScrollOfMirrorImage().identify();
		new RingOfWealth().identify();
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public String shortDesc(){
		return Messages.get(HeroClass.class, name()+"_desc_short");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities(){
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
			case MAGE:
				return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
			case ROGUE:
				return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
			case HUNTRESS:
				return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
			case NOISE:
				return new ArmorAbility[]{new Stimpack(), new SentryGun(), new DangerClose()};
			case LANCE:
				return new ArmorAbility[]{new Starburst(), new AfterImage(), new BloodWine()};
			case CARROLL:
				return new ArmorAbility[]{new Challenge(), new ElementalStrike(), new Feint()};
			case MAGNUS:
				return new ArmorAbility[]{new InrushBarrier(), new ImmobileTrap(), new Thruster()};
			case ARTILIA:
				return new ArmorAbility[]{new OffenceOrder(), new FrostWind(), new DeepStrike()};
		}
	}

	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
			case NOISE:
				return Assets.Sprites.NOISE;
			case LANCE:
				return Assets.Sprites.LANCE;
			case CARROLL:
				return Assets.Sprites.CARROLL;
			case MAGNUS:
				return Assets.Sprites.MAGNUS;
			case ARTILIA:
				return Assets.Sprites.ARTILIA;
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
			case NOISE:
				return Assets.Splashes.NOISE;
			case LANCE:
				return Assets.Splashes.LANCE;
			case CARROLL:
				return Assets.Splashes.CARROLL;
			case MAGNUS:
				return Assets.Splashes.MAGNUS;
			case ARTILIA:
				return Assets.Splashes.ARTILIA;
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;
		
		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			case NOISE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_NOISE);
			case LANCE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_LANCE);
			case CARROLL:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_CARROLL);
			case MAGNUS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGNUS);
			case ARTILIA:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ARTILIA);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
