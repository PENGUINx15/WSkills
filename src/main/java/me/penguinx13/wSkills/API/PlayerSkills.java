package me.penguinx13.wSkills.API;

import java.util.EnumMap;
import java.util.Map;

public class PlayerSkills {

    private final Map<SkillID, Integer> skills = new EnumMap<>(SkillID.class);
    private final Map<SkillID, Integer> skillXp = new EnumMap<>(SkillID.class);

    public int getLevel(SkillID type) {
        return skills.getOrDefault(type, 0);
    }

    public void setLevel(SkillID type, int level) {
        skills.put(type, level);
    }

    public int getXp(SkillID type) {
        return skillXp.getOrDefault(type, 0);
    }

    public void setXp(SkillID type, int xp) {
        skillXp.put(type, xp);
    }
}
