package org.springframework.social.connect.neo4j.repositories;

import org.neo4j.ogm.cypher.query.SortOrder;
import org.springframework.social.connect.neo4j.domain.UserConnection;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SWijerathna on 10/7/2015.
 */
@Repository
public interface OgmUserConnectionRepository {

    java.util.Collection<UserConnection> findByUserId(String userId, SortOrder sortOrder);

    java.util.Collection<UserConnection> findByUserIdAndProviderId(String userId, String providerId, SortOrder sortOrder);

    Iterable<UserConnection> findByUserIdAndProviderIdAndProviderUserIdIn(String userId, String providerId, List<String> value, SortOrder sortOrder);

    UserConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    void deleteByUserIdAndProviderId(String userId, String providerId);

    void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    java.util.Collection<UserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);

    Iterable<UserConnection> findByProviderIdAndProviderUserIdIn(String providerId, List<String> providerUserIds);

    void save(UserConnection userConnection);
}
