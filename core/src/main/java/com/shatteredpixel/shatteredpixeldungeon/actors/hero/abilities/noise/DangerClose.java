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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noise;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DangerClose extends ArmorAbility {

    {
        baseChargeUse = 65f;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        if (Actor.findChar(target) == hero){
            GLog.w(Messages.get(this, "self_target"));
            return;
        }

        CellEmitter.get(target).burst(SmokeParticle.FACTORY, 5);
        CellEmitter.center(target).burst(BlastParticle.FACTORY, 5);

        int[] neigh;
        switch (hero.pointsInTalent(Talent.HEAT_ROUND)) {
            case 1:
            case 2:
                neigh = PathFinder.NEIGHBOURS9;
                break;
            case 3:
            case 4:
                neigh = PathFinder.NEIGHBOURS5;
                break;
            default:
                neigh = PathFinder.NEIGHBOURS25;
                break;
        }

        for (int n : neigh) {
            int c = target + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 6);
                    CellEmitter.center(target).burst(BlastParticle.FACTORY, 6);
                }
                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                }
                Char ch = Actor.findChar(c);
                if (ch != null) {
                    int damage = Random.NormalIntRange(hero.belongings.weapon.min()+((hero.belongings.weapon.min()/4) * hero.pointsInTalent(Talent.HEAT_ROUND)),
                                                    hero.belongings.weapon.max()+((hero.belongings.weapon.max()/4) * hero.pointsInTalent(Talent.HEAT_ROUND)));
                    ch.damage(damage, this);
                    if (hero.hasTalent(Talent.EMP_SHELL)) {
                        if (hero.pointsInTalent(Talent.EMP_SHELL) >= 1) {
                            Buff.affect(ch, Blindness.class, 1*hero.pointsInTalent(Talent.EMP_SHELL));
                            if (hero.pointsInTalent(Talent.EMP_SHELL) >= 3) {
                                Buff.affect(ch, Terror.class, hero.pointsInTalent(Talent.EMP_SHELL)-2);
                            }
                        }
                    }
                    if (hero.hasTalent(Talent.EXPLOSIVE_PENETRATOR) && Random.Int(20) <= 3*hero.pointsInTalent(Talent.EXPLOSIVE_PENETRATOR)) {
                        Buff.affect(ch, Vulnerable.class, 1*hero.pointsInTalent(Talent.EXPLOSIVE_PENETRATOR));
                    }
                }
                if (ch == hero) {
                    if (!ch.isAlive()) {
                        Dungeon.fail(getClass());
                        GLog.n(Messages.get(FirearmWeapon.class, "ondeath"));
                    }
                }
            }
        }

        Sample.INSTANCE.play( Assets.Sounds.BLAST );

        armor.charge -= chargeUse(hero);
        Item.updateQuickslot();
    }

    @Override
    public int icon() {
        return HeroIcon.DANGER_CLOSE;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.HEAT_ROUND, Talent.EXPLOSIVE_PENETRATOR, Talent.EMP_SHELL, Talent.HEROIC_ENERGY};
    }
}
