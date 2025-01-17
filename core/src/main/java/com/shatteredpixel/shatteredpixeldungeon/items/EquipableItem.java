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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Blunderbust;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Harmonica;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Madness;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Revolver;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.ShortCarbine;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Tat;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Vega;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public abstract class EquipableItem extends Item {

	public static final String AC_EQUIP		= "EQUIP";
	public static final String AC_UNEQUIP	= "UNEQUIP";

	{
		bones = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		if (super.doPickUp(hero, pos)){
			if (!isIdentified() && !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_IDING)){
				GLog.p(Messages.get(Guidebook.class, "hint"));
				GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_IDING);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_EQUIP )) {
			//In addition to equipping itself, item reassigns itself to the quickslot
			//This is a special case as the item is being removed from inventory, but is staying with the hero.
			int slot = Dungeon.quickslot.getSlot( this );
			doEquip(hero);
			if (slot != -1) {
				Dungeon.quickslot.setSlot( slot, this );
				updateQuickslot();
			}
		} else if (action.equals( AC_UNEQUIP )) {
			doUnequip( hero, true );
			if (hero.buff(Revolver.APShot.class) != null) {
				if (hero.belongings.weapon == null && hero.belongings.secondWep == null) {
					hero.buff(Revolver.APShot.class).detach();
				} else {
					if (hero.belongings.weapon == null) {
						if (!hero.belongings.secondWep.firearmPistol) {
							hero.buff(Revolver.APShot.class).detach();
						}
					} else if (hero.belongings.secondWep == null) {
						if (!hero.belongings.weapon.firearmPistol) {
							hero.buff(Revolver.APShot.class).detach();
						}
					} else {
						if (!hero.belongings.weapon.firearmPistol && !hero.belongings.secondWep.firearmPistol) {
							hero.buff(Revolver.APShot.class).detach();
						}
					}
				}
			}
			if (hero.buff(Tat.PrecisionShot.class) != null) {
				if (hero.belongings.weapon == null && hero.belongings.secondWep == null) {
					hero.buff(Tat.PrecisionShot.class).detach();
				} else {
					if (hero.belongings.weapon == null) {
						if (!hero.belongings.secondWep.firearmPrecision) {
							hero.buff(Tat.PrecisionShot.class).detach();
						}
					} else if (hero.belongings.secondWep == null) {
						if (!hero.belongings.weapon.firearmPrecision) {
							hero.buff(Tat.PrecisionShot.class).detach();
						}
					} else {
						if (!hero.belongings.weapon.firearmPrecision && !hero.belongings.secondWep.firearmPrecision) {
							hero.buff(Tat.PrecisionShot.class).detach();
						}
					}
				}
			}
			if (hero.buff(ShortCarbine.InfiniteShot.class) != null) {
				if (hero.belongings.weapon == null && hero.belongings.secondWep == null) {
					hero.buff(ShortCarbine.InfiniteShot.class).detach();
				} else {
					if (hero.belongings.weapon == null) {
						if (!hero.belongings.secondWep.firearmAuto) {
							hero.buff(ShortCarbine.InfiniteShot.class).detach();
						}
					} else if (hero.belongings.secondWep == null) {
						if (!hero.belongings.weapon.firearmAuto) {
							hero.buff(ShortCarbine.InfiniteShot.class).detach();
						}
					} else {
						if (!hero.belongings.weapon.firearmAuto && !hero.belongings.secondWep.firearmAuto) {
							hero.buff(ShortCarbine.InfiniteShot.class).detach();
						}
					}
				}
			}
			if (hero.buff(Blunderbust.SlugShot.class) != null) {
				if (hero.belongings.weapon == null && hero.belongings.secondWep == null) {
					hero.buff(Blunderbust.SlugShot.class).detach();
				} else {
					if (hero.belongings.weapon == null) {
						if (!hero.belongings.secondWep.firearmShotgun) {
							hero.buff(Blunderbust.SlugShot.class).detach();
						}
					} else if (hero.belongings.secondWep == null) {
						if (!hero.belongings.weapon.firearmShotgun) {
							hero.buff(Blunderbust.SlugShot.class).detach();
						}
					} else {
						if (!hero.belongings.weapon.firearmShotgun && !hero.belongings.secondWep.firearmShotgun) {
							hero.buff(Blunderbust.SlugShot.class).detach();
						}
					}
				}
			}
			if (hero.buff(Harmonica.GuidedShot.class) != null) {
				if (hero.belongings.weapon == null && hero.belongings.secondWep == null) {
					hero.buff(Harmonica.GuidedShot.class).detach();
				} else {
					if (hero.belongings.weapon == null) {
						if (!hero.belongings.secondWep.firearmExplosive) {
							hero.buff(Harmonica.GuidedShot.class).detach();
						}
					} else if (hero.belongings.secondWep == null) {
						if (!hero.belongings.weapon.firearmExplosive) {
							hero.buff(Harmonica.GuidedShot.class).detach();
						}
					} else {
						if (!hero.belongings.weapon.firearmExplosive && !hero.belongings.secondWep.firearmExplosive) {
							hero.buff(Harmonica.GuidedShot.class).detach();
						}
					}
				}
			}
			if (hero.buff(Vega.BreakerShot.class) != null) {
				if (hero.belongings.weapon == null && hero.belongings.secondWep == null) {
					hero.buff(Vega.BreakerShot.class).detach();
				} else {
					if (hero.belongings.weapon == null) {
						if (!hero.belongings.secondWep.firearmEnergy) {
							hero.buff(Vega.BreakerShot.class).detach();
						}
					} else if (hero.belongings.secondWep == null) {
						if (!hero.belongings.weapon.firearmEnergy) {
							hero.buff(Vega.BreakerShot.class).detach();
						}
					} else {
						if (!hero.belongings.weapon.firearmEnergy && !hero.belongings.secondWep.firearmEnergy) {
							hero.buff(Vega.BreakerShot.class).detach();
						}
					}
				}
			}
			if (hero.buff(Madness.OverCharge.class) != null) {
				if (hero.belongings.weapon == null && hero.belongings.secondWep == null) {
					hero.buff(Madness.OverCharge.class).detach();
				} else {
					if (hero.belongings.weapon == null) {
						if (!hero.belongings.secondWep.firearmEtc) {
							hero.buff(Madness.OverCharge.class).detach();
						}
					} else if (hero.belongings.secondWep == null) {
						if (!hero.belongings.weapon.firearmEtc) {
							hero.buff(Madness.OverCharge.class).detach();
						}
					} else {
						if (!hero.belongings.weapon.firearmEtc && !hero.belongings.secondWep.firearmEtc) {
							hero.buff(Madness.OverCharge.class).detach();
						}
					}
				}
			}
		}
	}

	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || doUnequip( hero, false, false )) {
			super.doDrop( hero );
		}
	}

	@Override
	public void cast( final Hero user, int dst ) {

		if (isEquipped( user )) {
			if (quantity == 1 && !this.doUnequip( user, false, false )) {
				return;
			}
		}

		super.cast( user, dst );
	}

	public static void equipCursed( Hero hero ) {
		hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
		Sample.INSTANCE.play( Assets.Sounds.CURSED );
	}

	protected float time2equip( Hero hero ) {
		return 1;
	}

	public abstract boolean doEquip( Hero hero );

	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {

		if (cursed && hero.buff(MagicImmune.class) == null) {
			GLog.w(Messages.get(EquipableItem.class, "unequip_cursed"));
			return false;
		}

		if (single) {
			hero.spendAndNext( time2equip( hero ) );
		} else {
			hero.spend( time2equip( hero ) );
		}

		//temporarily keep this item so it can be collected
		boolean wasKept = keptThoughLostInvent;
		keptThoughLostInvent = true;
		if (!collect || !collect( hero.belongings.backpack )) {
			onDetach();
			Dungeon.quickslot.clearItem(this);
			updateQuickslot();
			if (collect) Dungeon.level.drop( this, hero.pos );
		}
		keptThoughLostInvent = wasKept;

		return true;
	}

	final public boolean doUnequip( Hero hero, boolean collect ) {
		return doUnequip( hero, collect, true );
	}

	public void activate( Char ch ){}
}
