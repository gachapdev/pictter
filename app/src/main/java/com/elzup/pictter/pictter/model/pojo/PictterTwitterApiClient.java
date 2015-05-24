package com.elzup.pictter.pictter.model.pojo;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;

import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by hiro on 5/25/15.
 */
public class PictterTwitterApiClient extends TwitterApiClient {
    public PictterTwitterApiClient(Session session) {
        super(session);
    }

    public RateLimitService getRateLimitService() {
        return getService(RateLimitService.class);
    }

}
interface RateLimitService {
    @GET("/1.1/application/rate_limit_status.json")

    void getLimit(Callback<Response> cb);
}
