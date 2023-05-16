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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.EvolutionFire;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Evolution;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class SecretEvolutionRoom extends SecretRoom {

    @Override
    public int minWidth() { return 7; }
    public int minHeight() { return 7; }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.CHASM );

        Point c = center();
        Door door = entrance();
        if (door.x == left || door.x == right) {
            if (door.y == c.y) c.y += Random.Int(2) == 0 ? -1 : +1;
            Point p = Painter.drawInside( level, this, door, Math.abs( door.x - c.x ) - 2, Terrain.EMPTY_SP );
            for (; p.y != c.y; p.y += p.y < c.y ? +1 : -1) {
                Painter.set( level, p, Terrain.EMPTY_SP );
            }
        } else {
            if (door.x == c.x) c.x += Random.Int(2) == 0 ? -1 : +1;
            Point p = Painter.drawInside( level, this, door, Math.abs( door.y - c.y ) - 2, Terrain.EMPTY_SP );
            for (; p.x != c.x; p.x += p.x < c.x ? +1 : -1) {
                Painter.set( level, p, Terrain.EMPTY_SP );
            }
        }

        //we add four statues to give some cover from ranged enemies
        Point statue = new Point(c);
        statue.x -= 2;
        if (statue.x > left) Painter.set( level, statue, Terrain.BOOKSHELF );
        statue.x += 2; statue.y -= 2;
        if (statue.y > top) Painter.set( level, statue, Terrain.BOOKSHELF );
        statue.y += 2; statue.x += 2;
        if (statue.x < right) Painter.set( level, statue, Terrain.BOOKSHELF );
        statue.x -= 2; statue.y += 2;
        if (statue.y < bottom) Painter.set( level, statue, Terrain.BOOKSHELF );

        //Painter.fill( level, c.x - 1, c.y - 1, 3, 3, Terrain.EMBERS );
        Painter.set( level, c, Terrain.PEDESTAL );

        Blob.seed( level.pointToCell(c), 6 + Dungeon.depth * 4, EvolutionFire.class, level ).setPrize(prize());

        door.set( Door.Type.EMPTY );
    }

    public static Item prize( ) {
        return new Evolution();
    }

}