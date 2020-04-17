package com.cointrendnotifier.android.api.dtos;

public class PreferenceDto {
    private String baseAssetName;
    private String quoteAssetName;

    public PreferenceDto(String baseAssetName, String quoteAssetName) {
        this.baseAssetName = baseAssetName;
        this.quoteAssetName = quoteAssetName;
    }

    public String getBaseAssetName() {
        return baseAssetName;
    }

    public String getQuoteAssetName() {
        return quoteAssetName;
    }
}
