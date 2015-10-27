package org.springframework.social.connect.neo4j.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by SWijerathna on 10/7/2015.
 */
@Repository
public interface SocialUserConnectionRepository extends GraphRepository<SocialUserConnection> {

    List<SocialUserConnection> findByUserId(String userId, Sort sort, int i);

    List<SocialUserConnection> findByUserIdAndProviderId(String userId, String s, Sort sort, int i);

    List<SocialUserConnection> findByUserIdAndProviderIdAndProviderUserIdIn(String userId, String providerId, List<String> value, Sort sort, int i);

    SocialUserConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    void deleteByUserIdAndProviderId(String userId, String providerId);

    void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);

    List<SocialUserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);

    List<SocialUserConnection> findByProviderIdAndProviderUserIdIn(String providerId, Set<String> providerUserIds);

}
