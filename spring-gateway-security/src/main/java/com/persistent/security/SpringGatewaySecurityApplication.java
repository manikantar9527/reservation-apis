package com.persistent.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.persistent.security.dto.ApiKey;
import com.persistent.security.util.AppConstants;


@SpringBootApplication
@EnableEurekaClient
public class SpringGatewaySecurityApplication {

	@PostConstruct
	public void initKeysToRedis() {
		List<ApiKey> apiKeys = new ArrayList<>();
		apiKeys.add(new ApiKey("343C-ED0B-4137-B27E", Stream
				.of(AppConstants.RESERVATION_SERVICE_KEY, AppConstants.BOOK_TICKET_SERVICE_KEY).collect(Collectors.toList())));
		apiKeys.add(new ApiKey("FA48-EF0C-427E-8CCF",
				Stream.of(AppConstants.BOOK_TICKET_SERVICE_KEY).collect(Collectors.toList())));
	}

	
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes().route(AppConstants.RESERVATION_SERVICE_KEY,
				r -> r.path("/api/reservation-service/**").filters(f -> f.stripPrefix(2)).uri("http://localhost:7071"))
				.route(AppConstants.BOOK_TICKET_SERVICE_KEY,
						r -> r.path("/api/book-ticket/**").filters(f -> f.stripPrefix(2)).uri("http://localhost:7072"))
				.build();
	}
	 
	/*
	 * @Bean public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	 * return builder.routes().route(AppConstants.STUDENT_SERVICE_KEY, r ->
	 * r.path("/students").filters(f -> f.circuitBreaker(cfg -> {})
	 * .rewritePath("/students",
	 * "/studentServiceFallBack").stripPrefix(2)).uri("http://localhost:7071"))
	 * .route(AppConstants.COURSE_SERVICE_KEY, r -> r.path("/api/course-service/**")
	 * .filters(f -> f.circuitBreaker(cfg -> {})
	 * .rewritePath("/api/student-service/**",
	 * "/courseServiceFallBack").stripPrefix(2)).uri("http://localhost:7072"))
	 * .build(); }
	 * 
	 * 
	 * @Bean RouteLocator routeLocator(RouteLocatorBuilder builder) { return
	 * builder.routes() .route(AppConstants.STUDENT_SERVICE_KEY, route -> route
	 * .path("/students") .filters(flt -> flt .circuitBreaker(cfg -> {})
	 * .rewritePath("/students", "/studentServiceFallBack") // See SlowCtrl below )
	 * .uri("http://localhost:7071") ).build(); }
	 * 
	 * @Bean public Customizer<ReactiveResilience4JCircuitBreakerFactory>
	 * slowConfig() { return factory -> factory .configure(bld -> bld
	 * .timeLimiterConfig(TimeLimiterConfig.custom() .timeoutDuration(ofSeconds(7))
	 * .build()), AppConstants.STUDENT_SERVICE_KEY ); }
	 * 
	 * @Bean ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory() { var
	 * factory = new ReactiveResilience4JCircuitBreakerFactory();
	 * slowConfig().customize(factory); return factory; }
	 */
		
		
	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringGatewaySecurityApplication.class, args);
	}

}
