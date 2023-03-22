package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.lance;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AfterImageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class AfterImage extends ArmorAbility {

    {
        baseChargeUse = 35f;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
        Buff.affect(hero, AfterImageBuff.class, 10+3*hero.pointsInTalent(Talent.MULTIPLE_IMAGE));
        if (hero.hasTalent(Talent.LIVING_IMAGE)) {
            new ScrollOfMirrorImage();
            ScrollOfMirrorImage.spawnImages(hero, 1*hero.pointsInTalent(Talent.LIVING_IMAGE));
        }
        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
        hero.spendAndNext(Actor.TICK);
    }

    @Override
    public int icon() {
        return HeroIcon.AFTERIMAGE;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.MULTIPLE_IMAGE, Talent.LIVING_IMAGE, Talent.IMAGE_WALKING, Talent.HEROIC_ENERGY};
    }
}
