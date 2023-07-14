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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ImmobileTrap extends ArmorAbility {

    {
        baseChargeUse = 50f;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    public void activate( ClassArmor armor, Hero hero, Integer target ) {
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);

        if (ch == null || ch == hero){
            GLog.w(Messages.get(this, "no_target"));
            return;
        }

        if (ch != null && ch != hero){
            Buff.prolong(ch, Roots.class, 15f);
            if (hero.hasTalent(Talent.SPIKE_TRAP)) {
                int damage = Random.NormalIntRange(hero.pointsInTalent(Talent.SPIKE_TRAP), 4*hero.pointsInTalent(Talent.SPIKE_TRAP));
                damage += Math.round(hero.damageRoll()*0.15f*hero.pointsInTalent(Talent.SPIKE_TRAP));
                ch.damage(damage, hero);
            }
            if (hero.hasTalent(Talent.AREA_DENIAL)) {
                for (int i : PathFinder.NEIGHBOURS25) {
                    Char mob = Actor.findChar(target);
                    if (mob != null && mob != hero && mob.alignment != Char.Alignment.ALLY) {
                        Buff.affect(mob, Blindness.class, 2*hero.pointsInTalent(Talent.AREA_DENIAL));
                    }
                }
            }
            if (hero.hasTalent(Talent.PURSUIT_DETERRANCE)) {
                GameScene.add( Blob.seed(ch.pos, 50*hero.pointsInTalent(Talent.PURSUIT_DETERRANCE), SmokeScreen.class));
            }
            CellEmitter.bottom( ch.pos ).start( EarthParticle.FACTORY, 0.05f, 8 );
            hero.sprite.zap(target);
            armor.charge -= chargeUse( hero );
            Sample.INSTANCE.play( Assets.Sounds.PLANT );
        }

        hero.busy();
        armor.updateQuickslot();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.IMMOBILETRAP;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.SPIKE_TRAP, Talent.AREA_DENIAL, Talent.PURSUIT_DETERRANCE, Talent.HEROIC_ENERGY};
    }
}
