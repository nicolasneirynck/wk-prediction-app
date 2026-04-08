package com.example.wk_prognose.util;

public enum MatchStage {
    GROUP_A("Group A"),
    GROUP_B("Group B"),
    GROUP_C("Group C"),
    GROUP_D("Group D"),
    GROUP_E("Group E"),
    GROUP_F("Group F"),
    GROUP_G("Group G"),
    GROUP_H("Group H"),
    GROUP_I("Group I"),
    GROUP_J("Group J"),
    GROUP_K("Group K"),
    GROUP_L("Group L"),
    ROUND_OF_16("Round of 16"),
    QUARTER_FINAL("Quarter-final"),
    SEMI_FINAL("Semi-final"),
    THIRD_PLACE("Third place"),
    FINAL("Final");

    private final String label;

    MatchStage(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
