package com.moneyminder.moneyminderexpenses.feignClients;

import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = AuthUtils.getToken();

        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
