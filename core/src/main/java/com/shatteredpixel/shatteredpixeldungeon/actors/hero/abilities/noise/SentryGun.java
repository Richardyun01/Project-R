package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noise;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SentryGun extends ArmorAbility {

    @Override
    public String targetingPrompt() {
        if (getTurret() == null) {
            return super.targetingPrompt();
        } else {
            return Messages.get(this, "prompt");
        }
    }

    @Override
    public boolean useTargeting(){
        return false;
    }

    {
        baseChargeUse = 35f;
    }

    @Override
    public float chargeUse(Hero hero) {
        if (getTurret() == null) {
            return super.chargeUse(hero);
        } else {
            return 0;
        }
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        SentryAlly ally = getTurret();

        if (ally != null){
            if (target == null){
                return;
            } else {
                ally.directTocell(target);
            }
        } else {
            ArrayList<Integer> spawnPoints = new ArrayList<>();
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                    spawnPoints.add(p);
                }
            }

            if (!spawnPoints.isEmpty()){
                armor.charge -= chargeUse(hero);
                armor.updateQuickslot();

                ally = new SentryAlly();
                ally.pos = Random.element(spawnPoints);
                GameScene.add(ally);

                ScrollOfTeleportation.appear(ally, ally.pos);
                Dungeon.observe();

                Invisibility.dispel();
                hero.spendAndNext(Actor.TICK);

            } else {
                GLog.w(Messages.get(this, "no_space"));
            }
        }

    }

    @Override
	public int icon() {
		return HeroIcon.SENTRY_GUN;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.NIGHT_RAVEN, Talent.BIO_DETECTOR, Talent.BURST_SENTRY, Talent.HEROIC_ENERGY};
	}

	private static SentryAlly getTurret(){
		for (Char ch : Actor.chars()){
			if (ch instanceof SentryAlly){
				return (SentryAlly) ch;
			}
		}
		return null;
	}

	public static class SentryAlly extends DirectableAlly {

		{
			spriteClass = SentrySprite.class;

			HP = HT = 50;
			defenseSkill = 60;

			flying = true;
			viewDistance = (int) GameMath.gate(5, 5+Dungeon.hero.pointsInTalent(Talent.BIO_DETECTOR), 8);
			baseSpeed = 0.5f;
			attacksAutomatically = false;

			immunities.addAll(new BlobImmunity().immunities());
			immunities.add(AllyBuff.class);
		}

		@Override
		public int attackSkill(Char target) {
			return 60;
		}

		private float timeRemaining = 100f;

		@Override
		public int defenseSkill(Char enemy) {
			return super.defenseSkill(enemy);
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(10 + 1*Dungeon.hero.pointsInTalent(Talent.BURST_SENTRY),
										20 + 2*Dungeon.hero.pointsInTalent(Talent.BURST_SENTRY));
		}

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 8);
        }

		@Override
        protected boolean canAttack( Char enemy ) {
            Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
            return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
        }

		@Override
		public int attackProc(Char enemy, int damage) {
			damage = super.attackProc( enemy, damage );
			if (Random.Int( 4 ) <= Dungeon.hero.pointsInTalent(Talent.NIGHT_RAVEN)) {
                Buff.prolong( enemy, Cripple.class, 1*Dungeon.hero.pointsInTalent(Talent.NIGHT_RAVEN) );
            }
			if (Random.Int( 25 ) <= Dungeon.hero.pointsInTalent(Talent.BURST_SENTRY)) {
				Buff.prolong( enemy, Paralysis.class, 1 );
			}

			return damage;
		}

		@Override
		protected boolean act() {
			if (timeRemaining <= 0){
				die(null);
				return true;
			}
			viewDistance = 6+Dungeon.hero.pointsInTalent(Talent.BIO_DETECTOR);
			baseSpeed = 2f;
			boolean result = super.act();
			Dungeon.level.updateFieldOfView( this, fieldOfView );
			GameScene.updateFog(pos, viewDistance+(int)Math.ceil(speed()));
			return result;
		}

		@Override
		protected void spend(float time) {
			super.spend(time);
			timeRemaining -= time;
		}

		@Override
		public void destroy() {
			super.destroy();
			Dungeon.observe();
			GameScene.updateFog();
		}

		@Override
		public void defendPos(int cell) {
			GLog.i(Messages.get(this, "direct_defend"));
			super.defendPos(cell);
		}

		@Override
		public void followHero() {
			GLog.i(Messages.get(this, "direct_follow"));
			super.followHero();
		}

		@Override
		public void targetChar(Char ch) {
			GLog.i(Messages.get(this, "direct_attack"));
			super.targetChar(ch);
		}

		@Override
		public String description() {
			String message = Messages.get(this, "desc", (int)timeRemaining);
			return message;
		}

		private static final String TIME_REMAINING  = "time_remaining";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TIME_REMAINING, timeRemaining);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			timeRemaining = bundle.getFloat(TIME_REMAINING);
		}
	}

	public static class SentrySprite extends MobSprite {

		public SentrySprite() {
			super();

			texture( Assets.Sprites.SENTRY_GUN );

			TextureFilm frames = new TextureFilm( texture, 16, 16 );

			int c = 0;

			idle = new Animation( 6, true );
			idle.frames( frames, 0, 1, 2, 3 );

			run = new Animation( 8, true );
			run.frames( frames, 0, 1, 2, 3 );

			attack = new Animation( 12, false );
			attack.frames( frames, 4, 5, 6, 7 );

			die = new Animation( 12, false );
			die.frames( frames, 8, 9, 10, 11, 12, 13, 14 );

			play( idle );
		}

		@Override
		public int blood() {
			return 0xFF00FFFF;
		}

		@Override
		public void die() {
			super.die();
			emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
			emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		}
	}

}

