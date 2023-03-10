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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Supercharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.text.DecimalFormat;

public class Supernova extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.SUPERNOVA;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 6;
        type = FirearmType.FirearmEnergy1;
        max_round = 1;

        bullet_image = ItemSpriteSheet.ENERGY_BULLET_3;
        bullet_sound = Assets.Sounds.PUFF;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (hero.heroClass == HeroClass.NOISE) {
            return 2f;
        } else {
            return 1f;
        }
    }

    private int Damagefactor(int lvl) {
        return (75 + 25 * lvl) / 5;
    }

    @Override
    public void cooldown() {
        Supercharge buff = Dungeon.hero.buff(Supercharge.class);
        if (hero.buff(Supercharge.class) != null) buff.turnCount = 0;
    }

    @Override
    public int Bulletmin(int lvl) {
        Supercharge buff = Dungeon.hero.buff(Supercharge.class);
        if (hero.buff(Supercharge.class) == null) {
            return RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        } else {
            return Damagefactor(lvl) * buff.turnCount +
                   RingOfSharpshooting.levelDamageBonus(Dungeon.hero); //+ (superCharge)*25;
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        /**
        Supercharge buff = Dungeon.hero.buff(Supercharge.class);
        if (hero.buff(Supercharge.class) == null) {
            return 0 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        } else {
            return 0 + Damagefactor(lvl) * buff.turnCount +
                    RingOfSharpshooting.levelDamageBonus(Dungeon.hero); //+ (superCharge)*25;
        } **/
        return Bulletmin(lvl);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        Supercharge buff = Dungeon.hero.buff(Supercharge.class);
        if (buff != null) {
            damage += buff.getDamageFactor() * buff.turnCount;
        }

        if (attacker instanceof Hero && round == 0 && buff != null) {
            buff.detach();
            buff.turnCount = 0;
        }
        return super.proc( attacker, defender, damage );
    }

    @Override
    public String info() {

        setReloadTime();
        setMaxRound();
        String info = desc();
        int loadtime = (int)(100 * (1 - SpeedLoader.reloadMultiplier()));

        int lvl = level();

        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.STR() > STRReq()){
                info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            }
            info += "\n\n" + Messages.get(FirearmWeapon.class, "stats_known",
                    0,
                    75+25*lvl,
                    round, max_round,
                    new DecimalFormat("#.##").format(reload_time));
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
            info += "\n\n" + Messages.get(FirearmWeapon.class, "stats_unknown",
                    0,
                    75,
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
        } else if (loader != null) {
            info += "\n\n" + Messages.get(FirearmWeapon.class, "loader_attached", loadtime);
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        return info;
    }

    public String statsInfo(){
        return Messages.get(this, "stats_desc");
    }

}
