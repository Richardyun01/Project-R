package com.shatteredpixel.shatteredpixeldungeon.items.weapon.firearm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

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
        reload_time = 6f *  RingOfReload.reloadMultiplier(Dungeon.hero);
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier+1, lvl); //22 base strength req, up from 20
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

    @Override
    public void onThrowBulletFirearmExplosive( int cell ) {
        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 5);
        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 5);
        ArrayList<Char> affected = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS9) {
            int c = cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 6);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 6);
                }
                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                }
                Char ch = Actor.findChar(c);
                if (ch != null) {
                    affected.add(ch);
                }
            }
        }
        Sample.INSTANCE.play( Assets.Sounds.BLAST );
    }
}
