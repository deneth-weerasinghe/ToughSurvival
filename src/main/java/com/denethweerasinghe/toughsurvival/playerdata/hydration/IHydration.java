package com.denethweerasinghe.toughsurvival.playerdata.hydration;

public interface IHydration {

    void setHydration(int value);
    int getHydration();
    void setDecayFactor(float value);
    float getDecayFactor();
    void applyDecay();
}
