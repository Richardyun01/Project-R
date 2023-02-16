package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class TestCannon extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.FIRESTORM;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        type = FirearmType.FirearmExplosive;
        max_round = 4;

        bullet_image = ItemSpriteSheet.ROCKET_3;
    }

    @Override
    public void setReloadTime() {
        reload_time = 2f * round * RingOfReload.reloadMultiplier(Dungeon.hero);
    }

}
