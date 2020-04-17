package com.cointrendnotifier.android.api.dtos;

public class EventDto {
    private String _id;
    private double probability;
    private int firedAt;
    private String baseAssetName;
    private String quoteAssetName;

    public EventDto(String baseAssetName, String quoteAssetName, double probability, int firedAt, String _id) {
        this._id = _id;
        this.probability = probability;
        this.firedAt = firedAt;
        this.baseAssetName = baseAssetName;
        this.quoteAssetName = quoteAssetName;
    }

    public String get_id() {
        return _id;
    }

    public double getProbability() {
        return probability;
    }

    public int getFiredAt() {
        return firedAt;
    }

    public String getBaseAssetName() {
        return baseAssetName;
    }

    public String getQuoteAssetName() {
        return quoteAssetName;
    }
}
