package com.shatteredpixel.shatteredpixeldungeon.items.weapon.weaponarm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Mace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Draken extends MeleeWeapon {

    {
        defaultAction = AC_MODE;
        image = ItemSpriteSheet.DRAKEN;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1f;

        ACC = 1.4f;
        DLY = 1.0f;

        weaponarm = true;

        tier=5;
    }

    public static final String AC_MODE  	= "MODE";
    public static final String ACCURACY     = "ACCURACY";
    public static final String DELAY        = "DELAY";
    public static final String SWITCH       = "SWITCH";
    private int switchMode = 0;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            actions.add("MODE");
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_MODE) && isEquipped(hero)) {
            int modeCount = switchMode+1;
            switchMode = modeCount;
            if (modeCount > 2) {
                switchMode = 0;
            }
            int modeCount2 = switchMode;
            if (modeCount2 == 1) {
                this.ACC = 1.0f;
                this.DLY = 0.75f;
            } else if (modeCount2 != 2) {
                this.ACC = 1.4f;
                this.DLY = 1.0f;
            } else {
                this.ACC = 0.4f;
                this.DLY = 0.5f;
            }
            updateQuickslot();
            curUser.spendAndNext(1.0f);
        }
    }

    @Override
    public int max(int lvl) {
        return  4*(tier) +
                lvl*(tier-1);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (switchMode == 2) {
            defender.damage(attacker.damageRoll(), attacker);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public String desc() {
        String str;
        int i = switchMode;
        if (i == 2) {
            str = Messages.get((Object) this, "desc_mode2", new Object[0]);
        } else if (i == 1) {
            str = Messages.get((Object) this, "desc_mode1", new Object[0]);
        } else {
            str = Messages.get((Object) this, "desc_mode0", new Object[0]);
        }
        return str + "\n\n" + super.desc();
    }

    @Override
    public String status() {
        if (!isIdentified()) {
            return null;
        }
        int i = switchMode;
        if (i == 2) {
            return "OV";
        }
        return i == 1 ? "SH" : "AR";
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SWITCH, switchMode);
        bundle.put(ACCURACY, ACC);
        bundle.put(DELAY, DLY);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        switchMode = bundle.getInt(SWITCH);
        ACC = bundle.getFloat(ACCURACY);
        DLY = bundle.getFloat(DELAY);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target) {
        Mace.heavyBlowAbility(hero, target, 1.5f, this);
    }

}
