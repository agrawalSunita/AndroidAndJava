package com.android.mytumblr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sunita on 8/22/2015.
 */
public class ServerResult {
    @SerializedName("response")
    private Response resposne;

    public Response getResposne() {
        return resposne;
    }

    public void setResposne(Response resposne) {
        this.resposne = resposne;
    }
}
