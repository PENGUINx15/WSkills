package me.penguinx13.wSkills.skills.mining;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.skills.mining.effects.DoubleDrop;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MiningSkill implements Skill {

    private final Map<MiningEffectID, SkillEffect<MiningEffectID>> effects = new EnumMap<>(MiningEffectID.class);

    public MiningSkill() {
        effects.put(MiningEffectID.DOUBLE_DROP, new DoubleDrop());
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
    public List<SkillEffect> getEffects() {
        return effects;
    }
}
