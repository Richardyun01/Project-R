package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Reiterpallasch extends FirearmWeapon{

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.REITERPALLASCH;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
        type = FirearmType.FirearmEtc1;
        max_round = 3;
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier, lvl)-1; //18 base strength req, down from 19
    }

    @Override
    public int min(int lvl) {
        return  tier +  //base
                lvl;    //level scaling
    }

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //24 base, down from 30
                lvl*(tier+1);   //scaling unchanged
    }

    @Override
    public int Bulletmin(int lvl) {
        return (tier) +
                lvl;
    }

    @Override
    public int Bulletmax(int lvl) {
        return 2 * tier +
                (3*lvl);
    }

}
