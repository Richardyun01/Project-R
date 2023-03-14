package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class OpIntensify extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_PLUS;

        levelCap = 10;
        charge = 100;
        partialCharge = 0.0f;
        chargeCap = 100;

        defaultAction = AC_USE;
    }

    public static final String AC_USE = "USE";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && charge > 0 && !cursed && hero.buff(MagicImmune.class) == null) {
            actions.add(AC_USE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (action.equals( AC_USE ) && this.activeBuff == null) {
            if (!isEquipped(hero)) {
                GLog.i(Messages.get(Artifact.class, "need_to_equip", new Object[0]), new Object[0]);
            } else if (this.cursed) {
                GLog.i(Messages.get((Object) this, "cursed", new Object[0]), new Object[0]);
            } else if (this.charge < 100) {
                GLog.i(Messages.get((Object) this, "no_charge", new Object[0]), new Object[0]);
            } else {
                Buff.affect(hero, Stamina.class, 10);
                if (level() >= 3) {
                    Buff.affect(hero, Foresight.class, 30f);
                    if (level() >= 7) {
                        Buff.affect(hero, FireImbue.class).set( 16 );
                        Buff.affect(hero, Adrenaline.class, 15f);
                        if (level() >= 10) {
                            Buff.affect(hero, ArcaneArmor.class).set(5 + hero.lvl/2, 26);
                            Buff.affect(hero, Barkskin.class).set(2 + hero.lvl/3, 26);
                        }
                    }
                }
                Invisibility.dispel(hero);
                this.charge = 0;
                updateQuickslot();
                if (level() < this.levelCap) {
                    upgrade();
                }
                curUser.spendAndNext(1.0f);
            }
        }
    }

    public void charge(Hero hero, float f) {
        if (this.charge < this.chargeCap) {
            this.charge += Math.round(f * 1.0f);
            if (this.charge >= this.chargeCap) {
                this.charge = this.chargeCap;
                updateQuickslot();
            }
        }
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped (Dungeon.hero)) {
            desc += "\n\n";
            if (cursed)
                desc += Messages.get(this, "desc_cursed");
            else
                desc += Messages.get(this, "desc_equipped_1");
            if (level() >= 3)  desc += Messages.get(this, "desc_equipped_2");
            if (level() >= 7)  desc += Messages.get(this, "desc_equipped_3");
            if (level() >= 10) desc += Messages.get(this, "desc_equipped_4");
        }

        return desc;
    }

    public Artifact.ArtifactBuff passiveBuff() {
        return new ArtifactPlusBuff();
    }

    public class ArtifactPlusBuff extends ArtifactBuff {
        public ArtifactPlusBuff() { super(); }

        public boolean act() {
            LockedFloor lockedFloor = (LockedFloor) this.target.buff(LockedFloor.class);
            if (OpIntensify.this.activeBuff != null || ((lockedFloor != null && !lockedFloor.regenOn()) || (Dungeon.depth >= 26 && Dungeon.depth <= 30))) {
                OpIntensify.this.partialCharge = 0.0f;
            } else if (OpIntensify.this.charge < OpIntensify.this.chargeCap && !OpIntensify.this.cursed) {
                OpIntensify.this.partialCharge += RingOfEnergy.artifactChargeMultiplier(this.target) * 0.13f;
                if (OpIntensify.this.partialCharge > 1.0f && OpIntensify.this.charge < OpIntensify.this.chargeCap) {
                    OpIntensify.this.partialCharge -= 1.0f;
                    OpIntensify.this.charge++;
                    Item.updateQuickslot();
                }
            }
            spend(1.0f);
            return true;
        }

        public void charge(Hero hero, float f) {
            OpIntensify.this.charge += Math.round(f * 1.0f);
            OpIntensify opIntensify = OpIntensify.this;
            opIntensify.charge = Math.min(opIntensify.charge, OpIntensify.this.chargeCap);
            Item.updateQuickslot();
        }
    }

}
