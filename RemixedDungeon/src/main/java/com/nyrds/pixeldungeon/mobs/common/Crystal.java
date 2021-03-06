package com.nyrds.pixeldungeon.mobs.common;

import com.nyrds.pixeldungeon.items.common.WandOfShadowbolt;
import com.nyrds.pixeldungeon.mechanics.NamedEntityKind;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.ConfusionGas;
import com.watabou.pixeldungeon.actors.blobs.Darkness;
import com.watabou.pixeldungeon.actors.blobs.Foliage;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.wands.SimpleWand;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class Crystal extends MultiKindMob implements IDepthAdjustable, IZapper{

	static private int ctr = 0;

	{
		movable = false;
	}

	public Crystal() {
		adjustStats(Dungeon.depth);
		ensureWand();
	}

	static public Crystal makeShadowLordCrystal() {
		Crystal crystal = new Crystal();
		crystal.kind = 2;
		crystal.ensureWand();
		return crystal;
	}

	@NotNull
	private Wand ensureWand() {
		if (loot instanceof  Wand) {
			return (Wand) loot;
		}

		if(kind == 2) {
			lootChance = 0.12f;
			loot = new WandOfShadowbolt();
			((Wand) loot).upgrade(Dungeon.depth / 3);
			return (Wand) loot;
		}

		loot = SimpleWand.createRandomSimpleWand();
		((Wand) loot).upgrade(Dungeon.depth / 3);

		lootChance = 0.25f;
		return ((Wand) loot);
	}

	public void adjustStats(int depth) {
		kind = (ctr++) % 2;

		hp(ht(depth * 4 + 1));
		defenseSkill = depth * 2 + 1;
		exp = depth + 1;
		maxLvl = depth + 2;

		addImmunity(ScrollOfPsionicBlast.class);
		addImmunity(ToxicGas.class);
		addImmunity(Paralysis.class);
		addImmunity(ConfusionGas.class);
	}

	@Override
	public int getKind() {
		return kind;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(hp() / 2, ht() / 2);
	}

	@Override
	public boolean canAttack(@NotNull Char enemy) {
		return Ballistica.cast(getPos(), enemy.getPos(), false, true) == enemy.getPos();
	}

	@Override
	public int attackSkill(Char target) {
		if (kind < 2) {
			return 1000;
		} else {
			return 35;
		}
	}

	@Override
	public int dr() {
		return exp / 3;
	}



	@Override
	public boolean attack(@NotNull Char enemy) {
		zap(enemy);
		return true;
	}

	@Override
	public boolean getCloser(int target) {
		return false;
	}

	@Override
    public boolean getFurther(int target) {
		return false;
	}

	@Override
	public void die(NamedEntityKind cause) {
		int pos = getPos();

		if (Dungeon.level.map[pos] == Terrain.PEDESTAL) {
			Dungeon.level.set(pos, Terrain.EMBERS);
			int x, y;
			x = Dungeon.level.cellX(pos);
			y = Dungeon.level.cellY(pos);

			Dungeon.level.clearAreaFrom(Darkness.class, x - 2, y - 2, 5, 5);
			Dungeon.level.fillAreaWith(Foliage.class, x - 2, y - 2, 5, 5, 1);

			GameScene.updateMap();
		}
		super.die(cause);
	}

	@Override
	public boolean canBePet() {
		return false;
	}

	@Override
	protected int zapProc(@NotNull Char enemy, int damage) {
		ensureWand().mobWandUse(this, enemy.getPos());
		return 0;
	}
}
