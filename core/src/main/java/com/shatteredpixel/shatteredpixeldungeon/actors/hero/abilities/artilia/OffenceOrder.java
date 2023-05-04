package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artilia;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CommandBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class OffenceOrder extends ArmorAbility {

    {
        baseChargeUse = 75f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);

        int countMob = 0;

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment == Char.Alignment.ALLY) {
                Buff.affect(mob, CommandBuff.class, CommandBuff.DURATION);
                if (hero.hasTalent(Talent.COMMISSAR)) {
                    Buff.affect(mob, Adrenaline.class, 0.5f*hero.pointsInTalent(Talent.COMMISSAR));
                }
                countMob++;
            }
        }

        if (hero.hasTalent(Talent.SITUATION_REPORT)) {
            Buff.affect(hero, MindVision.class, countMob * (0.5f*hero.pointsInTalent(Talent.SITUATION_REPORT)));
        }

        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.COMMAND_ORDER;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.COMMISSAR, Talent.SIMULTANEOUS_ATTACK, Talent.SITUATION_REPORT, Talent.HEROIC_ENERGY};
    }
}
