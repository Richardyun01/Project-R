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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class Madness extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.MADNESS;
        hitSound = Assets.Sounds.BLAST;
        hitSoundPitch = 0.8f;

        tier = 6;
        type = FirearmType.FirearmEtc1;
        max_round = 1;

        firearm = true;
        firearmEtc = true;

        bullet_image = ItemSpriteSheet.KOJIMA_PARTICLE;
        bullet_sound = Assets.Sounds.BLAST;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (hero.heroClass == HeroClass.NOISE) {
            return 2f;
        } else {
            return 1.5f;
        }
    }

    @Override
    public void setReloadTime() {
        if (loader != null) {
            reload_time = 50f * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 50f;
        }
    }

    @Override
    public int Bulletmin(int lvl) {
        return 200 + (lvl*25);
    }

    @Override
    public int Bulletmax(int lvl) {
        return 200 + (lvl*25);
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Madness.shootAbility(hero, this);
    }

    public static void shootAbility(Hero hero, FirearmWeapon wep) {
        wep.beforeAbilityUsed(hero);
        if (hero.buff(OverCharge.class) != null) {
            hero.buff(OverCharge.class).onUse = !hero.buff(OverCharge.class).onUse;
        }
        Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
        hero.sprite.operate(hero.pos);
        wep.afterAbilityUsed(hero);
    }

    public static class OverCharge extends Buff {

        public static float HIT_CHARGE_USE = 1f;

        public boolean onUse = true;

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        public int hitsLeft(){
            MeleeWeapon.Charger charger = Buff.affect(target, MeleeWeapon.Charger.class);
            float charges = charger.charges;
            charges += charger.partialCharge;

            return (int)(charges/HIT_CHARGE_USE);
        }

        public int maxHit(){
            MeleeWeapon.Charger charger = Buff.affect(target, MeleeWeapon.Charger.class);
            float maxCharges = charger.chargeCap();

            return (int)(maxCharges);
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_ETC;
        }

        @Override
        public void tintIcon(Image icon) {
            if (hitsLeft() == 0 || !onUse){
                icon.brightness(0.25f);
            } else {
                icon.resetColor();
            }
        }

        @Override
        public float iconFadePercent() {
            float usableCharges = hitsLeft()*HIT_CHARGE_USE;

            return 1f - (usableCharges /  Buff.affect(target, MeleeWeapon.Charger.class).chargeCap());
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(hitsLeft());
        }

        @Override
        public String desc() {
            if (!onUse) {
                return Messages.get(this, "no_use", hitsLeft(), maxHit());
            } else {
                return Messages.get(this, "desc", hitsLeft(), maxHit());
            }
        }

    }

}