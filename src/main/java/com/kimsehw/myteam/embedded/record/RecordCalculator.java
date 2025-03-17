package com.kimsehw.myteam.embedded.record;

public interface RecordCalculator {
    default String calcWinRate(int wins, int loses, int draws) {
        if (wins + loses + draws == 0) {
            return "기록 없음";
        }
        return String.format("%.2f", (((double) wins / (wins + loses + draws)) * 100));
    }
}
