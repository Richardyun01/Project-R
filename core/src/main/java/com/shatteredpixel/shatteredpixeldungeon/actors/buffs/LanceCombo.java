package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class LanceCombo extends Buff {

    {
        type = Buff.buffType.POSITIVE;
    }

    private int count = 0;
    private float comboTime = 0f;
    private float initialComboTime = 5f;

    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    /*
    @Override
    public void tintIcon(Image icon) {
        Combo.ComboMove move = getHighestMove();
        if (move != null){
            icon.hardlight(move.tintColor & 0x00FFFFFF);
        } else {
            icon.resetColor();
        }
    }
    */

    @Override
    public float iconFadePercent() {
        return Math.max(0, (initialComboTime - comboTime) / initialComboTime);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int) comboTime);
    }

    public Image actionIcon() {
        Image icon;
        if (((Hero) target).belongings.weapon() != null) {
            icon = new ItemSprite(((Hero) target).belongings.weapon().image, null);
        } else {
            icon = new ItemSprite(new Item() {
                {
                    image = ItemSpriteSheet.WEAPON_HOLDER;
                }
            });
        }

        icon.tint(getHighestMove().tintColor);
        return icon;
    }

    public enum ComboMove {
        DISSECT(2, 0xFF00FF00),
        CRACKDOWN(4, 0xFFCCFF00),
        MUTILATION(6, 0xFFFFFF00);

        public int comboReq, tintColor;

        ComboMove(int comboReq, int tintColor) {
            this.comboReq = comboReq;
            this.tintColor = tintColor;
        }

        public String desc(int count) {
            return Messages.get(this, name() + "_desc");
        }
    }

    public Combo.ComboMove getHighestMove(){
        Combo.ComboMove best = null;
        for (Combo.ComboMove move : Combo.ComboMove.values()){
            if (count >= move.comboReq){
                best = move;
            }
        }
        return best;
    }
}