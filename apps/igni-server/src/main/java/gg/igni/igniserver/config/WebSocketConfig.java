package gg.igni.igniserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/channel");
		config.setApplicationDestinationPrefixes("/app");
		//config.setUserDestinationPrefix("/user");
	}

  	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat-websocket").setAllowedOriginPatterns("*");//.withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration)
	{
		// registration.interceptors(getChannelInterceptor());
	}

	// @Bean
	// ChannelInterceptor getChannelInterceptor()
	// {
	// 	return new ChannelSubscribeInterceptor();
	// }
}
