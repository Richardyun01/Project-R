package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDread;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class DragWave extends TargetedSpell {

    {
        image = ItemSpriteSheet.RECYCLE;
        usesTargeting = true;
    }
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing(0);

    public ItemSprite.Glowing glowing() {
        return COL;
    }

    @Override
    protected void affectTarget(Ballistica bolt, Hero hero) {
        int cell = bolt.collisionPos;
        Char findChar = Actor.findChar(cell);
        if (findChar != null) {
            Splash.at(cell, 0, 10);
            Ballistica ballistica = new Ballistica(curUser.pos, findChar.pos, 1);
            WandOfBlastWave.throwChar(findChar, ballistica, 3, true, true, this.getClass());
        } else {
            GLog.w( Messages.get(this, "no_target"));
        }
    }

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity, rounds down
        return (int)((50 + 40) * (quantity/12f));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ScrollOfDread.class, PotionOfLevitation.class};
            inQuantity = new int[]{1, 1};

            cost = 8;

            output = DragWave.class;
            outQuantity = 12;
        }

    }
}
