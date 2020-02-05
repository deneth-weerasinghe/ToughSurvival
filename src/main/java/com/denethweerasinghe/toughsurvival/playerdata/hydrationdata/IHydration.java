package com.denethweerasinghe.toughsurvival.playerdata.hydrationdata;

public interface IHydration {

    void setHydration(int value);
    int getHydration();
    void setDecayFactor(float value);
    float getDecayFactor();
    void setDecayTimer(int value);
    void incrementTimer();
    int getDecayTimer();
    void setTimerEnd(int value);
    int getTimerEnd();
}
