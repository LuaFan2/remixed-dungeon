--
-- User: mike
-- Date: 26.05.2018
-- Time: 21:32
-- This file is part of Remixed Pixel Dungeon.
--

local RPD = require "scripts/lib/commonClasses"

local item = require "scripts/lib/item"


return item.init{
    desc  = function ()
        return {
            image     = 15,
            imageFile = "items/food.png",
            name      = "rotten fish",
            info      = "Rotten fish, smells terrible",
            stackable = true
        }
    end,
    actions = function() return {RPD.Actions.eat} end,
    execute = function(self, item, hero, action)
        if action == RPD.Actions.eat then
            RPD.Buffs.Buff:affect(hero, RPD.Buffs.Poison):set(2*math.random(1, hero:lvl()))
            hero:eat(item,RPD.Buffs.Hunger.HUNGRY / 4,"disgusting!")
        end
    end
}
