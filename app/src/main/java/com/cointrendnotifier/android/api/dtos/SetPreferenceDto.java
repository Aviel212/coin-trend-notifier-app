package com.cointrendnotifier.android.api.dtos;

public class SetPreferenceDto extends PreferenceDto {
    private double probability;

    public SetPreferenceDto(String baseAssetName, String quoteAssetName, double probability) {
        super(baseAssetName, quoteAssetName);
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
