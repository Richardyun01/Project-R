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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ItemSpriteSheet {

	private static final int WIDTH = 16;
	public static final int SIZE = 16;

	public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEMS, SIZE, SIZE );

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

	private static void assignItemRect( int item, int width, int height ){
		int x = (item % WIDTH) * SIZE;
		int y = (item / WIDTH) * SIZE;
		film.add( item, x, y, x+width, y+height);
	}

	private static final int PLACEHOLDERS   =                               xy(1, 1);   //16 slots
	//SOMETHING is the default item sprite at position 0. May show up ingame if there are bugs.
	public static final int SOMETHING       = PLACEHOLDERS+0;
	public static final int WEAPON_HOLDER   = PLACEHOLDERS+1;
	public static final int ARMOR_HOLDER    = PLACEHOLDERS+2;
	public static final int MISSILE_HOLDER  = PLACEHOLDERS+3;
	public static final int WAND_HOLDER     = PLACEHOLDERS+4;
	public static final int RING_HOLDER     = PLACEHOLDERS+5;
	public static final int ARTIFACT_HOLDER = PLACEHOLDERS+6;
	public static final int FOOD_HOLDER     = PLACEHOLDERS+7;
	public static final int BOMB_HOLDER     = PLACEHOLDERS+8;
	public static final int POTION_HOLDER   = PLACEHOLDERS+9;
	public static final int SCROLL_HOLDER   = PLACEHOLDERS+11;
	public static final int SEED_HOLDER     = PLACEHOLDERS+10;
	public static final int STONE_HOLDER    = PLACEHOLDERS+12;
	public static final int CATA_HOLDER     = PLACEHOLDERS+13;
	public static final int ELIXIR_HOLDER   = PLACEHOLDERS+14;
	public static final int SPELL_HOLDER    = PLACEHOLDERS+15;
	static{
		assignItemRect(SOMETHING,       8,  13);
		assignItemRect(WEAPON_HOLDER,   14, 14);
		assignItemRect(ARMOR_HOLDER,    14, 12);
		assignItemRect(MISSILE_HOLDER,  15, 15);
		assignItemRect(WAND_HOLDER,     14, 14);
		assignItemRect(RING_HOLDER,     8,  11);
		assignItemRect(ARTIFACT_HOLDER, 15, 13);
		assignItemRect(FOOD_HOLDER,     15, 11);
		assignItemRect(BOMB_HOLDER,     12, 12);
		assignItemRect(POTION_HOLDER,   12, 12);
		assignItemRect(SEED_HOLDER,     13, 15);
		assignItemRect(SCROLL_HOLDER,   12, 16);
		assignItemRect(STONE_HOLDER,    16, 16);
		assignItemRect(CATA_HOLDER,     6,  15);
		assignItemRect(ELIXIR_HOLDER,   12, 16);
		assignItemRect(SPELL_HOLDER,    8,  16);
	}

	private static final int UNCOLLECTIBLE  =                               xy(1, 2);   //16 slots
	public static final int GOLD            = UNCOLLECTIBLE+0;
	public static final int ENERGY          = UNCOLLECTIBLE+1;
	public static final int NOTHING         = UNCOLLECTIBLE+2; 								//for invisible sprites
	public static final int DEWDROP         = UNCOLLECTIBLE+3;
	public static final int PETAL           = UNCOLLECTIBLE+4;
	public static final int SANDBAG         = UNCOLLECTIBLE+5;
	public static final int SPIRIT_ARROW    = UNCOLLECTIBLE+6;
	
	public static final int TENGU_BOMB      = UNCOLLECTIBLE+8;
	public static final int TENGU_SHOCKER   = UNCOLLECTIBLE+9;
	static{
		assignItemRect(GOLD,        13, 14);
		assignItemRect(ENERGY,      16, 16);
		assignItemRect(NOTHING,     16, 16);
		assignItemRect(DEWDROP,     9, 13);
		assignItemRect(PETAL,       7,  8);
		assignItemRect(SANDBAG,     10, 10);
		assignItemRect(SPIRIT_ARROW,10, 10);
		
		assignItemRect(TENGU_BOMB,      10, 10);
		assignItemRect(TENGU_SHOCKER,   10, 10);
	}

	private static final int CONTAINERS     =                               xy(1, 3);   //16 slots
	public static final int BONES           = CONTAINERS+0;
	public static final int REMAINS         = CONTAINERS+1;
	public static final int TOMB            = CONTAINERS+2;
	public static final int GRAVE           = CONTAINERS+3;
	public static final int CHEST           = CONTAINERS+4;
	public static final int LOCKED_CHEST    = CONTAINERS+5;
	public static final int CRYSTAL_CHEST   = CONTAINERS+6;
	public static final int EBONY_CHEST     = CONTAINERS+7;
	static{
		assignItemRect(BONES,           14, 11);
		assignItemRect(REMAINS,         14, 11);
		assignItemRect(TOMB,            12, 15);
		assignItemRect(GRAVE,           12, 15);
		assignItemRect(CHEST,           16, 14);
		assignItemRect(LOCKED_CHEST,    16, 14);
		assignItemRect(CRYSTAL_CHEST,   16, 14);
		assignItemRect(EBONY_CHEST,     16, 14);
	}

	private static final int MISC_CONSUMABLE =                              xy(1, 4);   //16 slots
	public static final int ANKH            = MISC_CONSUMABLE +0;
	public static final int STYLUS          = MISC_CONSUMABLE +1;
	public static final int SEAL            = MISC_CONSUMABLE +2;
	public static final int TORCH           = MISC_CONSUMABLE +3;
	public static final int BEACON          = MISC_CONSUMABLE +4;
	public static final int HONEYPOT        = MISC_CONSUMABLE +5;
	public static final int SHATTPOT        = MISC_CONSUMABLE +6;
	public static final int IRON_KEY        = MISC_CONSUMABLE +7;
	public static final int GOLDEN_KEY      = MISC_CONSUMABLE +8;
	public static final int CRYSTAL_KEY     = MISC_CONSUMABLE +9;
	public static final int SKELETON_KEY    = MISC_CONSUMABLE +10;
	public static final int MASK            = MISC_CONSUMABLE +11;
	public static final int CROWN           = MISC_CONSUMABLE +12;
	public static final int AMULET          = MISC_CONSUMABLE +13;
	public static final int MASTERY         = MISC_CONSUMABLE +14;
	public static final int KIT             = MISC_CONSUMABLE +15;
	static{
		assignItemRect(ANKH,            13, 16);
		assignItemRect(STYLUS,          10, 10);
		
		assignItemRect(SEAL,            13,  13);
		assignItemRect(TORCH,           12, 15);
		assignItemRect(BEACON,          16, 15);
		
		assignItemRect(HONEYPOT,        14, 12);
		assignItemRect(SHATTPOT,        14, 12);
		assignItemRect(IRON_KEY,        11,  14);
		assignItemRect(GOLDEN_KEY,      11,  14);
		assignItemRect(CRYSTAL_KEY,     11,  14);
		assignItemRect(SKELETON_KEY,    11,  14);
		assignItemRect(MASK,            15,  13);
		assignItemRect(CROWN,           14,  14);
		assignItemRect(AMULET,          16, 14);
		assignItemRect(MASTERY,         13, 16);
		assignItemRect(KIT,             16, 15);
	}
	
	private static final int BOMBS          =                               xy(1, 5);   //16 slots
	public static final int BOMB            = BOMBS+0;
	public static final int DBL_BOMB        = BOMBS+1;
	public static final int FIRE_BOMB       = BOMBS+2;
	public static final int FROST_BOMB      = BOMBS+3;
	public static final int REGROWTH_BOMB   = BOMBS+4;
	public static final int FLASHBANG       = BOMBS+5;
	public static final int SHOCK_BOMB      = BOMBS+6;
	public static final int HOLY_BOMB       = BOMBS+7;
	public static final int WOOLY_BOMB      = BOMBS+8;
	public static final int NOISEMAKER      = BOMBS+9;
	public static final int ARCANE_BOMB     = BOMBS+10;
	public static final int SHRAPNEL_BOMB   = BOMBS+11;
	
	static{
		assignItemRect(BOMB,            12, 13);
		assignItemRect(DBL_BOMB,        14, 13);
		assignItemRect(FIRE_BOMB,       10, 16);
		assignItemRect(FROST_BOMB,      10, 16);
		assignItemRect(REGROWTH_BOMB,   10, 16);
		assignItemRect(FLASHBANG,       10, 16);
		assignItemRect(SHOCK_BOMB,      12, 13);
		assignItemRect(HOLY_BOMB,       12, 13);
		assignItemRect(WOOLY_BOMB,      12, 13);
		assignItemRect(NOISEMAKER,      12, 13);
		assignItemRect(ARCANE_BOMB,     12, 13);
		assignItemRect(SHRAPNEL_BOMB,   12, 13);
	}

	private static final int SPECIAL_ITEMS      =                               xy(1, 6);   //16 slots
	public static final int SPEED_LOADER     	= SPECIAL_ITEMS+0;
	public static final int TEMP_SHIP     		= SPECIAL_ITEMS+1;
	public static final int SHIELD	     		= SPECIAL_ITEMS+2;
	public static final int MAGNUS_INFO    		= SPECIAL_ITEMS+3;
	public static final int PRINCESS_MIRROR     = SPECIAL_ITEMS+4;
	public static final int PSIONIC_NODE     	= SPECIAL_ITEMS+5;
	public static final int SATELLITE_CANNON    = SPECIAL_ITEMS+6;
	public static final int LANCE_INFO_1		= SPECIAL_ITEMS+7;
	public static final int LANCE_INFO_2		= SPECIAL_ITEMS+8;
	public static final int BOUNTY				= SPECIAL_ITEMS+9;
	public static final int ARTILIA_INFO_1		= SPECIAL_ITEMS+10;
	public static final int MAGNUS_WING			= SPECIAL_ITEMS+11;

	static{
		assignItemRect(SPEED_LOADER,          	15, 15);
		assignItemRect(TEMP_SHIP,        		16, 16);
		assignItemRect(SHIELD,       			14, 16);
		assignItemRect(MAGNUS_INFO,      		12, 13);
		assignItemRect(PRINCESS_MIRROR,   		12, 14);
		assignItemRect(PSIONIC_NODE,       		10, 16);
		assignItemRect(SATELLITE_CANNON,       	16, 16);
		assignItemRect(LANCE_INFO_1,        	14, 14);
		assignItemRect(LANCE_INFO_2,        	10, 15);
		assignItemRect(BOUNTY,			       	16, 16);
		assignItemRect(ARTILIA_INFO_1,        	10, 14);
		assignItemRect(MAGNUS_WING,	        	14, 13);
	}
	
	                                                                                    //16 free slots

	private static final int WEP_TIER1      =                               xy(1, 7);   //8 slots
	public static final int WORN_SHORTSWORD = WEP_TIER1+0;
	public static final int COMMONBLADE     = WEP_TIER1+1;
	public static final int GLOVES          = WEP_TIER1+2;
	public static final int RUINSPEAR       = WEP_TIER1+3;
	//public static final int RAPIER        = WEP_TIER1+4;
	public static final int DAGGER          = WEP_TIER1+4;
	public static final int MAGES_STAFF     = WEP_TIER1+5;
	public static final int KRYSTALLOS      = WEP_TIER1+6;
	static{
		assignItemRect(WORN_SHORTSWORD, 13, 14);
		assignItemRect(COMMONBLADE, 	14, 14);
		assignItemRect(GLOVES,          12, 15);
		assignItemRect(RUINSPEAR,       16, 16);
		assignItemRect(DAGGER,          15, 15);
		assignItemRect(MAGES_STAFF,     16, 16);
		assignItemRect(KRYSTALLOS,      16, 16);
	}

	private static final int WEP_TIER2      =                               xy(9, 7);   //8 slots
	public static final int SHORTSWORD      = WEP_TIER2+0;
	public static final int HAND_AXE        = WEP_TIER2+1;
	public static final int SPEAR           = WEP_TIER2+2;
	public static final int QUARTERSTAFF    = WEP_TIER2+3;
	public static final int DIRK            = WEP_TIER2+4;
	public static final int LUNGE           = WEP_TIER2+5;
	public static final int SURRATION       = WEP_TIER2+6;
	public static final int RAPIER          = WEP_TIER2+7;
	static{
		assignItemRect(SHORTSWORD,      13, 14);
		assignItemRect(HAND_AXE,        16, 16);
		assignItemRect(SPEAR,           16, 16);
		assignItemRect(QUARTERSTAFF,    16, 16);
		assignItemRect(DIRK,            15, 15);
		assignItemRect(LUNGE,           13, 14);
		assignItemRect(SURRATION,       14, 15);
		assignItemRect(RAPIER,          14, 15);
	}

	private static final int WEP_TIER3      =                               xy(1, 8);   //8 slots
	public static final int SWORD           = WEP_TIER3+0;
	public static final int MACE            = WEP_TIER3+1;
	public static final int SCIMITAR        = WEP_TIER3+2;
	public static final int ROUND_SHIELD    = WEP_TIER3+3;
	public static final int SAI             = WEP_TIER3+4;
	public static final int WHIP            = WEP_TIER3+5;
	public static final int RIPPER          = WEP_TIER3+6;
	public static final int SCRIPTURE       = WEP_TIER3+7;
	static{
		assignItemRect(SWORD,           15, 15);
		assignItemRect(MACE,            15, 15);
		assignItemRect(SCIMITAR,        16, 16);
		assignItemRect(ROUND_SHIELD,    14, 16);
		assignItemRect(SAI,             15, 16);
		assignItemRect(WHIP,            13, 15);
		assignItemRect(RIPPER,          15, 14);
		assignItemRect(SCRIPTURE,       12, 16);
	}

	private static final int WEP_TIER4      =                               xy(9, 8);   //8 slots
	public static final int LONGSWORD       = WEP_TIER4+0;
	public static final int BATTLE_AXE      = WEP_TIER4+1;
	public static final int FLAIL           = WEP_TIER4+2;
	public static final int RUNIC_BLADE     = WEP_TIER4+3;
	public static final int ASSASSINS_BLADE = WEP_TIER4+4;
	public static final int CROSSBOW        = WEP_TIER4+5;
	public static final int CHAINSAW        = WEP_TIER4+6;
	public static final int THUNDERSPEAR    = WEP_TIER4+7;
	static{
		assignItemRect(LONGSWORD,       15, 15);
		assignItemRect(BATTLE_AXE,      15, 15);
		assignItemRect(FLAIL,           16, 16);
		assignItemRect(RUNIC_BLADE,     16, 16);
		assignItemRect(ASSASSINS_BLADE, 14, 15);
		assignItemRect(CROSSBOW,        16, 16);
		assignItemRect(CHAINSAW,        16, 16);
		assignItemRect(THUNDERSPEAR,    15, 15);
	}

	private static final int WEP_TIER5      =                               xy(1, 9);   //16 slots
	public static final int GREATSWORD      = WEP_TIER5+0;
	public static final int WAR_HAMMER      = WEP_TIER5+1;
	public static final int GLAIVE          = WEP_TIER5+2;
	public static final int GREATAXE        = WEP_TIER5+3;
	public static final int GREATSHIELD     = WEP_TIER5+4;
	public static final int GAUNTLETS       = WEP_TIER5+5;
	public static final int EXECUTIONER     = WEP_TIER5+6;
	public static final int MOONLIGHT       = WEP_TIER5+7;
	public static final int FUSION_LANCE    = WEP_TIER5+8;
	public static final int MURAKUMO    	= WEP_TIER5+9;
	public static final int WAR_SCYTHE    	= WEP_TIER5+10;
	static{
		assignItemRect(GREATSWORD,  16, 16);
		assignItemRect(WAR_HAMMER,  16, 16);
		assignItemRect(GLAIVE,      16, 16);
		assignItemRect(GREATAXE,    14, 16);
		assignItemRect(GREATSHIELD, 12, 16);
		assignItemRect(GAUNTLETS,   13, 16);
		assignItemRect(EXECUTIONER, 16, 16);
		assignItemRect(MOONLIGHT,   16, 16);
		assignItemRect(FUSION_LANCE,16, 16);
		assignItemRect(MURAKUMO,	16, 16);
		assignItemRect(WAR_SCYTHE,	14, 15);
	}

	private static final int HANDGUNS     =                                 xy(1, 33);
	public static final int APACHE_PISTOL = HANDGUNS+0;
	public static final int REVOLVER	  = HANDGUNS+1;
	public static final int DRAGOON       = HANDGUNS+2;
	public static final int FLASHBACK     = HANDGUNS+3;
	public static final int HUSH          = HANDGUNS+4;
	public static final int FENCER        = HANDGUNS+5;
	static{
		assignItemRect(APACHE_PISTOL      ,16, 16);
		assignItemRect(REVOLVER		  	  ,13, 15);
		assignItemRect(DRAGOON  		  ,15, 16);
		assignItemRect(FLASHBACK          ,13, 16);
		assignItemRect(HUSH         	  ,13, 16);
		assignItemRect(FENCER    		  ,14, 16);
	}

	private static final int PRECISION        =                               xy(9, 33);
	public static final int TAT 		      = PRECISION+0;
	public static final int ELEGUN            = PRECISION+1;
	public static final int SEEKER		 	  = PRECISION+2;
	public static final int LAURIA 	          = PRECISION+3;
	public static final int ARIA 	 	      = PRECISION+4;
	static{
		assignItemRect(REVOLVER 			  ,16, 16);
		assignItemRect(ELEGUN  				  ,13, 16);
		assignItemRect(SEEKER		 		  ,15, 16);
		assignItemRect(LAURIA 				  ,15, 16);
		assignItemRect(ARIA 	 			  ,16, 16);
	}

	private static final int AUTOMATIC         =                               xy(1, 34);
	public static final int NOT_MACHINE_GUN    = AUTOMATIC+0;
	public static final int SHORT_CARBINE      = AUTOMATIC+1;
	public static final int ARM_RIFLE      	   = AUTOMATIC+2;
	public static final int STANDARD  		   = AUTOMATIC+3;
	public static final int THIN_LINE          = AUTOMATIC+4;
	public static final int TRENCH     		   = AUTOMATIC+5;
	static{
		assignItemRect(NOT_MACHINE_GUN        ,10, 16);
		assignItemRect(SHORT_CARBINE     	  ,13, 15);
		assignItemRect(ARM_RIFLE      		  ,15, 16);
		assignItemRect(STANDARD  			  ,16, 15);
		assignItemRect(THIN_LINE	     	  ,15, 16);
		assignItemRect(TRENCH     			  ,16, 16);
	}

	private static final int SHOTGUN         =                               xy(9, 34);
	public static final int BLUNDERBUST      = SHOTGUN+0;
	public static final int OVERFLOW  		 = SHOTGUN+1;
	public static final int WAVE    		 = SHOTGUN+2;
	public static final int SUPER_SHOTGUN    = SHOTGUN+3;
	public static final int MAX_THUNDER      = SHOTGUN+4;

	static{
		assignItemRect(BLUNDERBUST              ,14, 16);
		assignItemRect(OVERFLOW      			,14, 16);
		assignItemRect(WAVE        				,15, 16);
		assignItemRect(SUPER_SHOTGUN         	,16, 16);
		assignItemRect(MAX_THUNDER        		,16, 16);
	}

	private static final int EXPLOSIVE         =                               xy(1, 35);
	public static final int HARMONICA          = EXPLOSIVE+0;
	public static final int HYDRA    		   = EXPLOSIVE+1;
	public static final int JUSTICE 		   = EXPLOSIVE+2;
	public static final int AAWS_M             = EXPLOSIVE+3;
	public static final int FIRESTORM   	   = EXPLOSIVE+4;
	public static final int BIG_BARREL   	   = EXPLOSIVE+5;
	static{
		assignItemRect(HARMONICA               ,14, 14);
		assignItemRect(HYDRA       			   ,16, 16);
		assignItemRect(JUSTICE    			   ,16, 16);
		assignItemRect(AAWS_M                  ,16, 16);
		assignItemRect(FIRESTORM      		   ,16, 16);
		assignItemRect(BIG_BARREL      		   ,16, 16);
	}

	private static final int LASER              =                               xy(9, 35);
	public static final int VEGA  				= LASER+0;
	public static final int KALEIDOSCOPE    	= LASER+1;
	public static final int KARASAWA   			= LASER+2;
	public static final int SPARK    			= LASER+3;
	public static final int SUPERNOVA    		= LASER+4;
	static{
		assignItemRect(VEGA      				,16, 16);
		assignItemRect(KALEIDOSCOPE            	,16, 16);
		assignItemRect(KARASAWA        		 	,16, 16);
		assignItemRect(SPARK          			,15, 16);
		assignItemRect(SUPERNOVA          		,16, 16);
	}

	private static final int ETC_WEAPONS        =                               xy(1, 36);
	public static final int CLEANSER 			= ETC_WEAPONS+0;
	public static final int MADNESS 			= ETC_WEAPONS+1;
	public static final int FROST 				= ETC_WEAPONS+2;
	public static final int VOLCANO 			= ETC_WEAPONS+3;
	public static final int REITERPALLASCH		= ETC_WEAPONS+4;
	static{
		assignItemRect(CLEANSER 				,16, 16);
		assignItemRect(MADNESS 					,16, 16);
		assignItemRect(FROST 					,16, 16);
		assignItemRect(VOLCANO 					,16, 16);
		assignItemRect(REITERPALLASCH 			,16, 16);
	}

	private static final int PSIONIC_WEAPONS    =                               xy(1, 37);	//16 free slots
	public static final int PSIONIC_BLADE1 		= PSIONIC_WEAPONS+0;
	public static final int PSIONIC_BLADE2 		= PSIONIC_WEAPONS+1;
	public static final int PSIONIC_BLADE3 		= PSIONIC_WEAPONS+2;
	public static final int DEFENDER 			= PSIONIC_WEAPONS+3;
	public static final int WARP_BLADE 			= PSIONIC_WEAPONS+4;
	public static final int GUNGNIR 			= PSIONIC_WEAPONS+5;
	public static final int SICKLE				= PSIONIC_WEAPONS+6;
	public static final int KATANA				= PSIONIC_WEAPONS+7;
	static{
		assignItemRect(PSIONIC_BLADE1 			,14, 14);
		assignItemRect(PSIONIC_BLADE2 			,15, 15);
		assignItemRect(PSIONIC_BLADE3 			,16, 16);
		assignItemRect(DEFENDER					,16, 16);
		assignItemRect(WARP_BLADE				,16, 16);
		assignItemRect(GUNGNIR					,16, 16);
		assignItemRect(SICKLE					,16, 15);
		assignItemRect(KATANA					,16, 16);
	}

	private static final int WEAPONARMS    		=                               xy(1, 38);	//16 free slots
	public static final int VENDETTA	 		= WEAPONARMS+0;
	public static final int STRIDENT	 		= WEAPONARMS+1;
	public static final int UNCONSIOUSNESS	 	= WEAPONARMS+2;
	public static final int HELLBLASTER 		= WEAPONARMS+3;
	public static final int RAIDEN	 			= WEAPONARMS+4;
	public static final int VULKAN	 			= WEAPONARMS+5;
	public static final int STINGER 			= WEAPONARMS+6;
	public static final int THUNDERSTRIKE 		= WEAPONARMS+7;
	public static final int SLASH	 			= WEAPONARMS+8;
	public static final int DRAKEN				= WEAPONARMS+9;
	static{
		assignItemRect(VENDETTA 				,16, 16);
		assignItemRect(STRIDENT 				,16, 16);
		assignItemRect(UNCONSIOUSNESS 			,16, 16);
		assignItemRect(HELLBLASTER	 			,16, 16);
		assignItemRect(RAIDEN					,16, 16);
		assignItemRect(VULKAN					,16, 16);
		assignItemRect(STINGER					,16, 16);
		assignItemRect(THUNDERSTRIKE			,16, 16);
		assignItemRect(SLASH					,16, 16);
		assignItemRect(DRAKEN					,16, 16);
	}

	private static final int MISSILE_WEP    =                               xy(1, 10);  //16 slots. 3 per tier + boomerang
	public static final int SPIRIT_BOW      = MISSILE_WEP+0;
	
	public static final int THROWING_SPIKE  = MISSILE_WEP+1;
	public static final int THROWING_KNIFE  = MISSILE_WEP+2;
	public static final int THROWING_STONE  = MISSILE_WEP+3;
	
	public static final int FISHING_SPEAR   = MISSILE_WEP+4;
	public static final int SHURIKEN        = MISSILE_WEP+5;
	public static final int THROWING_CLUB   = MISSILE_WEP+6;
	
	public static final int THROWING_SPEAR  = MISSILE_WEP+7;
	public static final int BOLAS           = MISSILE_WEP+8;
	public static final int KUNAI           = MISSILE_WEP+9;
	
	public static final int JAVELIN         = MISSILE_WEP+10;
	public static final int TOMAHAWK        = MISSILE_WEP+11;
	public static final int BOOMERANG       = MISSILE_WEP+12;
	
	public static final int TRIDENT         = MISSILE_WEP+13;
	public static final int THROWING_HAMMER = MISSILE_WEP+14;
	public static final int FORCE_CUBE      = MISSILE_WEP+15;
	
	static{
		assignItemRect(SPIRIT_BOW,      15, 15);
		
		assignItemRect(THROWING_SPIKE,  11, 10);
		assignItemRect(THROWING_KNIFE,  12, 13);
		assignItemRect(THROWING_STONE,  12, 13);
		
		assignItemRect(FISHING_SPEAR,   14, 14);
		assignItemRect(SHURIKEN,        14, 14);
		assignItemRect(THROWING_CLUB,   11, 11);
		
		assignItemRect(THROWING_SPEAR,  16, 16);
		assignItemRect(BOLAS,           15, 14);
		assignItemRect(KUNAI,           14, 14);
		
		assignItemRect(JAVELIN,         16, 16);
		assignItemRect(TOMAHAWK,        16, 16);
		assignItemRect(BOOMERANG,       14, 14);
		
		assignItemRect(TRIDENT,         16, 16);
		assignItemRect(THROWING_HAMMER, 16, 16);
		assignItemRect(FORCE_CUBE,      11, 12);
	}

	public static final int DARTS    =                                      xy(1, 11);  //16 slots
	public static final int DART            = DARTS+0;
	public static final int ROT_DART        = DARTS+1;
	public static final int INCENDIARY_DART = DARTS+2;
	public static final int ADRENALINE_DART = DARTS+3;
	public static final int HEALING_DART    = DARTS+4;
	public static final int CHILLING_DART   = DARTS+5;
	public static final int SHOCKING_DART   = DARTS+6;
	public static final int POISON_DART     = DARTS+7;
	public static final int CLEANSING_DART  = DARTS+8;
	public static final int PARALYTIC_DART  = DARTS+9;
	public static final int HOLY_DART       = DARTS+10;
	public static final int DISPLACING_DART = DARTS+11;
	public static final int BLINDING_DART   = DARTS+12;
	static {
		for (int i = DARTS; i < DARTS+16; i++)
			assignItemRect(i, 15, 15);
	}
	
	private static final int ARMOR          =                               xy(1, 12);  //16 slots
	public static final int ARMOR_CLOTH     = ARMOR+0;
	public static final int ARMOR_LEATHER   = ARMOR+1;
	public static final int ARMOR_MAIL      = ARMOR+2;
	public static final int ARMOR_SCALE     = ARMOR+3;
	public static final int ARMOR_PLATE     = ARMOR+4;
	public static final int ARMOR_WARRIOR   = ARMOR+5;
	public static final int ARMOR_MAGE      = ARMOR+6;
	public static final int ARMOR_ROGUE     = ARMOR+7;
	public static final int ARMOR_HUNTRESS  = ARMOR+8;
	public static final int ARMOR_NOISE  	= ARMOR+9;
	public static final int ARMOR_LANCE 	= ARMOR+10;
	public static final int ARMOR_CARROLL 	= ARMOR+11;
	public static final int ARMOR_MAGNUS 	= ARMOR+12;
	public static final int ARMOR_ARTILIA 	= ARMOR+13;
	static{
		assignItemRect(ARMOR_CLOTH,     15, 12);
		assignItemRect(ARMOR_LEATHER,   12, 12);
		assignItemRect(ARMOR_MAIL,      14, 13);
		assignItemRect(ARMOR_SCALE,     14, 13);
		assignItemRect(ARMOR_PLATE,     14, 13);
		assignItemRect(ARMOR_WARRIOR,   14, 14);
		assignItemRect(ARMOR_MAGE,      13, 15);
		assignItemRect(ARMOR_ROGUE,     12, 14);
		assignItemRect(ARMOR_HUNTRESS,  16, 16);
		assignItemRect(ARMOR_NOISE,  	16, 16);
		assignItemRect(ARMOR_LANCE,  	14, 14);
		assignItemRect(ARMOR_CARROLL,  	16, 14);
		assignItemRect(ARMOR_MAGNUS,    14, 14);
		assignItemRect(ARMOR_ARTILIA,  	16, 14);
	}

	                                                                                    //16 free slots

	private static final int WANDS              =                           xy(1, 14);  //16 slots
	public static final int WAND_MAGIC_MISSILE  = WANDS+0;
	public static final int WAND_FIREBOLT       = WANDS+1;
	public static final int WAND_FROST          = WANDS+2;
	public static final int WAND_LIGHTNING      = WANDS+3;
	public static final int WAND_DISINTEGRATION = WANDS+4;
	public static final int WAND_PRISMATIC_LIGHT= WANDS+5;
	public static final int WAND_CORROSION      = WANDS+6;
	public static final int WAND_LIVING_EARTH   = WANDS+7;
	public static final int WAND_BLAST_WAVE     = WANDS+8;
	public static final int WAND_CORRUPTION     = WANDS+9;
	public static final int WAND_WARDING        = WANDS+10;
	public static final int WAND_REGROWTH       = WANDS+11;
	public static final int WAND_TRANSFUSION    = WANDS+12;
	static {
		for (int i = WANDS; i < WANDS+16; i++)
			assignItemRect(i, 14, 14);
	}

	private static final int RINGS          =                               xy(1, 15);  //16 slots
	public static final int RING_GARNET     = RINGS+0;
	public static final int RING_RUBY       = RINGS+1;
	public static final int RING_TOPAZ      = RINGS+2;
	public static final int RING_EMERALD    = RINGS+3;
	public static final int RING_ONYX       = RINGS+4;
	public static final int RING_OPAL       = RINGS+5;
	public static final int RING_TOURMALINE = RINGS+6;
	public static final int RING_SAPPHIRE   = RINGS+7;
	public static final int RING_AMETHYST   = RINGS+8;
	public static final int RING_QUARTZ     = RINGS+9;
	public static final int RING_AGATE      = RINGS+10;
	public static final int RING_DIAMOND    = RINGS+11;
	public static final int RING_IRON 		= RINGS+12;
	static {
		for (int i = RINGS; i < RINGS+16; i++)
			assignItemRect(i, 8, 11);
	}

	private static final int ARTIFACTS          =                            xy(1, 16);  //32 slots
	public static final int ARTIFACT_CLOAK      = ARTIFACTS+0;
	public static final int ARTIFACT_ARMBAND    = ARTIFACTS+1;
	public static final int ARTIFACT_CAPE       = ARTIFACTS+2;
	public static final int ARTIFACT_TALISMAN   = ARTIFACTS+3;
	public static final int ARTIFACT_HOURGLASS  = ARTIFACTS+4;
	public static final int ARTIFACT_TOOLKIT    = ARTIFACTS+5;
	public static final int ARTIFACT_SPELLBOOK  = ARTIFACTS+6;
	public static final int ARTIFACT_BEACON     = ARTIFACTS+7;
	public static final int ARTIFACT_CHAINS     = ARTIFACTS+8;
	public static final int ARTIFACT_HORN1      = ARTIFACTS+9;
	public static final int ARTIFACT_HORN2      = ARTIFACTS+10;
	public static final int ARTIFACT_HORN3      = ARTIFACTS+11;
	public static final int ARTIFACT_HORN4      = ARTIFACTS+12;
	public static final int ARTIFACT_CHALICE1   = ARTIFACTS+13;
	public static final int ARTIFACT_CHALICE2   = ARTIFACTS+14;
	public static final int ARTIFACT_CHALICE3   = ARTIFACTS+15;
	public static final int ARTIFACT_SANDALS    = ARTIFACTS+16;
	public static final int ARTIFACT_SHOES      = ARTIFACTS+17;
	public static final int ARTIFACT_BOOTS      = ARTIFACTS+18;
	public static final int ARTIFACT_GREAVES    = ARTIFACTS+19;
	public static final int ARTIFACT_ROSE1      = ARTIFACTS+20;
	public static final int ARTIFACT_ROSE2      = ARTIFACTS+21;
	public static final int ARTIFACT_ROSE3      = ARTIFACTS+22;
	public static final int ARTIFACT_RADAR		= ARTIFACTS+23;
	public static final int ARTIFACT_PLUS		= ARTIFACTS+24;
	public static final int ARTIFACT_LAMENT		= ARTIFACTS+25;
	static{
		assignItemRect(ARTIFACT_CLOAK,      9,  15);
		assignItemRect(ARTIFACT_ARMBAND,    16, 11);
		assignItemRect(ARTIFACT_CAPE,       16, 14);
		assignItemRect(ARTIFACT_TALISMAN,   15, 13);
		assignItemRect(ARTIFACT_HOURGLASS,  13, 16);
		assignItemRect(ARTIFACT_TOOLKIT,    16, 16);
		assignItemRect(ARTIFACT_SPELLBOOK,  14, 16);
		assignItemRect(ARTIFACT_BEACON,     16, 16);
		assignItemRect(ARTIFACT_CHAINS,     16, 16);
		assignItemRect(ARTIFACT_HORN1,      16, 16);
		assignItemRect(ARTIFACT_HORN2,      16, 16);
		assignItemRect(ARTIFACT_HORN3,      16, 16);
		assignItemRect(ARTIFACT_HORN4,      16, 16);
		assignItemRect(ARTIFACT_CHALICE1,   10, 10);
		assignItemRect(ARTIFACT_CHALICE2,   12, 12);
		assignItemRect(ARTIFACT_CHALICE3,   15, 15);
		assignItemRect(ARTIFACT_SANDALS,    16, 6 );
		assignItemRect(ARTIFACT_SHOES,      16, 6 );
		assignItemRect(ARTIFACT_BOOTS,      16, 9 );
		assignItemRect(ARTIFACT_GREAVES,    16, 14);
		assignItemRect(ARTIFACT_ROSE1,      13, 15);
		assignItemRect(ARTIFACT_ROSE2,      13, 15);
		assignItemRect(ARTIFACT_ROSE3,      13, 15);
		assignItemRect(ARTIFACT_RADAR,      16, 16);
		assignItemRect(ARTIFACT_PLUS,       16, 16);
		assignItemRect(ARTIFACT_LAMENT,     16, 16);
	}

	                                                                                    //16 free slots

	private static final int SCROLLS        =                               xy(1, 19);  //16 slots
	public static final int SCROLL_KAUNAN   = SCROLLS+0;
	public static final int SCROLL_SOWILO   = SCROLLS+1;
	public static final int SCROLL_LAGUZ    = SCROLLS+2;
	public static final int SCROLL_YNGVI    = SCROLLS+3;
	public static final int SCROLL_GYFU     = SCROLLS+4;
	public static final int SCROLL_RAIDO    = SCROLLS+5;
	public static final int SCROLL_ISAZ     = SCROLLS+6;
	public static final int SCROLL_MANNAZ   = SCROLLS+7;
	public static final int SCROLL_NAUDIZ   = SCROLLS+8;
	public static final int SCROLL_BERKANAN = SCROLLS+9;
	public static final int SCROLL_ODAL     = SCROLLS+10;
	public static final int SCROLL_TIWAZ    = SCROLLS+11;
	
	public static final int SCROLL_CATALYST = SCROLLS+13;
	public static final int ARCANE_RESIN    = SCROLLS+14;
	static {
		for (int i = SCROLLS; i < SCROLLS+16; i++)
			assignItemRect(i, 12, 16);
		assignItemRect(SCROLL_CATALYST, 12, 11);
		assignItemRect(ARCANE_RESIN   , 12, 11);
	}
	
	private static final int EXOTIC_SCROLLS =                               xy(1, 20);  //16 slots
	public static final int EXOTIC_KAUNAN   = EXOTIC_SCROLLS+0;
	public static final int EXOTIC_SOWILO   = EXOTIC_SCROLLS+1;
	public static final int EXOTIC_LAGUZ    = EXOTIC_SCROLLS+2;
	public static final int EXOTIC_YNGVI    = EXOTIC_SCROLLS+3;
	public static final int EXOTIC_GYFU     = EXOTIC_SCROLLS+4;
	public static final int EXOTIC_RAIDO    = EXOTIC_SCROLLS+5;
	public static final int EXOTIC_ISAZ     = EXOTIC_SCROLLS+6;
	public static final int EXOTIC_MANNAZ   = EXOTIC_SCROLLS+7;
	public static final int EXOTIC_NAUDIZ   = EXOTIC_SCROLLS+8;
	public static final int EXOTIC_BERKANAN = EXOTIC_SCROLLS+9;
	public static final int EXOTIC_ODAL     = EXOTIC_SCROLLS+10;
	public static final int EXOTIC_TIWAZ    = EXOTIC_SCROLLS+11;
	static {
		for (int i = EXOTIC_SCROLLS; i < EXOTIC_SCROLLS+16; i++)
			assignItemRect(i, 12, 16);
	}
	
	private static final int STONES             =                           xy(1, 21);  //16 slots
	public static final int STONE_AGGRESSION    = STONES+0;
	public static final int STONE_AUGMENTATION  = STONES+1;
	public static final int STONE_FEAR          = STONES+2;
	public static final int STONE_BLAST         = STONES+3;
	public static final int STONE_BLINK         = STONES+4;
	public static final int STONE_CLAIRVOYANCE  = STONES+5;
	public static final int STONE_SLEEP         = STONES+6;
	public static final int STONE_DISARM        = STONES+7;
	public static final int STONE_ENCHANT       = STONES+8;
	public static final int STONE_FLOCK         = STONES+9;
	public static final int STONE_INTUITION     = STONES+10;
	public static final int STONE_SHOCK         = STONES+11;
	static {
		for (int i = STONES; i < STONES+16; i++)
			assignItemRect(i, 16, 16);
	}

	private static final int POTIONS        =                               xy(1, 22);  //16 slots
	public static final int POTION_CRIMSON  = POTIONS+0;
	public static final int POTION_AMBER    = POTIONS+1;
	public static final int POTION_GOLDEN   = POTIONS+2;
	public static final int POTION_JADE     = POTIONS+3;
	public static final int POTION_TURQUOISE= POTIONS+4;
	public static final int POTION_AZURE    = POTIONS+5;
	public static final int POTION_INDIGO   = POTIONS+6;
	public static final int POTION_MAGENTA  = POTIONS+7;
	public static final int POTION_BISTRE   = POTIONS+8;
	public static final int POTION_CHARCOAL = POTIONS+9;
	public static final int POTION_SILVER   = POTIONS+10;
	public static final int POTION_IVORY    = POTIONS+11;
	public static final int POTION_CHLORO   = POTIONS+12;
	public static final int POTION_CATALYST = POTIONS+13;
	public static final int LIQUID_METAL    = POTIONS+14;
	static {
		for (int i = POTIONS; i < POTIONS+16; i++)
			assignItemRect(i, 12, 12);
		assignItemRect(POTION_CATALYST, 6, 15);
		assignItemRect(LIQUID_METAL,    8, 15);
	}
	
	private static final int EXOTIC_POTIONS =                               xy(1, 23);  //16 slots
	public static final int EXOTIC_CRIMSON  = EXOTIC_POTIONS+0;
	public static final int EXOTIC_AMBER    = EXOTIC_POTIONS+1;
	public static final int EXOTIC_GOLDEN   = EXOTIC_POTIONS+2;
	public static final int EXOTIC_JADE     = EXOTIC_POTIONS+3;
	public static final int EXOTIC_TURQUOISE= EXOTIC_POTIONS+4;
	public static final int EXOTIC_AZURE    = EXOTIC_POTIONS+5;
	public static final int EXOTIC_INDIGO   = EXOTIC_POTIONS+6;
	public static final int EXOTIC_MAGENTA  = EXOTIC_POTIONS+7;
	public static final int EXOTIC_BISTRE   = EXOTIC_POTIONS+8;
	public static final int EXOTIC_CHARCOAL = EXOTIC_POTIONS+9;
	public static final int EXOTIC_SILVER   = EXOTIC_POTIONS+10;
	public static final int EXOTIC_IVORY    = EXOTIC_POTIONS+11;
	public static final int EXOTIC_CHLORO   = EXOTIC_POTIONS+11;
	static {
		for (int i = EXOTIC_POTIONS; i < EXOTIC_POTIONS+16; i++)
			assignItemRect(i, 16, 16);
	}

	private static final int SEEDS              =                           xy(1, 24);  //16 slots
	public static final int SEED_ROTBERRY       = SEEDS+0;
	public static final int SEED_FIREBLOOM      = SEEDS+1;
	public static final int SEED_SWIFTTHISTLE   = SEEDS+2;
	public static final int SEED_SUNGRASS       = SEEDS+3;
	public static final int SEED_ICECAP         = SEEDS+4;
	public static final int SEED_STORMVINE      = SEEDS+5;
	public static final int SEED_SORROWMOSS     = SEEDS+6;
	public static final int SEED_MAGEROYAL = SEEDS+7;
	public static final int SEED_EARTHROOT      = SEEDS+8;
	public static final int SEED_STARFLOWER     = SEEDS+9;
	public static final int SEED_FADELEAF       = SEEDS+10;
	public static final int SEED_BLINDWEED      = SEEDS+11;
	static{
		for (int i = SEEDS; i < SEEDS+16; i++)
			assignItemRect(i, 13, 15);
	}
	
	private static final int BREWS          =                               xy(1, 25);  //8 slots
	public static final int BREW_INFERNAL   = BREWS+0;
	public static final int BREW_BLIZZARD   = BREWS+1;
	public static final int BREW_SHOCKING   = BREWS+2;
	public static final int BREW_CAUSTIC    = BREWS+3;
	
	private static final int ELIXIRS        =                               xy(9, 25);  //8 slots
	public static final int ELIXIR_HONEY    = ELIXIRS+0;
	public static final int ELIXIR_AQUA     = ELIXIRS+1;
	public static final int ELIXIR_MIGHT    = ELIXIRS+2;
	public static final int ELIXIR_DRAGON   = ELIXIRS+3;
	public static final int ELIXIR_TOXIC    = ELIXIRS+4;
	public static final int ELIXIR_ICY      = ELIXIRS+5;
	public static final int ELIXIR_ARCANE   = ELIXIRS+6;
	static{
		for (int i = BREWS; i < BREWS+16; i++)
			assignItemRect(i, 12, 16);
	}
	
	                                                                                    //16 free slots
	
	private static final int SPELLS         =                               xy(1, 27);  //16 slots
	public static final int MAGIC_PORTER    = SPELLS+0;
	public static final int PHASE_SHIFT     = SPELLS+1;
	public static final int TELE_GRAB       = SPELLS+2;
	public static final int WILD_ENERGY     = SPELLS+3;
	public static final int RETURN_BEACON   = SPELLS+4;
	public static final int SUMMON_ELE      = SPELLS+5;
	
	public static final int AQUA_BLAST      = SPELLS+7;
	public static final int FEATHER_FALL    = SPELLS+8;
	public static final int RECLAIM_TRAP    = SPELLS+9;
	
	public static final int CURSE_INFUSE    = SPELLS+11;
	public static final int MAGIC_INFUSE    = SPELLS+12;
	public static final int ALCHEMIZE       = SPELLS+13;
	public static final int RECYCLE         = SPELLS+14;
	static{
		assignItemRect(MAGIC_PORTER,    12, 11);
		assignItemRect(PHASE_SHIFT,     12, 11);
		assignItemRect(TELE_GRAB,       12, 11);
		assignItemRect(WILD_ENERGY,      8, 16);
		assignItemRect(RETURN_BEACON,    8, 16);
		assignItemRect(SUMMON_ELE,       8, 16);
		
		assignItemRect(AQUA_BLAST,      11, 11);
		assignItemRect(FEATHER_FALL,    11, 11);
		assignItemRect(RECLAIM_TRAP,    11, 11);
		
		assignItemRect(CURSE_INFUSE,    10, 15);
		assignItemRect(MAGIC_INFUSE,    10, 15);
		assignItemRect(ALCHEMIZE,       10, 15);
		assignItemRect(RECYCLE,         10, 15);
	}
	
	private static final int FOOD       =                                   xy(1, 28);  //16 slots
	public static final int MEAT        = FOOD+0;
	public static final int STEAK       = FOOD+1;
	public static final int STEWED      = FOOD+2;
	public static final int OVERPRICED  = FOOD+3;
	public static final int CARPACCIO   = FOOD+4;
	public static final int RATION      = FOOD+5;
	public static final int PASTY       = FOOD+6;
	public static final int PUMPKIN_PIE = FOOD+7;
	public static final int CANDY_CANE  = FOOD+8;
	public static final int MEAT_PIE    = FOOD+9;
	public static final int BLANDFRUIT  = FOOD+10;
	public static final int BLAND_CHUNKS= FOOD+11;
	public static final int BERRY =       FOOD+12;
	public static final int PHANTOM_MEAT= FOOD+13;
	static{
		assignItemRect(MEAT,        15, 11);
		assignItemRect(STEAK,       15, 11);
		assignItemRect(STEWED,      15, 11);
		assignItemRect(OVERPRICED,  14, 11);
		assignItemRect(CARPACCIO,   15, 11);
		assignItemRect(RATION,      16, 12);
		assignItemRect(PASTY,       16, 16);
		assignItemRect(PUMPKIN_PIE, 16, 12);
		assignItemRect(CANDY_CANE,  13, 16);
		assignItemRect(MEAT_PIE,    16, 13);
		assignItemRect(BLANDFRUIT,  9,  12);
		assignItemRect(BLAND_CHUNKS,14, 6);
		assignItemRect(BERRY,       9,  11);
		assignItemRect(PHANTOM_MEAT,15, 11);
	}

	private static final int QUEST  =                                       xy(1, 29);  //32 slots
	public static final int SKULL   = QUEST+0;
	public static final int DUST    = QUEST+1;
	public static final int CANDLE  = QUEST+2;
	public static final int EMBER   = QUEST+3;
	public static final int PICKAXE = QUEST+4;
	public static final int ORE     = QUEST+5;
	public static final int TOKEN   = QUEST+6;
	public static final int BLOB    = QUEST+7;
	public static final int SHARD   = QUEST+8;
	public static final int CORE    = QUEST+9;
	public static final int CHIP	= QUEST+10;
	static{
		assignItemRect(SKULL,   16, 11);
		assignItemRect(DUST,    10, 13);
		assignItemRect(CANDLE,  12, 15);
		assignItemRect(EMBER,   10, 13);
		assignItemRect(PICKAXE, 16, 16);
		assignItemRect(ORE,     15, 15);
		assignItemRect(TOKEN,   12, 12);
		assignItemRect(BLOB,    10,  9);
		assignItemRect(SHARD,    8, 10);
		assignItemRect(CORE, 	16, 16);
		assignItemRect(CHIP, 	8, 11);
	}

	private static final int BULLETS      	=                                       xy(1, 30);  //16 slots
	public static final int SINGLE_BULLET 	= BULLETS+0;
	public static final int DUAL_BULLET   	= BULLETS+1;
	public static final int TRIPLE_BULLET 	= BULLETS+2;
	public static final int SNIPER_BULLET 	= BULLETS+3;
	public static final int KOJIMA_PARTICLE = BULLETS+4;
	public static final int ENERGY_BULLET_1 = BULLETS+5;
	public static final int ENERGY_BULLET_2 = BULLETS+6;
	public static final int ENERGY_BULLET_3 = BULLETS+7;
	public static final int GRENADE 		= BULLETS+8;
	public static final int ROCKET_1 		= BULLETS+9;
	public static final int ROCKET_2 		= BULLETS+10;
	public static final int ROCKET_3 		= BULLETS+11;
	public static final int SHELL 			= BULLETS+12;
	public static final int ICICLE 			= BULLETS+13;
	static{
		assignItemRect(SINGLE_BULLET,   8,  8 );
		assignItemRect(DUAL_BULLET,     11, 10);
		assignItemRect(TRIPLE_BULLET,   11, 11);
		assignItemRect(SNIPER_BULLET,   8,  8 );
		assignItemRect(KOJIMA_PARTICLE, 9,  9 );
		assignItemRect(ENERGY_BULLET_1, 10, 10);
		assignItemRect(ENERGY_BULLET_2, 10, 10);
		assignItemRect(ENERGY_BULLET_3, 12, 12);
		assignItemRect(GRENADE, 		7,  7 );
		assignItemRect(ROCKET_1, 		11, 11);
		assignItemRect(ROCKET_2, 		11, 11);
		assignItemRect(ROCKET_3, 		11, 11);
		assignItemRect(SHELL, 			14, 14);
		assignItemRect(ICICLE, 			10, 10);
	}

	private static final int BAGS       =                                   xy(1, 31);  //16 slots
	public static final int WATERSKIN   = BAGS+0;
	public static final int BACKPACK    = BAGS+1;
	public static final int POUCH       = BAGS+2;
	public static final int HOLDER      = BAGS+3;
	public static final int BANDOLIER   = BAGS+4;
	public static final int HOLSTER     = BAGS+5;
	public static final int VIAL        = BAGS+6;
	public static final int ICEBOX      = BAGS+7;
	static{
		assignItemRect(WATERSKIN,   15, 14);
		assignItemRect(BACKPACK,    16, 16);
		assignItemRect(POUCH,       14, 15);
		assignItemRect(HOLDER,      16, 16);
		assignItemRect(BANDOLIER,   15, 16);
		assignItemRect(HOLSTER,     16, 16);
		assignItemRect(VIAL,        12, 12);
		assignItemRect(ICEBOX,      15, 15);
	}

	private static final int DOCUMENTS  =                                   xy(1, 32);  //16 slots
	public static final int GUIDE_PAGE  = DOCUMENTS+0;
	public static final int ALCH_PAGE   = DOCUMENTS+1;
	public static final int SEWER_PAGE  = DOCUMENTS+2;
	public static final int PRISON_PAGE = DOCUMENTS+3;
	public static final int CAVES_PAGE  = DOCUMENTS+4;
	public static final int CITY_PAGE   = DOCUMENTS+5;
	public static final int HALLS_PAGE  = DOCUMENTS+6;
	static{
		assignItemRect(GUIDE_PAGE,  10, 11);
		assignItemRect(ALCH_PAGE,   10, 11);
		assignItemRect(SEWER_PAGE,  10, 11);
		assignItemRect(PRISON_PAGE, 10, 11);
		assignItemRect(CAVES_PAGE,  10, 11);
		assignItemRect(CITY_PAGE,   10, 11);
		assignItemRect(HALLS_PAGE,  10, 11);
	}

	//for smaller 8x8 icons that often accompany an item sprite
	public static class Icons {

		private static final int WIDTH = 16;
		public static final int SIZE = 8;

		public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEM_ICONS, SIZE, SIZE );

		private static int xy(int x, int y){
			x -= 1; y -= 1;
			return x + WIDTH*y;
		}

		private static void assignIconRect( int item, int width, int height ){
			int x = (item % WIDTH) * SIZE;
			int y = (item / WIDTH) * SIZE;
			film.add( item, x, y, x+width, y+height);
		}

		private static final int RINGS          =                            xy(1, 1);  //16 slots
		public static final int RING_ACCURACY   = RINGS+0;
		public static final int RING_ARCANA     = RINGS+1;
		public static final int RING_ELEMENTS   = RINGS+2;
		public static final int RING_ENERGY     = RINGS+3;
		public static final int RING_EVASION    = RINGS+4;
		public static final int RING_FORCE      = RINGS+5;
		public static final int RING_FUROR      = RINGS+6;
		public static final int RING_HASTE      = RINGS+7;
		public static final int RING_MIGHT      = RINGS+8;
		public static final int RING_SHARPSHOOT = RINGS+9;
		public static final int RING_TENACITY   = RINGS+10;
		public static final int RING_WEALTH     = RINGS+11;
		public static final int RING_RELOAD     = RINGS+12;
		static {
			assignIconRect( RING_ACCURACY,      7, 7 );
			assignIconRect( RING_ARCANA,        7, 7 );
			assignIconRect( RING_ELEMENTS,      7, 7 );
			assignIconRect( RING_ENERGY,        7, 5 );
			assignIconRect( RING_EVASION,       7, 7 );
			assignIconRect( RING_FORCE,         5, 6 );
			assignIconRect( RING_FUROR,         7, 6 );
			assignIconRect( RING_HASTE,         6, 6 );
			assignIconRect( RING_MIGHT,         7, 7 );
			assignIconRect( RING_SHARPSHOOT,    7, 7 );
			assignIconRect( RING_TENACITY,      6, 6 );
			assignIconRect( RING_WEALTH,        7, 6 );
			assignIconRect( RING_RELOAD,        7, 8 );
		}

		                                                                                //16 free slots

		private static final int SCROLLS        =                            xy(1, 3);  //16 slots
		public static final int SCROLL_UPGRADE  = SCROLLS+0;
		public static final int SCROLL_IDENTIFY = SCROLLS+1;
		public static final int SCROLL_REMCURSE = SCROLLS+2;
		public static final int SCROLL_MIRRORIMG= SCROLLS+3;
		public static final int SCROLL_RECHARGE = SCROLLS+4;
		public static final int SCROLL_TELEPORT = SCROLLS+5;
		public static final int SCROLL_LULLABY  = SCROLLS+6;
		public static final int SCROLL_MAGICMAP = SCROLLS+7;
		public static final int SCROLL_RAGE     = SCROLLS+8;
		public static final int SCROLL_RETRIB   = SCROLLS+9;
		public static final int SCROLL_TERROR   = SCROLLS+10;
		public static final int SCROLL_TRANSMUTE= SCROLLS+11;
		static {
			assignIconRect( SCROLL_UPGRADE,     7, 7 );
			assignIconRect( SCROLL_IDENTIFY,    4, 7 );
			assignIconRect( SCROLL_REMCURSE,    7, 7 );
			assignIconRect( SCROLL_MIRRORIMG,   7, 5 );
			assignIconRect( SCROLL_RECHARGE,    7, 5 );
			assignIconRect( SCROLL_TELEPORT,    7, 7 );
			assignIconRect( SCROLL_LULLABY,     7, 6 );
			assignIconRect( SCROLL_MAGICMAP,    7, 7 );
			assignIconRect( SCROLL_RAGE,        6, 6 );
			assignIconRect( SCROLL_RETRIB,      5, 6 );
			assignIconRect( SCROLL_TERROR,      5, 7 );
			assignIconRect( SCROLL_TRANSMUTE,   7, 7 );
		}

		private static final int EXOTIC_SCROLLS =                            xy(1, 4);  //16 slots
		public static final int SCROLL_ENCHANT  = EXOTIC_SCROLLS+0;
		public static final int SCROLL_DIVINATE = EXOTIC_SCROLLS+1;
		public static final int SCROLL_ANTIMAGIC= EXOTIC_SCROLLS+2;
		public static final int SCROLL_PRISIMG  = EXOTIC_SCROLLS+3;
		public static final int SCROLL_MYSTENRG = EXOTIC_SCROLLS+4;
		public static final int SCROLL_PASSAGE  = EXOTIC_SCROLLS+5;
		public static final int SCROLL_SIREN    = EXOTIC_SCROLLS+6;
		public static final int SCROLL_FORESIGHT= EXOTIC_SCROLLS+7;
		public static final int SCROLL_CHALLENGE= EXOTIC_SCROLLS+8;
		public static final int SCROLL_PSIBLAST = EXOTIC_SCROLLS+9;
		public static final int SCROLL_DREAD    = EXOTIC_SCROLLS+10;
		public static final int SCROLL_METAMORPH= EXOTIC_SCROLLS+11;
		static {
			assignIconRect( SCROLL_ENCHANT,     7, 7 );
			assignIconRect( SCROLL_DIVINATE,    7, 6 );
			assignIconRect( SCROLL_ANTIMAGIC,   7, 7 );
			assignIconRect( SCROLL_PRISIMG,     5, 7 );
			assignIconRect( SCROLL_MYSTENRG,    7, 5 );
			assignIconRect( SCROLL_PASSAGE,     5, 7 );
			assignIconRect( SCROLL_SIREN,       7, 6 );
			assignIconRect( SCROLL_FORESIGHT,   7, 5 );
			assignIconRect( SCROLL_CHALLENGE,   7, 7 );
			assignIconRect( SCROLL_PSIBLAST,    5, 6 );
			assignIconRect( SCROLL_DREAD,       5, 7 );
			assignIconRect( SCROLL_METAMORPH,   7, 7 );
		}

		                                                                                //16 free slots

		private static final int POTIONS        		=                            xy(1, 6);  //16 slots
		public static final int POTION_STRENGTH 		= POTIONS+0;
		public static final int POTION_HEALING  		= POTIONS+1;
		public static final int POTION_MINDVIS  		= POTIONS+2;
		public static final int POTION_FROST    		= POTIONS+3;
		public static final int POTION_LIQFLAME 		= POTIONS+4;
		public static final int POTION_TOXICGAS 		= POTIONS+5;
		public static final int POTION_HASTE    		= POTIONS+6;
		public static final int POTION_INVIS    		= POTIONS+7;
		public static final int POTION_LEVITATE 		= POTIONS+8;
		public static final int POTION_PARAGAS  		= POTIONS+9;
		public static final int POTION_PURITY   		= POTIONS+10;
		public static final int POTION_EXP      		= POTIONS+11;
		public static final int POTION_LESSER_HEALING 	= POTIONS+12;
		static {
			assignIconRect( POTION_STRENGTH,    	7, 7 );
			assignIconRect( POTION_HEALING,     	6, 7 );
			assignIconRect( POTION_MINDVIS,     	7, 5 );
			assignIconRect( POTION_FROST,       	7, 7 );
			assignIconRect( POTION_LIQFLAME,    	5, 7 );
			assignIconRect( POTION_TOXICGAS,    	7, 7 );
			assignIconRect( POTION_HASTE,       	6, 6 );
			assignIconRect( POTION_INVIS,       	5, 7 );
			assignIconRect( POTION_LEVITATE,    	6, 7 );
			assignIconRect( POTION_PARAGAS,     	7, 7 );
			assignIconRect( POTION_PURITY,      	5, 7 );
			assignIconRect( POTION_EXP,         	7, 7 );
			assignIconRect( POTION_LESSER_HEALING,         7, 7 );
		}

		private static final int EXOTIC_POTIONS =                            xy(1, 7);  //16 slots
		public static final int POTION_MASTERY  = EXOTIC_POTIONS+0;
		public static final int POTION_SHIELDING= EXOTIC_POTIONS+1;
		public static final int POTION_MAGISIGHT= EXOTIC_POTIONS+2;
		public static final int POTION_SNAPFREEZ= EXOTIC_POTIONS+3;
		public static final int POTION_DRGBREATH= EXOTIC_POTIONS+4;
		public static final int POTION_CORROGAS = EXOTIC_POTIONS+5;
		public static final int POTION_STAMINA  = EXOTIC_POTIONS+6;
		public static final int POTION_SHROUDFOG= EXOTIC_POTIONS+7;
		public static final int POTION_STRMCLOUD= EXOTIC_POTIONS+8;
		public static final int POTION_EARTHARMR= EXOTIC_POTIONS+9;
		public static final int POTION_CLEANSE  = EXOTIC_POTIONS+10;
		public static final int POTION_DIVINE   = EXOTIC_POTIONS+11;
		static {
			assignIconRect( POTION_MASTERY,     7, 7 );
			assignIconRect( POTION_SHIELDING,   6, 6 );
			assignIconRect( POTION_MAGISIGHT,   7, 5 );
			assignIconRect( POTION_SNAPFREEZ,   7, 7 );
			assignIconRect( POTION_DRGBREATH,   7, 7 );
			assignIconRect( POTION_CORROGAS,    7, 7 );
			assignIconRect( POTION_STAMINA,     6, 6 );
			assignIconRect( POTION_SHROUDFOG,   7, 7 );
			assignIconRect( POTION_STRMCLOUD,   7, 7 );
			assignIconRect( POTION_EARTHARMR,   6, 6 );
			assignIconRect( POTION_CLEANSE,     7, 7 );
			assignIconRect( POTION_DIVINE,      7, 7 );
		}

		                                                                                //16 free slots

	}

}
