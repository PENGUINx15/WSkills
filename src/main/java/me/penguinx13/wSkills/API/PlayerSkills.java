package me.penguinx13.wSkills.API;

import java.util.EnumMap;
import java.util.Map;

public class PlayerSkills {

    private final Map<SkillType, Integer> skills = new EnumMap<>(SkillType.class);

    public int getLevel(SkillType type) {
        return skills.getOrDefault(type, 0);
    }

    public void setLevel(SkillType type, int level) {
        skills.put(type, level);
    }
}
