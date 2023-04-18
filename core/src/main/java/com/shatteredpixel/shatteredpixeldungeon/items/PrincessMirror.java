package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WinterStorm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Peretoria;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PrincessMirror extends Item {

    {
        image = ItemSpriteSheet.PRINCESS_MIRROR;

        defaultAction = AC_USE;
        usesTargeting = false;

        bones = false;
        unique = true;
    }

    private static final String AC_USE = "USE";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_USE );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (hero.buff(PrincessMirrorCooldown.class) != null) {
            GLog.w(Messages.get(this, "cooldown"));
        } else {
            if (hero.subClass == HeroSubClass.PERETORIA) {
                spawnGuard(curUser, 1);
                if (hero.hasTalent(Talent.HIGH_LEGION) && hero.pointsInTalent(Talent.HIGH_LEGION) >= 2) {
                    spawnGuard(curUser, 1);
                }
                curUser.sprite.operate(curUser.pos);
                Buff.affect(curUser, PrincessMirrorCooldown.class, 50);
            } else if (hero.subClass == HeroSubClass.VALKYRIE) {
                Buff.affect(curUser, PrincessMirrorCooldown.class, PrincessMirrorCooldown.DURATION);
            } else if (hero.subClass == HeroSubClass.WINTERSTORM) {
                Buff.affect(curUser, WinterStorm.class, WinterStorm.DURATION);
                Buff.affect(curUser, PrincessMirrorCooldown.class, 150);
            } else {
                new ScrollOfMirrorImage();
                ScrollOfMirrorImage.spawnImages(curUser, 1);
                curUser.sprite.operate(curUser.pos);
                Buff.affect(curUser, PrincessMirrorCooldown.class, PrincessMirrorCooldown.DURATION);
            }
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            curUser.spendAndNext(Actor.TICK);
        }

    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return -1;
    }

    public static int spawnGuard( Hero hero, int nImages ){

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                respawnPoints.add( p );
            }
        }

        int spawned = 0;
        while (nImages > 0 && respawnPoints.size() > 0) {
            int index = Random.index( respawnPoints );

            Peretoria mob = new Peretoria();
            mob.duplicate( hero );
            GameScene.add( mob );
            ScrollOfTeleportation.appear( mob, respawnPoints.get( index ) );

            respawnPoints.remove( index );
            nImages--;
            spawned++;
        }

        return spawned;
    }

    public static class PrincessMirrorCooldown extends FlavourBuff {

        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        public static final float DURATION = 30f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xDFFF40);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }

}
