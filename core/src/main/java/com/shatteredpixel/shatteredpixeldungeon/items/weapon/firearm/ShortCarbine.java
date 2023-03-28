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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class ShortCarbine extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.SHORT_CARBINE;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 2;
        type = FirearmType.FirearmAuto;
        max_round = 8;// + 2 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE);;
        shot = 2;

        firearm = true;
        firearmAuto = true;

        bullet_image = ItemSpriteSheet.DUAL_BULLET;
    }

    @Override
    public void setMaxRound() {
        max_round = 8 + 2 * Dungeon.hero.pointsInTalent(Talent.DEATH_MACHINE) + 2 * Dungeon.hero.pointsInTalent(Talent.QUANTITY_OVER_QUALITY);
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (Dungeon.level.distance(owner.pos, target.pos) <= 2) {
            return 1.25f;
        } else if (Dungeon.level.distance(owner.pos, target.pos) > 2 && Dungeon.level.distance(owner.pos, target.pos) <= 7) {
            return 1f;
        } else {
            return 0.8f;
        }
    }

    @Override
    protected void carrollAbility( Hero hero, Integer target ) {
        ShortCarbine.shootAbility(hero, this);
    }

    public static void shootAbility(Hero hero, FirearmWeapon wep) {
        wep.beforeAbilityUsed(hero);
        if (hero.buff(InfiniteShot.class) != null) {
            hero.buff(InfiniteShot.class).onUse = !hero.buff(InfiniteShot.class).onUse;
        }
        Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
        hero.sprite.operate(hero.pos);
        wep.afterAbilityUsed(hero);
    }

    public static class InfiniteShot extends Buff {

        public static float HIT_CHARGE_USE = 2f;

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
            return BuffIndicator.DUEL_AUTO;
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