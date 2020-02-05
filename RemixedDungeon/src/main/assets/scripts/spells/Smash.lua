---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by mike.
--- DateTime: 21.03.19 22:57
---

local RPD = require "scripts/lib/commonClasses"

local spell = require "scripts/lib/spell"

return spell.init{
    desc  = function ()
        return {
            image         = 3,
            imageFile     = "spellsIcons/warrior.png",
            name          = "SmashSpell_Name",
            info          = "SmashSpell_Info",
            magicAffinity = "Combat",
            targetingType = "self",
            level         = 4,
            spellCost     = 10,
            cooldown      = 10,
            castTime      = 0.5
        }
    end,
    cast = function(self, spell, caster)
        local items = caster:getBelongings()

        if items.weapon == nil then
            RPD.glogn("SmashSpell_NeedWeapon")
            return false
        end

        local fist = RPD.topEffect(caster:getPos(),"smash_fist")
        fist:incY(-30);
        RPD.attachMoveTweener(fist, 0,30,0.5)

        local clip = RPD.bottomEffect(caster:getPos(),"smash_blast")
        clip:setScale(1.5,1.3)

        local function smash(cell)
            local victim = RPD.Actor:findChar(cell)
            if victim ~= nil then
                RPD.affectBuff(victim, RPD.Buffs.Vertigo, caster:skillLevel())
                local dmg = items.weapon:damageRoll(caster)
                dmg = victim:defenseProc(caster, dmg)
                victim:damage(dmg, caster)
            end
        end

	    RPD.playSound("smash")
        RPD.forCellsAround(caster:getPos(), smash)

        caster:getSprite():jump(30)

        return true
    end
}
