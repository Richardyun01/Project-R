/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo2;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class LanceCombo extends Buff implements ActionIndicator.Action {

	{
		type = buffType.POSITIVE;
	}
	
	private int count = 0;

	@Override
	public int icon() {
		return BuffIndicator.LANCE_COMBO;
	}
	
	@Override
	public void tintIcon(Image icon) {
		ComboMove move = getHighestMove();
		if (move != null){
			icon.hardlight(move.tintColor & 0x00FFFFFF);
		} else {
			icon.resetColor();
		}
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)1);
	}
	
	public void hit( Char enemy ) {

		count++;
		if (Dungeon.hero.hasTalent(Talent.BURNING_BLOOD) && Random.Int(10) < Dungeon.hero.pointsInTalent(Talent.BURNING_BLOOD)) {
			count++;
		}
		if (Dungeon.hero.subClass == HeroSubClass.PHALANX && Random.Int(10) < 1) {
			count++;
		}

		if ((getHighestMove() != null)) {

			ActionIndicator.setAction( this );

			GLog.p( Messages.get(this, "combo", count) );

		}

		BuffIndicator.refreshHero(); //refresh the buff visually on-hit

	}

	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
	}

	@Override
	public boolean act() {
		spend(TICK);
		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", count);
	}

	private static final String COUNT = "count";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt( COUNT );

		if (getHighestMove() != null) ActionIndicator.setAction(this);
	}

	@Override
	public String actionName() {
		return Messages.get(this, "action_name");
	}

	@Override
	public int actionIcon() {
		return HeroIcon.USE_PHALANX;
	}

	@Override
	public Visual secondaryVisual() {
		BitmapText txt = new BitmapText(PixelScene.pixelFont);
		txt.text( Integer.toString(count) );
		txt.hardlight(CharSprite.POSITIVE);
		txt.measure();
		return txt;
	}

	@Override
	public int indicatorColor() {
		return 0x660000;
	}

	@Override
	public void doAction() {
		GameScene.show(new WndCombo2(this));
	}

	public enum ComboMove {
		DISSECT   (5, 0xFF00FF00), // skill-1
		CRACKDOWN  (10, 0xFFFFFF00),  // skill-2
		MUTILATION   (15, 0xFFFF0000); // skill-3

		public int comboReq, tintColor;

		ComboMove(int comboReq, int tintColor){
			this.comboReq = comboReq;
			this.tintColor = tintColor;
		}

		public String desc(int count){
			switch (this){
				default:
					return Messages.get(this, name()+"_desc");
				case DISSECT:
					return Messages.get(this, name()+"_desc", 100+15*Dungeon.hero.pointsInTalent(Talent.PRECISE_DISSECTION), 20+20*Dungeon.hero.pointsInTalent(Talent.PRECISE_DISSECTION));
				case CRACKDOWN:
					return Messages.get(this, name()+"_desc", 100+10*Dungeon.hero.pointsInTalent(Talent.EXPLOSION));
			}
		}

	}

	public ComboMove getHighestMove(){
		ComboMove best = null;
		for (ComboMove move : ComboMove.values()){
			if (count >= move.comboReq){
				best = move;
			}
		}
		return best;
	}

	public int getComboCount(){
		return count;
	}

	public boolean canUseMove(ComboMove move){
		return move.comboReq <= count;
	}

	public void useMove(ComboMove move){
		if (Dungeon.hero.subClass == HeroSubClass.PHALANX && count == 0) {
			if (Random.Int(6) == 0) {
				Buff.affect( hero, Bless.class, 3f);
			} else if (Random.Int(6) == 1) {
				Buff.affect( hero, Adrenaline.class, 3f);
			} else if (Random.Int(6) == 2) {
				int barrAmt = 1;
				barrAmt = Math.min( barrAmt, hero.HT - hero.HP );
				if (barrAmt > 0 && hero.isAlive()) {
					Buff.affect(hero, Barrier.class).setShield((int) (2));
					hero.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( barrAmt ) );
				}
			} else if (Random.Int(6) == 3) {
				Buff.affect( hero, Haste.class, 3);
			} else if (Random.Int(6) == 4) {
				Buff.affect( hero, ArtifactRecharge.class).prolong(3);
			} else if (Random.Int(6) == 5) {
				Buff.affect( hero, Invisibility.class, 3);
			} else if (Random.Int(6) == 6) {
				Buff.affect( hero, MagicImmune.class, 3);
			}
		}
		moveBeingUsed = move;
		GameScene.selectCell(listener);
	}

	private static ComboMove moveBeingUsed;

	private void doAttack(final Char enemy) {

		AttackIndicator.target(enemy);

		boolean wasAlly = enemy.alignment == target.alignment;
		Hero hero = (Hero) target;

		float dmgMulti = 1f;
		int dmgBonus = 0;

		//variance in damage dealt
		switch (moveBeingUsed) {
			case DISSECT:
				dmgMulti = 1f + 0.15f * hero.pointsInTalent(Talent.PRECISE_DISSECTION);
				break;
			case CRACKDOWN:
				dmgMulti = 1f + 0.1f * hero.pointsInTalent(Talent.EXPLOSION);
				break;
			case MUTILATION:
				if (hero.hasTalent(Talent.RED_RAGE)) {
					dmgMulti = 1.2f;
				} else {
					dmgMulti = 1f;
				}
				break;
		}

		if (hero.attack(enemy, dmgMulti, dmgBonus, Char.INFINITE_ACCURACY)){
			//special on-hit effects
			switch (moveBeingUsed) {
				case DISSECT:
					if (hero.belongings.weapon != null) {
						Buff.affect(enemy, LanceBleeding.class).set(hero.belongings.weapon.max() / 5 * (1+hero.pointsInTalent(Talent.PRECISE_DISSECTION)));
					} else {
						Buff.affect(enemy, LanceBleeding.class).set(RingOfForce.damageRoll(hero) / 5 * (1+hero.pointsInTalent(Talent.PRECISE_DISSECTION)));
					}
					break;
				case CRACKDOWN:
					WandOfBlastWave.BlastWave.blast(enemy.pos);
					for (int n : PathFinder.NEIGHBOURS9) {
						int c = enemy.pos + n;
						if (c >= 0 && c < Dungeon.level.length()) {
							Char ch = Actor.findChar(c);
							if (ch != null && ch != hero) {
								int aoeHit = Math.round(target.damageRoll());
								ch.damage(aoeHit, target);
								ch.sprite.bloodBurstA(target.sprite.center(), aoeHit);
								ch.sprite.flash();
							}
						}
					}
					if (hero.hasTalent(Talent.EXPLOSION) && Random.Int(5) < hero.pointsInTalent(Talent.EXPLOSION)) {
						Buff.affect(enemy, Paralysis.class, 1);
					}
					break;
				case MUTILATION:
					if (hero.hasTalent(Talent.RED_RAGE) && hero.pointsInTalent(Talent.RED_RAGE) >= 2) {
						Buff.affect(enemy, Cripple.class, 4);
					}
					break;
				default:
					//nothing
					break;
			}
		}

		Invisibility.dispel();

		//Post-attack behaviour
		switch(moveBeingUsed){
			case MUTILATION:
				count--;
				if (count > 0 && enemy.isAlive() && hero.canAttack(enemy) &&
					(wasAlly || enemy.alignment != target.alignment)){
					if (count >= 14) {
						count = 2;
						if (hero.pointsInTalent(Talent.RED_RAGE) == 3) count = 3;
					}
					target.sprite.attack(enemy.pos, new Callback() {
						@Override
						public void call() {
							doAttack(enemy);
						}
					});
					if (hero.belongings.weapon != null) {
						Buff.affect(enemy, LanceBleeding.class).set(hero.belongings.weapon.max() / 3);
					} else {
						Buff.affect(enemy, LanceBleeding.class).set(RingOfForce.damageRoll(hero) / 3);
					}
					Buff.affect(enemy, Vulnerable.class, 4);
				} else {
					detach();
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					ActionIndicator.clearAction(LanceCombo.this);
					hero.spendAndNext(hero.attackDelay());
				}
				break;
			default:
				detach();
				ActionIndicator.clearAction(LanceCombo.this);
				hero.spendAndNext(hero.attackDelay());
				break;
		}

	}

	private CellSelector.Listener listener = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| enemy == target
					|| !Dungeon.level.heroFOV[cell]
					|| target.isCharmedBy( enemy )) {
				GLog.w(Messages.get(Combo.class, "bad_target"));

			} else if (!((Hero)target).canAttack(enemy)){
				Ballistica c = new Ballistica(target.pos, enemy.pos, Ballistica.PROJECTILE);
				if (c.collisionPos == enemy.pos){
					final int leapPos = c.path.get(c.dist-1);
					if (!Dungeon.level.passable[leapPos]){
						GLog.w(Messages.get(Combo.class, "bad_target"));
					} else {
						Dungeon.hero.busy();
						target.sprite.jump(target.pos, leapPos, new Callback() {
							@Override
							public void call() {
								target.move(leapPos);
								Dungeon.level.occupyCell(target);
								Dungeon.observe();
								GameScene.updateFog();
								target.sprite.attack(cell, new Callback() {
									@Override
									public void call() {
										doAttack(enemy);
									}
								});
							}
						});
					}
				} else {
					GLog.w(Messages.get(Combo.class, "bad_target"));
				}
			} else {
				Dungeon.hero.busy();
				target.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		@Override
		public String prompt() {
			return Messages.get(Combo.class, "prompt");
		}
	};
}
