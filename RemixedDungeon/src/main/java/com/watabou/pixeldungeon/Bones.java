/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon;

import com.nyrds.android.util.FileSystem;
import com.nyrds.pixeldungeon.utils.CharsList;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.rings.Artifact;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Bones {

	private static final String BONES_FILE	= "bones.dat";
	
	private static final String LEVEL	= "level";
	private static final String ITEM	= "item";
	
	private static int depth = -1;
	private static Item item;
	
	public static void leave() {
		
		item = CharsList.DUMMY_ITEM;

		switch (Random.Int( 4 )) {
		case 0:
			item = Dungeon.hero.getBelongings().weapon;
			break;
		case 1:
			item = Dungeon.hero.getBelongings().armor;
			break;
		case 2:
			item = Dungeon.hero.getBelongings().ring1;
			break;
		case 3:
			item = Dungeon.hero.getBelongings().ring2;
			break;
		}
		if (item == CharsList.DUMMY_ITEM || (item instanceof Artifact && !(item instanceof Ring))) {
			if (Dungeon.hero.gold() > 0) {
				item = new Gold( Random.IntRange( 1, Dungeon.hero.gold()) );
			} else {
				item = new Gold( 1 );
			}
		}
		
		depth = Dungeon.depth;
		
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, depth );
		bundle.put( ITEM, item );

		try {
			OutputStream output = FileSystem.getOutputStream(BONES_FILE);
			Bundle.write( bundle, output );
			output.close();
		} catch (IOException ignored) {

		}
	}
	
	public static Item get() {
		if (depth == -1) {
			try {
				InputStream input = Game.instance().openFileInput(BONES_FILE);
				Bundle bundle = Bundle.read(input);
				input.close();

				if (bundle.contains(LEVEL) && bundle.contains(ITEM)) {
					depth = bundle.getInt(LEVEL);
					item = (Item) bundle.get(ITEM);
					return get();
				}
			}  catch (Exception ignored) {
			}
			return CharsList.DUMMY_ITEM;
		} else {
			if (depth == Dungeon.depth) {
				Game.instance().deleteFile( BONES_FILE );
				depth = 0;
				
				if (!item.stackable) {
					item.setCursed(true);
					item.setCursedKnown(true);
					if (item.isUpgradable()) {
						int lvl = (Dungeon.depth - 1) * 3 / 5 + 1;
						if (lvl < item.level()) {
							item.degrade( item.level() - lvl );
						}
						item.setLevelKnown(false);
					}
				}
				
				if (item instanceof Ring) {
					((Ring)item).syncGem();
				}
				
				return item;
			} else {
				return CharsList.DUMMY_ITEM;
			}
		}
	}

	public static String getBonesFile() {
		return BONES_FILE;
	}
}
