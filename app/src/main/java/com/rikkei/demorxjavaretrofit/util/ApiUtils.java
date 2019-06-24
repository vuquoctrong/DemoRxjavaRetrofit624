package com.rikkei.demorxjavaretrofit.util;

import com.rikkei.demorxjavaretrofit.data.network.RetrofitClient;
import com.rikkei.demorxjavaretrofit.data.network.SOService;

public class ApiUtils {
    public static final String BASE_URL = "https://api.github.com/search/";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
