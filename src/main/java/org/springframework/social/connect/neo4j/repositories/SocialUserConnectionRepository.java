package org.springframework.social.connect.neo4j.repositories;

import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by SWijerathna on 10/7/2015.
 */
@Repository
public interface SocialUserConnectionRepository {

    List<SocialUserConnection> findByUserId(String userId, String sort, int i);

    List<SocialUserConnection> findByUserIdAndProviderId(String userId, String s, String sort, int i);

    List<SocialUserConnection> findByUserIdAndProviderIdAndProviderUserIdIn(String userId, String providerId, List<String> value, String sort, int i);

    SocialUserConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    void deleteByUserIdAndProviderId(String userId, String providerId);

    void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    List<SocialUserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);

    List<SocialUserConnection> findByProviderIdAndProviderUserIdIn(String providerId, Set<String> providerUserIds);

    void save(SocialUserConnection socialUserConnection);
}
