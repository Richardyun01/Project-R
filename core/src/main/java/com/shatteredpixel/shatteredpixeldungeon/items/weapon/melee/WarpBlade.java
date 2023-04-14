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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Disintegrating;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WarpBlade extends MeleeWeapon {

    {
        image = ItemSpriteSheet.WARP_BLADE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        ACC = 1.5f;

        tier = 6;
    }

    @Override
    public int max(int lvl) {
        return  5*(tier) +    //base
                lvl*(tier-1);   //level scaling
    }

    public int proc(Char attacker, Char defender, int damage ) {

        Buff.affect( defender, Disintegrating.class ).set(max()/10);
        Splash.at( defender.sprite.center(), 0xFFB2D6FF, 5);

        return super.proc( attacker, defender, damage );
    }

    @Override
    public float abilityChargeUse(Hero hero) {
        return 2*super.abilityChargeUse(hero);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        warpCutAbility(hero, target, this);
    }

    public static void warpCutAbility(Hero hero, Integer target, MeleeWeapon wep){
        if (target == null) {
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(wep, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = wep;
        if (!hero.canAttack(enemy)){
            GLog.w(Messages.get(wep, "ability_bad_position"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                wep.beforeAbilityUsed(hero);
                AttackIndicator.target(enemy);
                if (hero.attack(enemy, 1.3f, 0, Char.INFINITE_ACCURACY)) {
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                    Buff.affect(enemy, Disintegrating.class).set(enemy.HP*0.25f);
                }
                Invisibility.dispel();
                hero.next();
                wep.afterAbilityUsed(hero);
            }
        });
    }

}
