package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artilia;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.MistScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.HashMap;

public class FrostWind extends ArmorAbility {

    private static final HashMap<Class<?extends Wand>, Integer> effectTypes = new HashMap<>();
    static {
        effectTypes.put(WandOfFrost.class, MagicMissile.FROST_CONE);
    }

    {
        baseChargeUse = 30f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        Ballistica aim;
        //Basically the direction of the aim only matters if it goes outside the map
        //So we just ensure it won't do that.
        if (hero.pos % Dungeon.level.width() > 10){
            aim = new Ballistica(hero.pos, hero.pos - 1, Ballistica.WONT_STOP);
        } else {
            aim = new Ballistica(hero.pos, hero.pos + 1, Ballistica.WONT_STOP);
        }

        Class<? extends Wand> wandCls = WandOfFrost.class;

        int aoeSize = 15;
        int projectileProps = Ballistica.STOP_SOLID | Ballistica.STOP_TARGET;

        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);

        for (Ballistica ray : aoe.outerRays){
            ((MagicMissile)hero.sprite.parent.recycle( MagicMissile.class )).reset(
                    effectTypes.get(wandCls),
                    hero.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        final float effectMulti = 1f;

        Class<? extends Wand> finalWandCls = wandCls;
        ((MagicMissile)hero.sprite.parent.recycle( MagicMissile.class )).reset(
            effectTypes.get(wandCls),
            hero.sprite,
            aim.path.get(aoeSize / 2),
            new Callback() {
                @Override
                public void call() {
                    Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
                    int mobCount = 0;
                    if (hero.hasTalent(Talent.CRYSTAL_TORMENT)){
                        Buff.affect(hero, Talent.FrostWindTracker.class, 0f);
                    }
                    for (int cell : aoe.cells) {
                        if (fire != null) {
                            fire.clear(cell);
                        }

                        Char mob = Actor.findChar(cell);
                        if (mob != null && mob != hero){
                            if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
                                Buff.affect( mob, Frost.class, effectMulti*Frost.DURATION );
                                mobCount++;
                            }
                        }

                        //### Deal damage ###
                        int damage = Math.round(Random.NormalIntRange(10+5*hero.pointsInTalent(Talent.GLACIAL_STORM), 20+5*hero.pointsInTalent(Talent.GLACIAL_STORM)));
                        if (mob != null && damage > 0 && mob.alignment != Char.Alignment.ALLY && hero.hasTalent(Talent.GLACIAL_STORM)){
                            mob.damage(damage, Reflection.newInstance(finalWandCls));
                        }
                    }
                    Buff.affect(hero, FrostImbue.class, 5*mobCount);
                    if ((hero.buff(Burning.class)) != null) {
                        hero.buff(Burning.class).detach();
                    }
                    if (hero.hasTalent(Talent.FREEZING_MIST)) {
                        int centerVolume = 40*hero.pointsInTalent(Talent.FREEZING_MIST);
                        int cell = hero.pos;
                        for (int i : PathFinder.NEIGHBOURS8){
                            if (!Dungeon.level.solid[cell+i]){
                                GameScene.add( Blob.seed( cell+i, 40*hero.pointsInTalent(Talent.FREEZING_MIST), MistScreen.class ) );
                            } else {
                                centerVolume += 40;
                            }
                        }

                        GameScene.add( Blob.seed( cell, centerVolume, MistScreen.class ) );
                    }

                    hero.spendAndNext(Actor.TICK);
                }
            }
        );

        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();

        Sample.INSTANCE.play( Assets.Sounds.GAS );
    }

    @Override
    public int icon() {
        return HeroIcon.FROST_WIND;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.FREEZING_MIST, Talent.GLACIAL_STORM, Talent.CRYSTAL_TORMENT, Talent.HEROIC_ENERGY};
    }
}
