package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artilia;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DeepStrike extends ArmorAbility {

    {
        baseChargeUse = 70f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        hero.sprite.operate(hero.pos);
        hero.sprite.centerEmitter().start( Speck.factory( Speck.ORDER ), 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment == Char.Alignment.ALLY) {
                new Fadeleaf().activate(mob);
                mob.HP += mob.HT - mob.HP;
                PotionOfHealing.cure( mob );
                Buff.affect(mob, Vertigo.class, 1.5f);
                if (hero.hasTalent(Talent.DROP_IMPACT)) {
                    for (int i : PathFinder.NEIGHBOURS8) {
                        Char enemy = Actor.findChar(hero.pos + i);
                        if (enemy != null && enemy != hero && enemy.alignment != Char.Alignment.ALLY) {
                            int damage = Random.NormalIntRange(2*hero.pointsInTalent(Talent.DROP_IMPACT), 8 * hero.pointsInTalent(Talent.DROP_IMPACT));
                            enemy.damage(damage, hero);
                        }
                    }
                }
                if (hero.hasTalent(Talent.BATTLE_FRENZY)) {
                    Buff.affect(mob, Adrenaline.class, 1f*hero.pointsInTalent(Talent.BATTLE_FRENZY));
                }
                if (hero.hasTalent(Talent.PERSONNEL_PROTECTION)) {
                    int barrAmt = 1;
                    barrAmt = Math.min( barrAmt, mob.HT - mob.HP );
                    Buff.affect(hero, Barrier.class).setShield((int) (5 * hero.pointsInTalent(Talent.PERSONNEL_PROTECTION)));
                    mob.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( barrAmt ) );
                }
            }
        }

        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.DEEP_STRIKE;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.DROP_IMPACT, Talent.BATTLE_FRENZY, Talent.PERSONNEL_PROTECTION, Talent.HEROIC_ENERGY};
    }
}
