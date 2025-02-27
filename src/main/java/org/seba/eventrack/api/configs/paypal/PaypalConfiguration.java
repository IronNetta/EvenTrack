package org.seba.eventrack.api.configs.paypal;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfiguration {

    @Value("Ab-8ylUmSNo1KjL6DYh83RnbF1s_1iLkBWYtWrJe-_28jeUofpoQKImhohL4UXvaXGRVKTIvXFHkgHPl")
    private String clientId;

    @Value("EM6SfzGZ5qObMERmvHSNr0uruyjKq-plYU_qAarVD8iyCrGWyiM22gJlG2d_d6WzX10uRgb171Mw72Hg")
    private String clientSecret;

    @Value("sandbox")
    private String mode;

    @Bean
    public APIContext getAPIContext() {
        System.out.println("PayPal Config - Client ID: " + clientId);
        System.out.println("PayPal Config - Mode: " + mode);
        return new APIContext(clientId, clientSecret, mode);
    }
}
