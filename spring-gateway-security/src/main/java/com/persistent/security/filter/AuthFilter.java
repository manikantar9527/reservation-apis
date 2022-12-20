package com.persistent.security.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.persistent.security.dto.ApiKey;
import com.persistent.security.util.AppConstants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter , Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> apiKeyHeader=exchange.getRequest().getHeaders().get("gatewaykey");
        log.info("api key {} ",apiKeyHeader);
        Route route=exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String routeId=route!=null? route.getId() : null;

        if(routeId ==null || CollectionUtils.isEmpty(apiKeyHeader) || !isAuthorize(routeId, apiKeyHeader.get(0))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"you can't consume this service , Please validate your apikeys");
        }return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private boolean isAuthorize(String routeId,String apiKey){
    	ApiKey key=getKey(apiKey);
        if(key!=null){
            return key.getServices().contains(routeId);
        }else{
            return false;
        }
    }
    
    private List<ApiKey> getApiKeys() {
		List<ApiKey> apiKeys = new ArrayList<>();
		apiKeys.add(new ApiKey("343C-ED0B-4137-B27E", Stream
				.of(AppConstants.RESERVATION_SERVICE_KEY, AppConstants.BOOK_TICKET_SERVICE_KEY).collect(Collectors.toList())));
		apiKeys.add(new ApiKey("FA48-EF0C-427E-8CCF",
				Stream.of(AppConstants.BOOK_TICKET_SERVICE_KEY).collect(Collectors.toList())));
		return apiKeys;
	}

	private ApiKey getKey(String apiKey) {
		return getApiKeys().stream().filter(k -> apiKey.equals(k.getKey())).findFirst().orElse(null);
	}
}
