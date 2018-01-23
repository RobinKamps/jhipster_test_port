package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SocialUserConnection;
import org.springframework.social.connect.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomSocialUsersConnectionRepository implements UsersConnectionRepository {

    private SocialUserConnectionRepository socialUserConnectionRepository;

    private ConnectionFactoryLocator connectionFactoryLocator;

    public CustomSocialUsersConnectionRepository(SocialUserConnectionRepository socialUserConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator) {
        this.socialUserConnectionRepository = socialUserConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<SocialUserConnection> socialUserConnections =
            socialUserConnectionRepository.findAllByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
        return socialUserConnections.stream()
            .map(socialConnection -> socialConnection.getUserId().toString())
            .collect(Collectors.toList());
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        List<SocialUserConnection> socialUserConnections =
            socialUserConnectionRepository.findAllByProviderIdAndProviderUserIdIn(providerId, providerUserIds.stream().map(Long::parseLong).collect(Collectors.toSet()));
        return socialUserConnections.stream()
            .map((connection) -> connection.getUserId().toString())
            .collect(Collectors.toSet());
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new CustomSocialConnectionRepository(Long.parseLong(userId), socialUserConnectionRepository, connectionFactoryLocator);
    }
}
