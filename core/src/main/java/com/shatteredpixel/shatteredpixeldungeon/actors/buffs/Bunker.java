package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

public class Bunker extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;

        //acts before the hero
        actPriority = HERO_PRIO+1;
    }

    private int bunkeringStacks = 0;
    private int bunkeringTurns = 0;
    private int bunkeringCooldown = 0;

    private boolean attackedLastTurn = true;

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public boolean act() {
        if (bunkeringCooldown > 0){
            bunkeringCooldown--;
        }

        if (!attackedLastTurn) {
            bunkeringStacks = (int) GameMath.gate(0, bunkeringStacks-1, Math.round(bunkeringStacks * 0.667f));
            if (bunkeringStacks <= 0) {
                ActionIndicator.clearAction(this);
                if (bunkeringCooldown <= 0) detach();
            }
        }

        attackedLastTurn = false;

        spend(TICK);
        return true;
    }

    public void gainStack(){
        attackedLastTurn = true;
        if (bunkeringCooldown <= 0 && !bunkering()){
            postpone(target.cooldown()+(1/target.speed()));
            bunkeringStacks = Math.min(bunkeringStacks + 1, 10);
            ActionIndicator.setAction(this);
        }
    }

    public boolean bunkering(){
        return bunkeringTurns > 0;
    }

    public float speedMultiplier(){
        if (bunkering()){
            return 0.2f+0.2f*Dungeon.hero.pointsInTalent(Talent.LIGHTWEIGHT_TOCHKA);
        } else {
            return 1;
        }
    }

    public float defenceBonus(int heroLvl, int excessArmorStr) {
        if (bunkeringTurns > 0) {
            return heroLvl/2 + excessArmorStr;
        } else {
            return 0;
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.BUNKER;
    }

    @Override
    public void tintIcon(Image icon) {
        if (bunkeringTurns > 0){
            icon.hardlight(1,1,0);
        } else if (bunkeringCooldown > 0){
            icon.hardlight(0.5f,0.5f,1);
        } else {
            icon.hardlight(1f - (bunkeringStacks /10f),1,1f - (bunkeringStacks /10f));
        }
    }

    @Override
    public float iconFadePercent() {
        if (bunkeringTurns > 0){
            return (20 - bunkeringTurns) / 20f;
        } else if (bunkeringCooldown > 0){
            return (bunkeringCooldown) / 30f;
        } else {
            return (10 - bunkeringStacks) / 10f;
        }
    }

    @Override
    public String iconTextDisplay() {
        if (bunkeringTurns > 0){
            return Integer.toString(bunkeringTurns);
        } else if (bunkeringCooldown > 0){
            return Integer.toString(bunkeringCooldown);
        } else {
            return Integer.toString(bunkeringStacks);
        }
    }

    @Override
    public String name() {
        if (bunkeringTurns > 0){
            return Messages.get(this, "bunkering");
        } else if (bunkeringCooldown > 0){
            return Messages.get(this, "resting");
        } else {
            return Messages.get(this, "bunker");
        }
    }

    @Override
    public String desc() {
        if (bunkeringTurns > 0){
            return Messages.get(this, "bunkering_desc", bunkeringTurns);
        } else if (bunkeringCooldown > 0){
            return Messages.get(this, "resting_desc", bunkeringCooldown);
        } else {
            return Messages.get(this, "bunker_desc", bunkeringStacks);
        }
    }

    private static final String STACKS =        "stacks";
    private static final String BUNKER_TURNS =  "bunker_turns";
    private static final String BUNKER_CD =     "bunker_CD";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STACKS, bunkeringStacks);
        bundle.put(BUNKER_TURNS, bunkeringTurns);
        bundle.put(BUNKER_CD, bunkeringCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bunkeringStacks = bundle.getInt(STACKS);
        bunkeringTurns = bundle.getInt(BUNKER_TURNS);
        bunkeringCooldown = bundle.getInt(BUNKER_CD);
        if (bunkeringStacks > 0 && bunkeringTurns <= 0){
            ActionIndicator.setAction(this);
        }
        attackedLastTurn = false;
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.USE_BUNKER;
    }

    @Override
    public Visual secondaryVisual() {
        BitmapText txt = new BitmapText(PixelScene.pixelFont);
        txt.text( Float.toString(bunkeringStacks) );
        txt.hardlight(CharSprite.POSITIVE);
        txt.measure();
        return txt;
    }

    @Override
    public int indicatorColor() {
        return 0x660000;
    }

    @Override
    public void doAction() {
        bunkeringTurns = 2*bunkeringStacks;
        //cooldown is functionally 10+2*stacks when active effect ends
        bunkeringCooldown = 10 + 4*bunkeringStacks;
        Sample.INSTANCE.play(Assets.Sounds.MISS, 1f, 0.8f);
        target.sprite.emitter().burst(Speck.factory(Speck.JET), 5+ bunkeringStacks);
        SpellSprite.show(target, SpellSprite.BUNKER, 1, 1, 0);
        bunkeringStacks = 0;
        BuffIndicator.refreshHero();
        ActionIndicator.clearAction(this);
    }
}
