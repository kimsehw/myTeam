package com.kimsehw.myteam.embedded.record;

public interface RecordCalculator {
    default String calcWinRate(int wins, int loses) {
        if (wins + loses == 0) {
            return "기록 없음";
        }
        return String.format("%.2f", (((double) wins / (wins + loses)) * 100));
    }
}
