package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.lance;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class BloodWine extends ArmorAbility {

    {
        baseChargeUse = 45f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
        armor.charge -= chargeUse(hero);
        Mob affected = null;
        int mobCount = 0;
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                mob.beckon( hero.pos );
                Buff.affect( mob, Vertigo.class, Vertigo.DURATION );
                Buff.affect( mob, Blindness.class, Blindness.DURATION );
                mobCount++;
                if (mob.buff(Vertigo.class) != null || mob.buff(Blindness.class) != null){
                    affected = mob;
                }
                if (hero.hasTalent(Talent.TOXIC_WINE)) {
                    GameScene.add( Blob.seed( mob.pos, 1*hero.pointsInTalent(Talent.TOXIC_WINE), CorrosiveGas.class ) );
                }
                if (hero.hasTalent(Talent.ALCOHOLIC_FRENZY)) {
                    Buff.affect(mob, Cripple.class, 10+3*(hero.pointsInTalent(Talent.ALCOHOLIC_FRENZY)-1));
                    if (hero.pointsInTalent(Talent.ALCOHOLIC_FRENZY) >= 3) {
                        Buff.affect(mob, Amok.class, 5*(hero.pointsInTalent(Talent.ALCOHOLIC_FRENZY)-2));
                    }
                }
            }
        }
        if (hero.hasTalent(Talent.HANGOVER)) {
            float healFactor = (float)(Dungeon.hero.HP * mobCount * (0.04 * hero.pointsInTalent(Talent.HANGOVER)));
            healFactor = Math.min( healFactor, hero.HT - hero.HP );
            Dungeon.hero.HP += (int)healFactor;
        }
        armor.updateQuickslot();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.BLOOD_WINE;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.TOXIC_WINE, Talent.ALCOHOLIC_FRENZY, Talent.HANGOVER, Talent.HEROIC_ENERGY};
    }
}
