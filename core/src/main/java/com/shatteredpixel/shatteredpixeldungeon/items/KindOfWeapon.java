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

package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DragonBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

abstract public class KindOfWeapon extends EquipableItem {
	
	protected static final float TIME_TO_EQUIP = 1f;

	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;

	public boolean firearm = false;
	public boolean firearmPistol = false;
	public boolean firearmAuto = false;
	public boolean firearmPrecision = false;
	public boolean firearmShotgun = false;
	public boolean firearmExplosive = false;
	public boolean firearmEnergy = false;
	public boolean firearmEtc = false;
	public boolean bullet = false;
	public boolean weaponarm = false;

	@Override
	public void execute(Hero hero, String action) {
		if (hero.subClass == HeroSubClass.CHALLENGER && action.equals(AC_EQUIP)){
			usesTargeting = false;
			String primaryName = Messages.titleCase(hero.belongings.weapon != null ? hero.belongings.weapon.trueName() : Messages.get(KindOfWeapon.class, "empty"));
			String secondaryName = Messages.titleCase(hero.belongings.secondWep != null ? hero.belongings.secondWep.trueName() : Messages.get(KindOfWeapon.class, "empty"));
			if (primaryName.length() > 18) primaryName = primaryName.substring(0, 15) + "...";
			if (secondaryName.length() > 18) secondaryName = secondaryName.substring(0, 15) + "...";
			GameScene.show(new WndOptions(
					new ItemSprite(this),
					Messages.titleCase(name()),
					Messages.get(KindOfWeapon.class, "which_equip_msg"),
					Messages.get(KindOfWeapon.class, "which_equip_primary", primaryName),
					Messages.get(KindOfWeapon.class, "which_equip_secondary", secondaryName)
			){
				@Override
				protected void onSelect(int index) {
					super.onSelect(index);
					if (index == 0){
						int slot = Dungeon.quickslot.getSlot( KindOfWeapon.this );
						doEquip(hero);
						if (slot != -1) {
							Dungeon.quickslot.setSlot( slot, KindOfWeapon.this );
							updateQuickslot();
						}
					}
					if (index == 1){
						int slot = Dungeon.quickslot.getSlot( KindOfWeapon.this );
						equipSecondary(hero);
						if (slot != -1) {
							Dungeon.quickslot.setSlot( slot, KindOfWeapon.this );
							updateQuickslot();
						}
					}
				}
			});
		} else {
			super.execute(hero, action);
		}
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.weapon() == this || hero.belongings.secondWep() == this;
	}
	
