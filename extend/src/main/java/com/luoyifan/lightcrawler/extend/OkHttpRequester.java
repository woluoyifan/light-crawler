package com.luoyifan.lightcrawler.extend;

import com.luoyifan.lightcrawler.core.model.Page;
import com.luoyifan.lightcrawler.core.model.Seed;
import com.luoyifan.lightcrawler.core.processor.Requester;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author EvanLuo
 * @date 2019/5/11 14:11
 */
public class OkHttpRequester implements Requester {
    private OkHttpClient httpClient = new OkHttpClient();

    @Override
    public Page request(Seed seed) throws IOException {
        Request request = new Request.Builder()
                .url(seed.getUrl())
                .build();
        Call call = httpClient.newCall(request);
        try (Response response = call.execute()) {
            Page page = new Page();
            if(response.code() == 200 && response.body()!=null){
                return new Page(response.body().bytes());
            }
            page.setCode(response.code());
            return page;
        }
    }
}
