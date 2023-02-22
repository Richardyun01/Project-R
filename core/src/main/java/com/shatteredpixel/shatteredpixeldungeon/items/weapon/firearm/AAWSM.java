package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class AAWSM extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.AAWS_M;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        type = FirearmType.FirearmExplosive;
        max_round = 1;

        bullet_image = ItemSpriteSheet.ROCKET_2;
        bullet_sound = Assets.Sounds.PUFF;
    }

    @Override
    public void setReloadTime() {
        reload_time = 3f * round * RingOfReload.reloadMultiplier(Dungeon.hero);
    }

    @Override
    public int min(int lvl) {
        return  0 +  //base
                lvl*2;    //level scaling
    }

    @Override
    public int max(int lvl) {
        return  10 +    //base
                lvl*3;   //level scaling
    }

    @Override
    public int Bulletmin(int lvl) {
        return (tier+8) + lvl*5 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int Bulletmax(int lvl) {
        return (tier)*12 + lvl*9 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

}