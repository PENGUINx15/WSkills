package me.penguinx13.wSkills.skills.mining;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.skills.mining.effects.DoubleDrop;
import me.penguinx13.wSkills.skills.mining.effects.DurabilitySave;
import me.penguinx13.wSkills.skills.mining.effects.ExtraXp;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class MiningSkill implements Skill {

    private final Map<MiningEffectID, SkillEffect<MiningEffectID>> effects = new EnumMap<>(MiningEffectID.class);

    public MiningSkill() {
        effects.put(MiningEffectID.DOUBLE_DROP, new DoubleDrop());
        effects.put(MiningEffectID.DURABILITY_SAVE, new DurabilitySave());
        effects.put(MiningEffectID.EXTRA_XP, new ExtraXp());
    }

    public SkillEffect<MiningEffectID> getEffect(MiningEffectID id) {
        return effects.get(id);
    }
    @Override
    public SkillID getType() {
        return SkillID.MINING;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public Collection<? extends SkillEffect<?>> getEffects() {
        return effects.values();
    }
}
