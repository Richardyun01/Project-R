package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Murakumo extends MeleeWeapon{

    {
        image = ItemSpriteSheet.MURAKUMO;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.2f;

        tier = 5;
    }

    @Override
    public int max(int lvl) {
        return  3*(tier) +    //15 base
                lvl*(tier);     //scaling +5 per +1
    }

}
