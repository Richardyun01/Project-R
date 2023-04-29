package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM301;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.watabou.utils.Point;

public class DM301Room extends SpecialRoom {

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);

        Point c = center();
        int cx = c.x;
        int cy = c.y;

        Door door = entrance();
        door.set(Room.Door.Type.BARRICADE);

        DM301 dm301 = new DM301();
        dm301.pos = cx + cy * level.width();
        level.mobs.add( dm301 );

        level.addItemToSpawn(new PotionOfLiquidFlame());
    }

    @Override
    public boolean connect(Room room) {
        //cannot connect to entrance, otherwise works normally
        if (room instanceof EntranceRoom) return false;
        else                              return super.connect(room);
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    @Override
    public boolean canPlaceWater(Point p) {
        return false;
    }

    @Override
    public boolean canPlaceGrass(Point p) {
        return false;
    }

    public int maxHeight() {
        return 12;
    }

    public int maxWidth() {
        return 12;
    }

    public int minHeight() {
        return 9;
    }

    public int minWidth() {
        return 9;
    }

}
