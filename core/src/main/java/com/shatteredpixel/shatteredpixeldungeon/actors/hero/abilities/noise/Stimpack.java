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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noise;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Stimpack extends ArmorAbility {

    {
        baseChargeUse = 50f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        int damage = Math.round(hero.HT*(0.4f - 0.1f*(hero.pointsInTalent(Talent.SUPER_STIMPACK))));
        if (hero.HP <= damage) {
            GLog.w(Messages.get(this, "cannot_use"));
        } else {
            hero.damage(damage, hero);
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.DRINK);
            int duration = 20;
            if (hero.hasTalent(Talent.DRUG_COMPOUND)) {
                duration += 5*hero.pointsInTalent(Talent.DRUG_COMPOUND);
            }
            Buff.prolong(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StimpackAdrenaline.class, duration);
            armor.charge -= chargeUse(hero);
            armor.updateQuickslot();
            hero.spendAndNext(Actor.TICK);
        }
    }

    @Override
    public int icon() {
        return HeroIcon.STIMPACK;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.SUPER_STIMPACK, Talent.DRUG_COMPOUND, Talent.PSYCHOTROPIC_CONTROL, Talent.HEROIC_ENERGY};
    }
}

