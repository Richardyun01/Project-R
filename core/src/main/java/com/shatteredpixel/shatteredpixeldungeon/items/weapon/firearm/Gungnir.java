package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Gungnir extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.GUNGNIR;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.8f;

        tier = 6;
        DLY = 1.2f; //0.83x speed
        RCH = 2;    //extra reach
        type = FirearmType.FirearmExplosive;
        max_round = 1;
        ACC = 3f;

        bullet_image = ItemSpriteSheet.GUNGNIR;
        bullet_sound = Assets.Sounds.LIGHTNING;
    }

    @Override
    public void setReloadTime() {
        reload_time = 5f;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        return 999f;
    }

    @Override
    public int STRReq(int lvl) {                    //18 base strength req, down from 19
        if (hero.heroClass == HeroClass.NOISE) {
            return STRReq(tier, lvl);
        } else {
            return STRReq(tier, lvl);
        }
    }

    @Override
    public int max(int lvl) {
        return  Math.round(6.67f*(tier+1)) +    //40 base, up from 30
                lvl*Math.round(1.33f*(tier+1)); //+8 per level, up from +6
    }

    @Override
    public int Bulletmin(int lvl) {
        return 0;
    }

    @Override
    public int Bulletmax(int lvl) {
        return 0;
    }

    @Override
    public void reload() {
        curUser.spend(reload_time);
        curUser.busy();
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING, 3, 1.1f);
        curUser.sprite.operate(curUser.pos);
        round = Math.max(max_round, round);

        updateQuickslot();
    }

    @Override
    public void onThrowBulletFirearmExplosive( int cell ) {
        ArrayList<Char> affected = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS25) {
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
                    Buff.affect(ch, Vulnerable.class, 10f);
                }
            }
        }
    }

    @Override
    public String info() {

        setReloadTime();
        setMaxRound();
        String info = desc();

        int lvl = level();

        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.STR() > STRReq()){
                info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            }
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
        }

        String statsInfo = statsInfo();
        if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

        switch (augment) {
            case SPEED:
                info += " " + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += " " + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        if (Dungeon.hero.heroClass == HeroClass.CARROLL){
            info += "\n\n" + Messages.get(this, "ability_desc");
        }

        return info;
    }

    public String statsInfo(){
        return Messages.get(this, "stats_desc");
    }

    @Override
    public String status() {
        if (isEquipped(Dungeon.hero)
                && Dungeon.hero.buff(Charger.class) != null
                && Dungeon.hero.heroClass == HeroClass.CARROLL) {
            Charger buff = Dungeon.hero.buff(Charger.class);
            if (Dungeon.hero.belongings.weapon == this) {
                return buff.charges + "/" + buff.chargeCap();
            } else {
                return buff.secondCharges + "/" + buff.secondChargeCap();
            }
        } else {
            return super.status();
        }
    }

    @Override
    public float abilityChargeUse(Hero hero) {
        int total_charge = (int)((Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge));
        return total_charge*super.abilityChargeUse(hero);
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        if (hero.belongings.weapon == this &&
                (Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge) >= 5) {
            beforeAbilityUsed(hero);
            hero.HP = 1;
            Buff.prolong(hero, Gungnir.TwilightStance.class, 3 * ((Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge)));
            hero.sprite.operate(hero.pos);
            hero.next();
            afterAbilityUsed(hero);
        } else {
            GLog.n(Messages.get(this, "ability_not_enough_charge"));
        }
    }

    public static class TwilightStance extends FlavourBuff {

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 30;

        @Override
        public int icon() {
            return BuffIndicator.DUEL_TWILIGHT;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.aura( 0xFFFFFF00 );
            else target.sprite.clearAura();
        }

        {
            immunities.add(Paralysis.class);
            immunities.add(Frost.class);
            immunities.add(Burning.class);
            immunities.add(Poison.class);
            immunities.add(Bleeding.class);
            immunities.add(Corrosion.class);
            immunities.add(Ooze.class);

            immunities.add( ToxicGas.class );
            immunities.add( CorrosiveGas.class );
            immunities.add( Electricity.class );

            immunities.addAll( AntiMagic.RESISTS );
        }
    }
}
