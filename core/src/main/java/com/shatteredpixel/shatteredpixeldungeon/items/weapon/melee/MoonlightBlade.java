/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class MoonlightBlade extends MeleeWeapon {

    {
        image = ItemSpriteSheet.MOONLIGHT;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 5;

    }

    @Override
    public int max(int lvl) {
        return  2*(tier+1) +    //12 base, down from 30
                lvl*(tier-1);   //scaling 4 down from 6
    }

    @Override
    public int min(int lvl) {
        return  tier +    //5 base, down from 6
                lvl*(tier-3);   //scaling 2 down from 4
    }

    public String statsInfo() {
        if (isIdentified()) {
            return Messages.get((Object) this, "stats_desc", Integer.valueOf(buffedLvl() + 4), Integer.valueOf((buffedLvl() * 4) + 12));
        }
        return Messages.get((Object) this, "typical_stats_desc", 4, 12);
    }

    public int proc(Char charR, Char charR2, int i) {
        boolean z = false;
        Ballistica ballistica = new Ballistica(charR.pos, charR2.pos, Ballistica.WONT_STOP);
        int min = Math.min(5, ballistica.dist.intValue());
        charR.sprite.parent.add(new Beam.DeathRay(charR.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(ballistica.path.get(Math.min(ballistica.dist.intValue(), min)).intValue())));
        ArrayList arrayList = new ArrayList();
        Blob blob = Dungeon.level.blobs.get(Web.class);
        int i2 = 2;
        for (Integer intValue : ballistica.subPath(1, min)) {
            int intValue2 = intValue.intValue();
            Char findChar = Actor.findChar(intValue2);
            if (findChar != null) {
                i2 %= 3;
                arrayList.add(findChar);
            }
            if (Dungeon.level.solid[intValue2]) {
                i2++;
            }
            if (Dungeon.level.flamable[intValue2]) {
                Dungeon.level.destroy(intValue2);
                GameScene.updateMap(intValue2);
                z = true;
            }
            CellEmitter.center(intValue2).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
        }
        if (z) {
            Dungeon.observe();
        }
        int NormalIntRange = Random.NormalIntRange(buffedLvl() + 4, (buffedLvl() * 4) + 12);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Char charR3 = (Char) it.next();
            charR3.damage(NormalIntRange, this);
            charR3.sprite.centerEmitter().burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            charR3.sprite.flash();
        }
        return super.proc(charR, charR2, i);
    }

}