package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.lance;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StarburstBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class Starburst extends ArmorAbility {

    {
        baseChargeUse = 55f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
        int dmgBonus = 50;

        if (hero.hasTalent(Talent.PHOBIC_SAW)) {
            if (hero.belongings.weapon == null) {
                dmgBonus += (int)(0.25f * RingOfForce.armedDamageBonus(hero) * hero.pointsInTalent(Talent.PHOBIC_SAW));
            } else {
                dmgBonus += (int)(0.25f * hero.belongings.weapon.max() * hero.pointsInTalent(Talent.PHOBIC_SAW));
            }
        }
        Buff.affect(hero, StarburstBuff.class).setBonus(dmgBonus);
        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.STARBURST;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.PHOBIC_SAW, Talent.GRIND_DEFENCE, Talent.WARPED_BLADE, Talent.HEROIC_ENERGY};
    }
}
