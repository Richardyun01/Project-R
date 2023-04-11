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

public class BigBarrel extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.BIG_BARREL;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 6;
        type = FirearmType.FirearmExplosive;
        max_round = 1;

        firearm = true;
        firearmExplosive = true;

        bullet_image = ItemSpriteSheet.SHELL;
    }

    @Override
    public void setReloadTime() {
        if (loader != null) {
            reload_time = 5f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 5f * RingOfReload.reloadMultiplier(Dungeon.hero);
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
    public int STRReq(int lvl) {
        return STRReq(tier+1, lvl); //22 base strength req, up from 20
    }

    @Override
    public int min(int lvl) {
        if (hero.heroClass == HeroClass.NOISE || hero.subClass == HeroSubClass.TERCIO) {
            return  17 +  //base
                    lvl*3;    //level scaling
        } else {
            return  15 +  //base
                    lvl*3;    //level scaling
        }
    }

    @Override
    public int max(int lvl) {
        if (hero.heroClass == HeroClass.NOISE || hero.subClass == HeroSubClass.TERCIO) {
            return  32 +    //base
                    lvl*4;   //level scaling
        } else {
            return  30 +    //base
                    lvl*4;   //level scaling
        }
    }

    @Override
    public int Bulletmin(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (tier+4)*3 + lvl*6 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier+4)*3 + lvl*6 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (tier+4)*10 + lvl*10 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier+4)*10 + lvl*10 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    public void onThrowBulletFirearmExplosive( int cell ) {
        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 5);
        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 5);

        for (int n : PathFinder.NEIGHBOURS25) {
            int c = cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 6);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 6);
                }
                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                }
                Char ch = Actor.findChar(c);
                if (ch != null) {
                    int damage = Random.NormalIntRange(Bulletmin(buffedLvl()),
                            Bulletmax(buffedLvl()));
                    if (hero.pointsInTalent(Talent.MALICIOUS_FUEL) == 3) {
                        Buff.affect(ch, Ooze.class).set( Ooze.DURATION );
                    }
                    ch.damage(damage, this);
                }
                if (hero.hasTalent(Talent.MALICIOUS_FUEL)) {
                    if (hero.pointsInTalent(Talent.MALICIOUS_FUEL) == 1) {
                        GameScene.add( Blob.seed( cell, 5, ToxicGas.class ) );
                    } else if (hero.pointsInTalent(Talent.MALICIOUS_FUEL) == 2) {
                        GameScene.add( Blob.seed( cell, 5, CorrosiveGas.class ) );
                    }
                }
            }
            if (hero.hasTalent(Talent.GRID_EXPOSURE)) {
                if (Random.Int(10) < 1+hero.pointsInTalent(Talent.GRID_EXPOSURE)) {
                    GameScene.add( Blob.seed( cell, 3*hero.pointsInTalent(Talent.GRID_EXPOSURE), Electricity.class ) );
                }
            }
        }

        Sample.INSTANCE.play( Assets.Sounds.BLAST );
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Harmonica.shootAbility(hero, this);
    }
}
