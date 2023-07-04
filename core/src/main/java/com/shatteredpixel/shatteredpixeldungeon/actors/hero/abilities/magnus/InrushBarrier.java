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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.magnus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class InrushBarrier extends ArmorAbility {

    {
        baseChargeUse = 40f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {

        int barrierAmount = 30;

        if (hero.hasTalent(Talent.SHIELD_AMPLIFICATION)) {
            barrierAmount += 4*hero.pointsInTalent(Talent.SHIELD_AMPLIFICATION);
        }
        Buff.affect(hero, Barrier.class).setShield(barrierAmount);
        if (hero.hasTalent(Talent.TORCH_LIGHT)) {
            Buff.affect(hero, Light.class, 30*hero.pointsInTalent(Talent.TORCH_LIGHT));
        }
        if (hero.hasTalent(Talent.RESOLVE)) {
            Buff.affect(hero, Bless.class, 5*hero.pointsInTalent(Talent.RESOLVE));
        }

        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        Emitter e = hero.sprite.centerEmitter();
        if (e != null) e.burst( EnergyParticle.FACTORY, 15 );

        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.INRUSHBARRIER;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.SHIELD_AMPLIFICATION, Talent.TORCH_LIGHT, Talent.RESOLVE, Talent.HEROIC_ENERGY};
    }
}
