package com.shatteredpixel.shatteredpixeldungeon.items.weapon.weaponarm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.SpeedLoader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.Blunderbust;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm.FirearmWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Vulkan extends FirearmWeapon {

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.VULKAN;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        type = FirearmType.FirearmShotgun;
        max_round = 12;
        shot = 6;

        firearm = true;
        firearmShotgun = true;
        weaponarm = true;

        bullet_image = ItemSpriteSheet.TRIPLE_BULLET;
    }

    @Override
    public float accuracyFactorBullet(Char owner, Char target) {
        if (owner instanceof Hero &&
                owner.buff(Blunderbust.SlugShot.class) != null &&
                owner.buff(MeleeWeapon.Charger.class) != null &&
                owner.buff(Blunderbust.SlugShot.class).onUse &&
                owner.buff(MeleeWeapon.Charger.class).charges >= 1) {
            return 1f;
        } else {
            return Dungeon.level.adjacent(owner.pos, target.pos) ? 1.5f : 0f;
        }
    }

    @Override
    public void setReloadTime() {
        if (loader != null) {
            reload_time = 1.5f * RingOfReload.reloadMultiplier(Dungeon.hero) * SpeedLoader.reloadMultiplier();
        } else {
            reload_time = 1.5f * RingOfReload.reloadMultiplier(Dungeon.hero);
        }
    }

    @Override
    protected void carrollAbility(Hero hero, Integer target ) {
        Blunderbust.shootAbility(hero, this);
    }

}
