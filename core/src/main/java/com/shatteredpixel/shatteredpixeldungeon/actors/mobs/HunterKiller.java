package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NoEnergy;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LightJunkerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.Iterator;

public class HunterKiller extends Mob {

    private static final String COOLDOWN = "CoolDown";
    private static final String SKILLPOS = "SkillPos";
    private int CoolDown = 0;
    private int SkillPos = -1;

    {
        spriteClass = LightJunkerSprite.class;

        HP = HT = 150;
        defenseSkill = 18;
        baseSpeed = 0.5f;
        flying = true;

        EXP = 35;
        maxLvl = 28;

        //loot = Generator.Category.WEAPON;
        lootChance = 1f; //by default, see lootChance()

        properties.add(Property.INORGANIC);

        immunities.add(NoEnergy.class);
        immunities.add(Corruption.class);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(55, 65);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 20);
    }

    @Override
    protected boolean act() {
        int coolDown = this.CoolDown;
        if (coolDown == 0) {
            int skillPos = this.SkillPos;
            if (skillPos == -1) {
                if (this.state != this.HUNTING) {
                    return super.act();
                }
                this.SkillPos = Dungeon.hero.pos;
                this.sprite.parent.addToBack(new TargetedCell(this.SkillPos, CharSprite.NEGATIVE));
                this.sprite.zap(this.enemy.pos);
                Iterator<Mob> it = Dungeon.level.mobs.iterator();
                while (it.hasNext()) {
                    Mob next = it.next();
                    if (next.paralysed <= 0 && Dungeon.level.distance(this.pos, next.pos) <= 7 && next.state != next.HUNTING) {
                        next.beckon(Dungeon.hero.pos);
                    }
                }
                spend(GameMath.gate(1.0f, Dungeon.hero.cooldown(), 2.0f));
                Dungeon.hero.interrupt();
                return true;
            } else if (skillPos == Dungeon.hero.pos) {
                Dungeon.hero.damage(damageRoll() - Dungeon.hero.drRoll(), this);
                Dungeon.hero.sprite.burst(CharSprite.NEGATIVE, 10);
                CellEmitter.center(this.SkillPos).burst(BlastParticle.FACTORY, 10);
                Camera.main.shake(5.0f, 0.5f);
                this.CoolDown = 1;
                this.SkillPos = -1;
                spend(1.0f);
                if (!Dungeon.hero.isAlive()) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get((Object) this, "bomb_kill", new Object[0]), new Object[0]);
                }
                return true;
            } else {
                CellEmitter.center(this.SkillPos).burst(BlastParticle.FACTORY, 10);
                Camera.main.shake(5.0f, 0.5f);
                this.CoolDown = 1;
                this.SkillPos = -1;
            }
        } else {
            this.CoolDown = coolDown - 1;
        }
        return super.act();
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN, this.CoolDown);
        bundle.put(SKILLPOS, this.SkillPos);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.CoolDown = bundle.getInt(COOLDOWN);
        this.SkillPos = bundle.getInt(SKILLPOS);
    }

}
