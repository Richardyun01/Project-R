package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BulletUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FireStorm extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.FIRESTORM;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        type = FirearmType.FirearmExplosive;
        max_round = 4;

        firearm = true;
        firearmExplosive = true;

        bullet_image = ItemSpriteSheet.ROCKET_3;
        bullet_sound = Assets.Sounds.PUFF;
    }

    @Override
    public void setReloadTime() {
        if (loader != null) {
            reload_time = 6f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 6f * RingOfReload.reloadMultiplier(Dungeon.hero);
        }
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (hero.heroClass == HeroClass.NOISE) {
            return 1.5f;
        } else if (owner instanceof Hero &&
                owner.buff(Harmonica.GuidedShot.class) != null &&
                owner.buff(MeleeWeapon.Charger.class) != null &&
                owner.buff(Harmonica.GuidedShot.class).onUse &&
                owner.buff(MeleeWeapon.Charger.class).charges >= 1) {
            return INFINITE_ACCURACY;
        } else {
            return 1f;
        }
    }

    @Override
    public int min(int lvl) {
        if (hero.heroClass == HeroClass.NOISE || hero.subClass == HeroSubClass.TERCIO) {
            return  2 +     //base
                    lvl*2;    //level scaling
        } else {
            return  0 +     //base
                    lvl*2;    //level scaling
        }
    }

    @Override
    public int max(int lvl) {
        if (hero.heroClass == HeroClass.NOISE || hero.subClass == HeroSubClass.TERCIO) {
            return  12 +    //base
                    lvl*3;   //level scaling
        } else {
            return  10 +    //base
                    lvl*3;   //level scaling
        }
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (tier*2) + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier*2) + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (tier*3) + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier*3) + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    public void onThrowBulletFirearmExplosive( int cell ) {
        ArrayList<Char> affected = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS9) {
            int c = cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 4);
                }
                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                }
                Char ch = Actor.findChar(c);
                if (ch != null) {
                    affected.add(ch);
                    if (hero.pointsInTalent(Talent.MALICIOUS_FUEL) == 3) {
                        Buff.affect(ch, Ooze.class).set( Ooze.DURATION );
                    }
                }
            }
            GameScene.add(Blob.seed(cell + n, 2, Fire.class));
            if (hero.hasTalent(Talent.MALICIOUS_FUEL)) {
                if (hero.pointsInTalent(Talent.MALICIOUS_FUEL) == 1) {
                    GameScene.add( Blob.seed( cell, 25, ToxicGas.class ) );
                } else if (hero.pointsInTalent(Talent.MALICIOUS_FUEL) == 2) {
                    GameScene.add( Blob.seed( cell, 10, CorrosiveGas.class ) );
                }
            }
            if (hero.hasTalent(Talent.GRID_EXPOSURE)) {
                if (Random.Int(10) < 1+hero.pointsInTalent(Talent.GRID_EXPOSURE)) {
                    GameScene.add( Blob.seed( cell, 3*hero.pointsInTalent(Talent.GRID_EXPOSURE), Electricity.class ) );
                }
            }

        }
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Harmonica.shootAbility(hero, this);
    }
}