	@Override
	public boolean doEquip( Hero hero ) {

		detachAll( hero.belongings.backpack );
		
		if (hero.belongings.weapon == null || hero.belongings.weapon.doUnequip( hero, true )) {
			
			hero.belongings.weapon = this;
			activate( hero );
			Talent.onItemEquipped(hero, this);
			Badges.validateCarrollUnlock();
			ActionIndicator.refresh();
			updateQuickslot();
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
			}

			if (hero.hasTalent(Talent.SWIFT_EQUIP)) {
				if (hero.buff(Talent.SwiftEquipCooldown.class) == null){
					hero.spendAndNext(-hero.cooldown());
					Buff.affect(hero, Talent.SwiftEquipCooldown.class, 19f)
							.secondUse = hero.pointsInTalent(Talent.SWIFT_EQUIP) == 2;
					GLog.i(Messages.get(this, "swift_equip"));
				} else if (hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()) {
					hero.spendAndNext(-hero.cooldown());
					hero.buff(Talent.SwiftEquipCooldown.class).secondUse = false;
					GLog.i(Messages.get(this, "swift_equip"));
				} else {
					hero.spendAndNext(TIME_TO_EQUIP);
				}
			} else {
				hero.spendAndNext(TIME_TO_EQUIP);
			}
			return true;
			
		} else {
			
			collect( hero.belongings.backpack );
			return false;
		}
	}


	public boolean equipSecondary( Hero hero ){
		detachAll( hero.belongings.backpack );

		if (hero.belongings.secondWep == null || hero.belongings.secondWep.doUnequip( hero, true )) {

			hero.belongings.secondWep = this;
			activate( hero );
			Talent.onItemEquipped(hero, this);
			Badges.validateCarrollUnlock();
			ActionIndicator.refresh();
			updateQuickslot();

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
			}

			if (hero.hasTalent(Talent.SWIFT_EQUIP)) {
				if (hero.buff(Talent.SwiftEquipCooldown.class) == null) {
					hero.spendAndNext(-hero.cooldown());
					Buff.affect(hero, Talent.SwiftEquipCooldown.class, 19f)
							.secondUse = hero.pointsInTalent(Talent.SWIFT_EQUIP) == 2;
					GLog.i(Messages.get(this, "swift_equip"));
				} else if (hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()) {
					hero.spendAndNext(-hero.cooldown());
					hero.buff(Talent.SwiftEquipCooldown.class).secondUse = false;
					GLog.i(Messages.get(this, "swift_equip"));
				} else {
					hero.spendAndNext(TIME_TO_EQUIP);
				}
			} else {
				hero.spendAndNext(TIME_TO_EQUIP);
			}
			return true;

		} else {

			collect( hero.belongings.backpack );
			return false;
		}
	}


	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		boolean second = hero.belongings.secondWep == this;

		if (second){
			//do this first so that the item can go to a full inventory
			hero.belongings.secondWep = null;
		}

		if (super.doUnequip( hero, collect, single )) {

			if (!second){
				hero.belongings.weapon = null;
			}
			return true;

		} else {

			if (second){
				hero.belongings.secondWep = this;
			}
			return false;

		}
	}

	public int min(){
		return min(buffedLvl());
	}

	public int max(){
		return max(buffedLvl());
	}

	public int STRReq() {
		return STRReq();
	}

	abstract public int min(int lvl);
	abstract public int max(int lvl);

	public int damageRoll( Char owner ) {
		int critChance = 0;

		DragonBuff dragonBuff = owner.buff(DragonBuff.class);
		if (dragonBuff != null && dragonBuff.isDragon()) {
			critChance += 2*hero.STR();
			if (hero.hasTalent(Talent.OLD_MEMORY_III)) {
				critChance += 2*dragonBuff.attackBonus(((Hero) owner).STR() - ((Hero) owner).belongings.armor.STRReq());
			}
		}

		if (owner == Dungeon.hero && Dungeon.hero.belongings.weapon() instanceof MeleeWeapon) {
			if ( critChance > 0 ) {
				if (Random.Int(100) < critChance) {
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					Dungeon.hero.sprite.showStatus(CharSprite.NEUTRAL, "!");
					int damageBonus = 0;
					return Random.NormalIntRange(Math.round(0.75f * max()), max()) + damageBonus;
				} else {
					return Random.NormalIntRange( min(), max() );
				}
			} else {
				if (Random.Int(100) < -critChance) {
					Dungeon.hero.sprite.showStatus(CharSprite.NEUTRAL, "?");
					return Random.NormalIntRange(min(), Math.round(0.5f * max()));
				} else {
					return Random.NormalIntRange( min(), max() );
				}
			}
		} else {
			return Random.NormalIntRange( min(), max() );
		}
	}
	
	public float accuracyFactor( Char owner, Char target ) {
		return 1f;
	}
	
	public float delayFactor( Char owner ) {
		return 1f;
	}

	public int reachFactor( Char owner ){
		return 1;
	}
	
	public boolean canReach( Char owner, int target){
		if (Dungeon.level.distance( owner.pos, target ) > reachFactor(owner)){
			return false;
		} else {
			boolean[] passable = BArray.not(Dungeon.level.solid, null);
			for (Char ch : Actor.chars()) {
				if (ch != owner) passable[ch.pos] = false;
			}
			
			PathFinder.buildDistanceMap(target, passable, reachFactor(owner));
			
			return PathFinder.distance[owner.pos] <= reachFactor(owner);
		}
	}

	public int defenseFactor( Char owner ) {
		return 0;
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		return damage;
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(hitSound, 1, pitch * hitSoundPitch);
	}
	
}
