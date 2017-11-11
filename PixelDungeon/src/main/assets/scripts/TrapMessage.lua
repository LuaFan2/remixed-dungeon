--
-- User: mike
-- Date: 04.11.2017
-- Time: 22:26
-- This file is part of Remixed Pixel Dungeon.
--
local RPD = require "scripts/commonClasses"

local trap = require"scripts/TrapCommon"

return trap.init(
    function (cell, char, data)
        local wnd = luajava.newInstance("com.watabou.pixeldungeon.windows.WndMessage",data)
        RPD.GameScene:show(wnd)
    end
)