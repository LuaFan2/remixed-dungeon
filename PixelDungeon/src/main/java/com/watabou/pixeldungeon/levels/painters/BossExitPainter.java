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
package com.watabou.pixeldungeon.levels.painters;

import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Terrain;

public class BossExitPainter extends ExitPainter {

	public static void paint(Level level, Room room) {

		fill(level, room, 1, Terrain.EMPTY);

		for (Room.Door door : room.connected.values()) {
			door.set(Room.Door.Type.REGULAR);
		}

		int centerX = (room.left + room.right) / 2;
		int centerY = (room.top  + room.bottom) / 2;

		set(level,centerX-1,centerY,Terrain.WALL_DECO);
		set(level,centerX+1,centerY,Terrain.WALL_DECO);

		set(level,centerX-1, centerY-1,Terrain.WALL);
		set(level,centerX,   centerY-1,Terrain.WALL);
		set(level,centerX+1, centerY-1,Terrain.WALL);


		set(level,centerX-1, centerY+1,Terrain.WATER);
		set(level,centerX,   centerY+1,Terrain.WATER);
		set(level,centerX+1, centerY+1,Terrain.WATER);

		level.setExit(level.cell(centerX,centerY), 0);
		set(level, level.getExit(0), Terrain.LOCKED_EXIT);
	}
}
