package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SocialUserConnection;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Social User Connection entity.
 */
public interface SocialUserConnectionRepository extends JpaRepository<SocialUserConnection, Long> {

    List<SocialUserConnection> findAllByProviderIdAndProviderUserId(String providerId, String providerUserId);

    List<SocialUserConnection> findAllByProviderIdAndProviderUserIdIn(String providerId, Set<String> providerUserIds);

    List<SocialUserConnection> findAllByUserIdOrderByProviderIdAscRankAsc(Long userId);

    List<SocialUserConnection> findAllByUserIdAndProviderIdOrderByRankAsc(Long userId, String providerId);

    List<SocialUserConnection> findAllByUserIdAndProviderIdAndProviderUserIdIn(Long userId, String providerId, List<String> provideUserId);

    SocialUserConnection findOneByUserIdAndProviderIdAndProviderUserId(Long userId, String providerId, String providerUserId);

    void deleteByUserIdAndProviderId(Long userId, String providerId);

    void deleteByUserIdAndProviderIdAndProviderUserId(Long userId, String providerId, String providerUserId);
}
