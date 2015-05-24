package com.elzup.pictter.pictter.twitter;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.internal.TwitterApi;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

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
    @GET("1.1/application/rate_limit_status")
    void getLimit(Callback<Response> cb);
}
