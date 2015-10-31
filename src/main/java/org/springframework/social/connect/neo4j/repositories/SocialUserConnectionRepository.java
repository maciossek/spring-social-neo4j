package org.springframework.social.connect.neo4j.repositories;

import org.neo4j.ogm.cypher.query.SortOrder;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SWijerathna on 10/7/2015.
 */
@Repository
public interface SocialUserConnectionRepository {

    java.util.Collection<SocialUserConnection> findByUserId(String userId, SortOrder sortOrder);

    java.util.Collection<SocialUserConnection> findByUserIdAndProviderId(String userId, String providerId, SortOrder sortOrder);

    Iterable<SocialUserConnection> findByUserIdAndProviderIdAndProviderUserIdIn(String userId, String providerId, List<String> value, SortOrder sortOrder);

    SocialUserConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    void deleteByUserIdAndProviderId(String userId, String providerId);

    void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    java.util.Collection<SocialUserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);

    Iterable<SocialUserConnection> findByProviderIdAndProviderUserIdIn(String providerId, List<String> providerUserIds);

    void save(SocialUserConnection socialUserConnection);
}
