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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

//icons for hero subclasses and abilities atm, maybe add classes?
public class HeroIcon extends Image {

	private static TextureFilm film;
	private static final int SIZE = 16;

	//transparent icon
	public static final int NONE    = 31;

	//subclasses
	public static final int BERSERKER   	= 0;
	public static final int GLADIATOR   	= 1;
	public static final int SPECOPS 		= 21;
	public static final int BATTLEMAGE  	= 2;
	public static final int WARLOCK     	= 3;
	public static final int ORACLE      	= 22;
	public static final int ASSASSIN    	= 4;
	public static final int FREERUNNER  	= 5;
	public static final int HITMAN			= 23;
	public static final int SNIPER      	= 6;
	public static final int WARDEN      	= 7;
	public static final int POLARIS			= 24;
	public static final int TRIGGERHAPPY	= 25;
	public static final int DEMOLITIONIST	= 26;
	public static final int BUNKER			= 27;
	public static final int PHALANX			= 31;
	public static final int TERCIO			= 32;
	public static final int VLAD			= 33;
	public static final int CHALLENGER		= 37;
	public static final int BOUNTY_HUNTER	= 38;
	public static final int CENOBITE		= 39;
	public static final int PERETORIA		= 43;
	public static final int VALKYRIE		= 44;
	public static final int WINTERSTORM		= 45;
	public static final int DEFENDER		= 49;
	public static final int CAPTAIN			= 50;
	public static final int DRAGON			= 51;
	public static final int LIBRARIAN		= 55;
	public static final int DEVASTATOR		= 56;
	public static final int IMMORTAL		= 57;

	//abilities
	public static final int HEROIC_LEAP     = 8;
	public static final int SHOCKWAVE       = 9;
	public static final int ENDURE          = 10;
	public static final int ELEMENTAL_BLAST = 11;
	public static final int WILD_MAGIC      = 12;
	public static final int WARP_BEACON     = 13;
	public static final int SMOKE_BOMB      = 14;
	public static final int DEATH_MARK      = 15;
	public static final int SHADOW_CLONE    = 16;
	public static final int SPECTRAL_BLADES = 17;
	public static final int NATURES_POWER   = 18;
	public static final int SPIRIT_HAWK     = 19;
	public static final int STIMPACK		= 28;
	public static final int SENTRY_GUN		= 29;
	public static final int DANGER_CLOSE	= 30;
	public static final int STARBURST 		= 34;
	public static final int AFTERIMAGE 		= 35;
	public static final int BLOOD_WINE		= 36;
	public static final int CHALLENGE		= 40;
	public static final int ELEMENTAL_STRIKE= 41;
	public static final int FEINT			= 42;
	public static final int COMMAND_ORDER 	= 46;
	public static final int FROST_WIND	 	= 47;
	public static final int DEEP_STRIKE		= 48;
	public static final int RATMOGRIFY      = 20;

	//action indicator visuals
	public static final int BERSERK         = 64;
	public static final int COMBO           = 65;
	public static final int PREPARATION     = 66;
	public static final int MOMENTUM        = 67;
	public static final int SNIPERS_MARK    = 68;
	public static final int WEAPON_SWAP     = 69;
	public static final int CENOBITE_ABILITY= 70;
	public static final int USE_BUNKER      = 71;
	public static final int USE_PHALANX     = 72;
	public static final int USE_TERCIO      = 73;
	public static final int USE_VLAD        = 74;
	public static final int USE_BOUNTYMARK  = 75;
	public static final int USE_SHIP        = 76;


	public HeroIcon(HeroSubClass subCls){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(subCls.icon()));
	}

	public HeroIcon(ArmorAbility abil){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(abil.icon()));
	}

	public HeroIcon(ActionIndicator.Action action){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(action.actionIcon()));
	}

}
