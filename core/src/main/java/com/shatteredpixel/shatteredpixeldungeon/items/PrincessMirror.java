package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WinterStorm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Peretoria;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
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
                if (hero.hasTalent(Talent.HIGH_LEGION) && hero.pointsInTalent(Talent.HIGH_LEGION) >= 3) {
                    spawnGuard(curUser, 1);
                }
                curUser.sprite.operate(curUser.pos);
                if (hero.hasTalent(Talent.MAGIC_MIRROR) && hero.buff(Talent.MagicMirrorCoolDown.class) != null) {
                    Buff.affect(curUser, MindVision.class, 1+hero.pointsInTalent(Talent.MAGIC_MIRROR));
                    Buff.affect(curUser, Talent.MagicMirrorCoolDown.class, 40-10*hero.pointsInTalent(Talent.MAGIC_MIRROR));
                }
                Buff.affect(curUser, PrincessMirrorCooldown.class, 50);
                curUser.spendAndNext(Actor.TICK);
            } else if (hero.subClass == HeroSubClass.VALKYRIE) {
                usesTargeting = true;
                curUser = hero;
                GameScene.selectCell(targeter);
            } else if (hero.subClass == HeroSubClass.WINTERSTORM) {
                Buff.affect(curUser, WinterStorm.class, WinterStorm.DURATION + 4*Dungeon.hero.pointsInTalent(Talent.AMPLIFIED_GENERATOR));
                if (hero.hasTalent(Talent.MAGIC_MIRROR) && hero.buff(Talent.MagicMirrorCoolDown.class) != null) {
                    Buff.affect(curUser, MindVision.class, 1+hero.pointsInTalent(Talent.MAGIC_MIRROR));
                    Buff.affect(curUser, Talent.MagicMirrorCoolDown.class, 40-10*hero.pointsInTalent(Talent.MAGIC_MIRROR));
                }
                if (hero.hasTalent(Talent.IMPULSE_STRIKE) && hero.pointsInTalent(Talent.IMPULSE_STRIKE) >= 2) {
                    Buff.affect(curUser, Adrenaline.class, 5f);
                }
                Buff.affect(curUser, PrincessMirrorCooldown.class, 100-5*Dungeon.hero.pointsInTalent(Talent.AMPLIFIED_GENERATOR));
            } else {
                new ScrollOfMirrorImage();
                ScrollOfMirrorImage.spawnImages(curUser, 1);
                curUser.sprite.operate(curUser.pos);
                if (hero.hasTalent(Talent.MAGIC_MIRROR) && hero.buff(Talent.MagicMirrorCoolDown.class) != null) {
                    Buff.affect(curUser, MindVision.class, 1+hero.pointsInTalent(Talent.MAGIC_MIRROR));
                    Buff.affect(curUser, Talent.MagicMirrorCoolDown.class, 40-10*hero.pointsInTalent(Talent.MAGIC_MIRROR));
                }
                Buff.affect(curUser, PrincessMirrorCooldown.class, PrincessMirrorCooldown.DURATION);
                curUser.spendAndNext(Actor.TICK);
            }
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
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

    @Override
    public String desc() {
        String desc = super.desc();

        desc += "\n\n";
        if (Dungeon.hero.subClass == HeroSubClass.PERETORIA)
            desc += Messages.get(this, "desc_2");
        else if (Dungeon.hero.subClass == HeroSubClass.VALKYRIE)
            desc += Messages.get(this, "desc_3");
        else if (Dungeon.hero.subClass == HeroSubClass.WINTERSTORM)
            desc += Messages.get(this, "desc_4");
        else
            desc += Messages.get(this, "desc_1");

        return desc;
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
            GameScene.add( mob );
            ScrollOfTeleportation.appear( mob, respawnPoints.get( index ) );

            respawnPoints.remove( index );
            nImages--;
            spawned++;
            if (hero.hasTalent(Talent.HIGH_LEGION) && hero.pointsInTalent(Talent.HIGH_LEGION) >= 2)
                Buff.affect(mob, Adrenaline.class, 3f);
        }

        return spawned;
    }

    private CellSelector.Listener targeter = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                if (target == curUser.pos) {
                    GLog.w(Messages.get(this, "cant_aim"));
                } else {
                    Ballistica beam = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
                    Char ch = Actor.findChar(beam.collisionPos);
                    if (ch != null) {
                        ch.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                        Sample.INSTANCE.playDelayed(Assets.Sounds.LULLABY, 0.1f);

                        if (!ch.isImmune(ScrollOfSirensSong.Enthralled.class) && ch.buff(Charm.class) != null) {
                            AllyBuff.affectAndLoot((Mob) ch, curUser, ScrollOfSirensSong.Enthralled.class);
                        } else if (ch.isImmune(ScrollOfSirensSong.Enthralled.class) && Dungeon.hero.hasTalent(Talent.MIND_BREAKER)) {
                            Buff.affect(ch, Slow.class, 3 * Dungeon.hero.pointsInTalent(Talent.MIND_BREAKER));
                        } else {
                            Buff.affect(ch, Charm.class, Charm.DURATION).object = curUser.id();
                        }
                        ch.sprite.centerEmitter().burst(Speck.factory(Speck.HEART), 10);
                        if (Dungeon.hero.hasTalent(Talent.MAGIC_MIRROR) && Dungeon.hero.buff(Talent.MagicMirrorCoolDown.class) != null) {
                            Buff.affect(curUser, MindVision.class, 1 + Dungeon.hero.pointsInTalent(Talent.MAGIC_MIRROR));
                            Buff.affect(curUser, Talent.MagicMirrorCoolDown.class, 40 - 10 * Dungeon.hero.pointsInTalent(Talent.MAGIC_MIRROR));
                        }
                        Buff.affect(curUser, PrincessMirrorCooldown.class, 20 - 4 * Dungeon.hero.pointsInTalent(Talent.SPIRIT_CONVERSION));
                        curUser.spendAndNext(Actor.TICK);
                    }
                    curUser.sprite.zap(target);
                    curUser.sprite.parent.add(new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(ScrollOfSirensSong.class, "prompt");
        }
    };

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
