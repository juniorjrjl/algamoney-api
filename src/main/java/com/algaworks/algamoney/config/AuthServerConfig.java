package com.algaworks.algamoney.config;

import com.algaworks.algamoney.config.property.AlgamoneyApiProperty;
import com.algaworks.algamoney.security.UsuarioSistema;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
@Profile("oauth-security")
@AllArgsConstructor
public class AuthServerConfig {

    private final PasswordEncoder passwordEncoder;

    private final AlgamoneyApiProperty algamoneyApiProperty;

    @Bean
    public RegisteredClientRepository registeredClientRepository(){
        var angularClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("angular")
                .clientSecret("$2a$10$6CfxFlYTNc2kBkvhtD3xvOsR6Y7Z13MgFopcW.Ij8SdEbA22QWuVW")//@ngul@r0
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUris(uri -> uri.addAll(algamoneyApiProperty.getSeguranca().getRedirectsPermitidos()))
                .scope("read")
                .scope("write")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(24))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        var mobileClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("mobile")
                .clientSecret(passwordEncoder.encode("m0bile"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUris(uri -> uri.addAll(algamoneyApiProperty.getSeguranca().getRedirectsPermitidos()))
                .scope("read")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(24))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(List.of(angularClient, mobileClient));
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerFilterChain(final HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtBuildCustomizer(){
        return context -> {
            UsernamePasswordAuthenticationToken authenticationToken = context.getPrincipal();
            UsuarioSistema usuarioSistema = (UsuarioSistema) authenticationToken.getPrincipal();

            Set<String> authorities = new HashSet<>();
            usuarioSistema.getAuthorities().forEach(a -> authorities.add(a.getAuthority()));

            context.getClaims().claim("nome", usuarioSistema.getUsuario().getNome());
            context.getClaims().claim("authorities", authorities);
        };
    }

    @Bean
    public JWKSet jwkSet() throws IOException, KeyStoreException, JOSEException {
        var file = new ClassPathResource("keystore/algamoney.jks").getFile();
        var keyStore = KeyStore.Builder
                .newInstance(file, new KeyStore.PasswordProtection("123456".toCharArray())).getKeyStore();
        var rsa = RSAKey.load(keyStore, "algamoney", "123456".toCharArray());
        return new JWKSet(rsa);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(final JWKSet jwkSet){
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(final JWKSource<SecurityContext> jwkSource){
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public ProviderSettings providerSettings(){
        return ProviderSettings.builder()
                .issuer(algamoneyApiProperty.getSeguranca().getAuthServerUrl())
                .build();
    }

}
