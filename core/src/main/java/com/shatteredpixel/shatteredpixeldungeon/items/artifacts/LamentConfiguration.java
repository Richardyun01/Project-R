package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LamentConfiguration extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_LAMENT;

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
        if (Dungeon.hero.belongings.weapon != null) {
            if (Dungeon.hero.belongings.weapon.weaponarm) {
                return;
            }
        }

        if (action.equals( AC_USE ) && this.activeBuff == null) {
            if (!isEquipped(hero)) {
                GLog.i(Messages.get(Artifact.class, "need_to_equip", new Object[0]), new Object[0]);
            } else if (this.cursed) {
                GLog.i(Messages.get((Object) this, "cursed", new Object[0]), new Object[0]);
            } else if (this.charge < 100) {
                GLog.i(Messages.get((Object) this, "no_charge", new Object[0]), new Object[0]);
            } else {
                new SummoningTrap().set( Dungeon.hero.pos ).activate();
                new SummoningTrap().set( Dungeon.hero.pos ).activate();
                if (Random.Int(10) < this.level()) {
                    Item reward = null;
                    do {
                        switch (Random.Int(3)) {
                            case 0:
                                reward = Generator.random(Generator.Category.WAND);
                                break;
                            case 2:
                                reward = Generator.randomArmor();
                                break;
                            case 3:
                                reward = Generator.randomWeapon();
                                break;
                        }
                        float itemLevelRoll = Random.Float();
                        int itemLevel;
                        if (itemLevelRoll < 0.5f){
                            itemLevel = 0;
                        } else if (itemLevelRoll < 0.8f){
                            itemLevel = 1;
                        } else if (itemLevelRoll < 0.95f){
                            itemLevel = 2;
                        } else {
                            itemLevel = 3;
                        }
                        reward.upgrade(itemLevel);
                    } while (reward == null || Challenges.isItemBlocked(reward));
                    Dungeon.level.drop(reward, Dungeon.hero.pos).sprite.drop();
                }
                Invisibility.dispel(hero);
                curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 2 );
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
            this.charge += Math.round(f * 1.5f);
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
                desc += Messages.get(this, "desc_equipped");
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
            if (LamentConfiguration.this.activeBuff != null || ((lockedFloor != null && !lockedFloor.regenOn()) || (Dungeon.depth >= 26 && Dungeon.depth <= 30))) {
                LamentConfiguration.this.partialCharge = 0.0f;
            } else if (LamentConfiguration.this.charge < LamentConfiguration.this.chargeCap && !LamentConfiguration.this.cursed) {
                LamentConfiguration.this.partialCharge += RingOfEnergy.artifactChargeMultiplier(this.target) * 0.13f;
                if (LamentConfiguration.this.partialCharge > 1.0f && LamentConfiguration.this.charge < LamentConfiguration.this.chargeCap) {
                    LamentConfiguration.this.partialCharge -= 1.0f;
                    LamentConfiguration.this.charge++;
                    Item.updateQuickslot();
                }
            } else if (cursed && Random.Int(100) == 0){
                new SummoningTrap().set( Dungeon.hero.pos ).activate();
            }
            spend(1.0f);
            return true;
        }

        public void charge(Hero hero, float f) {
            LamentConfiguration.this.charge += Math.round(f * 1.5f);
            LamentConfiguration lamentConfiguration = LamentConfiguration.this;
            lamentConfiguration.charge = Math.min(lamentConfiguration.charge, LamentConfiguration.this.chargeCap);
            Item.updateQuickslot();
        }
    }

}
