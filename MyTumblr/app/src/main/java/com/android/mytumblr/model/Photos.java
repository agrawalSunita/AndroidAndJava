package com.android.mytumblr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sunita on 8/22/2015.
 */
public class Photos {

    @SerializedName("alt_sizes")
    private List<AltSizesPhoto> altSizes;

    public List<AltSizesPhoto> getAltSizes() {
        return altSizes;
    }

    public void setAltSizes(List<AltSizesPhoto> altSizes) {
        this.altSizes = altSizes;
    }
}
