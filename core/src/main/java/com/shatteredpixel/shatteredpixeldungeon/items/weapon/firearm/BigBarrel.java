package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BigBarrel extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.BIG_BARREL;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 6;
        type = FirearmType.FirearmExplosive;
        max_round = 1;

        bullet_image = ItemSpriteSheet.SHELL;
    }

    @Override
    public void setReloadTime() {
        reload_time = 6f * round * RingOfReload.reloadMultiplier(Dungeon.hero);
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier+1, lvl); //20 base strength req, up from 18
    }

    @Override
    public int min(int lvl) {
        return  15 +  //base
                lvl*3;    //level scaling
    }

    @Override
    public int max(int lvl) {
        return  30 +    //base
                lvl*4;   //level scaling
    }

    @Override
    public int Bulletmin(int lvl) {
        return (tier+4)*3 + lvl*6 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int Bulletmax(int lvl) {
        return (tier+4)*10 + lvl*10 + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

}
