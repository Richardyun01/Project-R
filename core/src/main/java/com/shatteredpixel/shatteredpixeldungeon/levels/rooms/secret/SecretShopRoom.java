package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.MerchantsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class SecretShopRoom extends SecretRoom {
    private ArrayList<Item> itemsToSpawn;

    public int minWidth() {
        return Math.max(6, (int) (Math.sqrt((double) itemCount()) + 2.0d));
    }

    public int minHeight() {
        return Math.max(6, (int) (Math.sqrt((double) itemCount()) + 2.0d));
    }

    public int itemCount() {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        return this.itemsToSpawn.size();
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);
        placeShopkeeper(level);
        placeItems(level);
        for (Room.Door door : this.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }
    }

    protected void placeShopkeeper(Level level) {
        int pointToCell = level.pointToCell(center());
        Shopkeeper miniShopkeeper = new Shopkeeper();
        miniShopkeeper.pos = pointToCell;
        level.mobs.add(miniShopkeeper);
    }

    protected void placeItems(Level level) {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        Point point = new Point(entrance());
        if (point.y == this.top) {
            point.y++;
        } else if (point.y == this.bottom) {
            point.y--;
        } else if (point.x == this.left) {
            point.x++;
        } else {
            point.x--;
        }
        Iterator<Item> it = this.itemsToSpawn.iterator();
        while (it.hasNext()) {
            Item next = it.next();
            if (point.x == this.left + 1 && point.y != this.top + 1) {
                point.y--;
            } else if (point.y == this.top + 1 && point.x != this.right - 1) {
                point.x++;
            } else if (point.x != this.right - 1 || point.y == this.bottom - 1) {
                point.x--;
            } else {
                point.y++;
            }
            int pointToCell = level.pointToCell(point);
            if (level.heaps.get(pointToCell) != null) {
                while (true) {
                    pointToCell = level.pointToCell(random());
                    if (level.heaps.get(pointToCell) == null && level.findMob(pointToCell) == null) {
                        break;
                    }
                }
            }
            level.drop(next, pointToCell).type = Heap.Type.FOR_SALE;
        }
    }

    protected static ArrayList<Item> generateItems() {
        ArrayList<Item> arrayList = new ArrayList<>();
        arrayList.add(new MerchantsBeacon());
        arrayList.add(Generator.randomUsingDefaults(Generator.Category.POTION));
        arrayList.add(Generator.randomUsingDefaults(Generator.Category.SCROLL));
        arrayList.add(Generator.randomUsingDefaults(Generator.Category.RING));
        arrayList.add(Generator.randomUsingDefaults(Generator.Category.STONE));
        arrayList.add(Generator.randomUsingDefaults(Generator.Category.STONE));
        arrayList.add(Generator.randomUsingDefaults(Generator.Category.SEED));
        arrayList.add(Generator.randomUsingDefaults(Generator.Category.SEED));
        //arrayList.add(Generator.randomUsingDefaults(Generator.Category.N_INGREDINETS));
        //arrayList.add(Generator.randomUsingDefaults(Generator.Category.N_INGREDINETS));
        //arrayList.add(Generator.randomUsingDefaults(Generator.Category.N_INGREDINETS));
        //arrayList.add(Generator.randomUsingDefaults(Generator.Category.N_INGREDINETS));
        if (Random.Int(8) == 0) {
            arrayList.add(new ScrollOfTransmutation());
        } else {
            arrayList.add(new PotionOfHealing());
        }
        return arrayList;
    }
}
