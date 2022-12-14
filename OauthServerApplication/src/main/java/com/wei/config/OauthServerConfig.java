package com.wei.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Qualifier("userServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
 
 
    //???????????????????????????????????????
    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    //token????????????
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
 
    //????????????????????????
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }
 
    //???????????????????????????
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    //?????????????????????????????????
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //?????????????????????
        clients.withClientDetails(clientDetailsService());
 
//         ?????????????????????
//        clients.inMemory()
//                .withClient("baidu")
//                .secret(passwordEncoder.encode("12345"))
//                .resourceIds("product_api")
//                .authorizedGrantTypes(
//                        "authorization_code",
//                        "password",
//                        "client_credentials",
//                        "implicit",
//                        "refresh_token"
//                )// ???client????????????????????? authorization_code,password,refresh_token,implicit,client_credentials
//                .scopes("read", "write")// ?????????????????????
//                .autoApprove(false)
//                //????????????????????????
//                .redirectUris("http://www.baidu.com");
    }
 
    //??????token?????????
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients()    //??????form?????????????????????,?????????????????????client_id???client_secret??????token
                .checkTokenAccess("isAuthenticated()")     //??????????????????token??????
                .tokenKeyAccess("permitAll()")            // ??????token?????????????????????
                .passwordEncoder(passwordEncoder);
    }
 
    //OAuth2??????????????????
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .approvalStore(approvalStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService);
    }
}