package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BulletUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class Harmonica extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.HARMONICA;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        type = FirearmType.FirearmExplosive;
        max_round = 1;

        firearm = true;
        firearmExplosive = true;

        bullet_image = ItemSpriteSheet.GRENADE;
        bullet_sound = Assets.Sounds.PUFF;
    }

    @Override
    public void setReloadTime() {
        if (loader != null) {
            reload_time = 2f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 2f * RingOfReload.reloadMultiplier(Dungeon.hero);
        }
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (hero.heroClass == HeroClass.NOISE) {
            return 1.5f;
        } else if (owner instanceof Hero &&
                owner.buff(GuidedShot.class) != null &&
                owner.buff(MeleeWeapon.Charger.class) != null &&
                owner.buff(GuidedShot.class).onUse &&
                owner.buff(MeleeWeapon.Charger.class).charges >= 1) {
            owner.buff(MeleeWeapon.Charger.class).charges--;
            return INFINITE_ACCURACY;
        } else {
            return 1f;
        }
    }

    @Override
    public int min(int lvl) {
        if (hero.heroClass == HeroClass.NOISE) {
            return  2 +  //base
                    lvl*2;    //level scaling
        } else {
            return  0 +  //base
                    lvl*2;    //level scaling
        }
    }

    @Override
    public int max(int lvl) {
        if (hero.heroClass == HeroClass.NOISE) {
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
            return (tier+3) + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier+3) + lvl*4 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    public int Bulletmax(int lvl) {
        if (Dungeon.hero.buff(BulletUp.class) != null) {
            return (tier+2)*5 + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) + 3 * hero.pointsInTalent(Talent.ONE_MORE_BITE);
        } else {
            return (tier+2)*5 + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
        }
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Harmonica.shootAbility(hero, this);
    }

    public static void shootAbility(Hero hero, FirearmWeapon wep) {
        wep.beforeAbilityUsed(hero);
        if (hero.buff(GuidedShot.class) != null) {
            hero.buff(GuidedShot.class).onUse = !hero.buff(GuidedShot.class).onUse;
        }
        Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
        hero.sprite.operate(hero.pos);
        wep.afterAbilityUsed(hero);
    }

    public static class GuidedShot extends Buff {

        public static float HIT_CHARGE_USE = 1f;

        public boolean onUse = true;

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        public int hitsLeft(){
            MeleeWeapon.Charger charger = Buff.affect(target, MeleeWeapon.Charger.class);
            float charges = charger.charges;
            charges += charger.partialCharge;

            return (int)(charges/HIT_CHARGE_USE);
        }

        public int maxHit(){
            MeleeWeapon.Charger charger = Buff.affect(target, MeleeWeapon.Charger.class);
            float maxCharges = charger.chargeCap();

            return (int)(maxCharges);
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_BOMB;
        }

        @Override
        public void tintIcon(Image icon) {
            if (hitsLeft() == 0 || !onUse){
                icon.brightness(0.25f);
            } else {
                icon.resetColor();
            }
        }

        @Override
        public float iconFadePercent() {
            float usableCharges = hitsLeft()*HIT_CHARGE_USE;

            return 1f - (usableCharges /  Buff.affect(target, MeleeWeapon.Charger.class).chargeCap());
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(hitsLeft());
        }

        @Override
        public String desc() {
            if (!onUse) {
                return Messages.get(this, "no_use", hitsLeft(), maxHit());
            } else {
                return Messages.get(this, "desc", hitsLeft(), maxHit());
            }
        }

    }

}
