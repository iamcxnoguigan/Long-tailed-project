package com.l2l.contextsharing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .simpTypeMatchers(SimpMessageType.CONNECT , SimpMessageType.CONNECT_ACK, SimpMessageType.DISCONNECT, SimpMessageType.UNSUBSCRIBE , SimpMessageType.SUBSCRIBE).permitAll()
            .simpDestMatchers("/app/**").permitAll()
            .simpDestMatchers("/user/**").authenticated()
            .simpSubscribeDestMatchers("/topic/**", "/queue/**").permitAll()
            .anyMessage().denyAll();
    }

    /**
     * Disables CSRF for Websockets.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
