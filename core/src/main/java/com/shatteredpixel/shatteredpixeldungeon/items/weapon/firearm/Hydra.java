package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BulletUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Hydra extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.HYDRA;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        type = FirearmType.FirearmExplosive;
        max_round = 2;

        firearm = true;
        firearmExplosive = true;

        bullet_image = ItemSpriteSheet.GRENADE;
        bullet_sound = Assets.Sounds.PUFF;
    }

    @Override
    public void setReloadTime() {
        if (loader != null) {
            reload_time = 4f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 4f * RingOfReload.reloadMultiplier(Dungeon.hero);
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
            return  2 +  //base
                    lvl*2;    //level scaling
        } else {
            return  0 +  //base
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
            return (tier+2) + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier+2) + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (tier*6) + lvl*6 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier*6) + lvl*6 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Harmonica.shootAbility(hero, this);
    }

}
