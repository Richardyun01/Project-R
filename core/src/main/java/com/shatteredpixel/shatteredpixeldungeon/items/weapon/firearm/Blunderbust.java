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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class Blunderbust extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.BLUNDERBUST;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 2;
        type = FirearmType.FirearmShotgun;
        max_round = 5;
        shot = 5;

        firearm = true;
        firearmShotgun = true;

        bullet_image = ItemSpriteSheet.TRIPLE_BULLET;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (owner instanceof Hero &&
                owner.buff(SlugShot.class) != null &&
                owner.buff(MeleeWeapon.Charger.class) != null &&
                owner.buff(SlugShot.class).onUse &&
                owner.buff(MeleeWeapon.Charger.class).charges >= 1) {
            owner.buff(MeleeWeapon.Charger.class).charges--;
            return 1f;
        } else {
            return Dungeon.level.adjacent(owner.pos, target.pos) ? 1.5f : 0f;
        }
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Blunderbust.shootAbility(hero, this);
    }

    public static void shootAbility(Hero hero, FirearmWeapon wep) {
        wep.beforeAbilityUsed(hero);
        if (hero.buff(SlugShot.class) != null) {
            hero.buff(SlugShot.class).onUse = !hero.buff(SlugShot.class).onUse;
        }
        Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
        hero.sprite.operate(hero.pos);
        wep.afterAbilityUsed(hero);
    }

    public static class SlugShot extends Buff {

        public static float HIT_CHARGE_USE = 3f;

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
            return BuffIndicator.DUEL_SHOTGUN;
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
