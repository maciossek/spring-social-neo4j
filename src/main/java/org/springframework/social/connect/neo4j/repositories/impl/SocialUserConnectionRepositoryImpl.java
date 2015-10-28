package org.springframework.social.connect.neo4j.repositories.impl;


import org.neo4j.ogm.session.Session;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.social.connect.neo4j.repositories.SocialUserConnectionRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by SWijerathna on 10/28/2015.
 */
public class SocialUserConnectionRepositoryImpl implements SocialUserConnectionRepository {

    Session session;

    public SocialUserConnectionRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public List<SocialUserConnection> findByUserId(String userId, String sort, int i) {


        return null;
    }

    @Override
    public List<SocialUserConnection> findByUserIdAndProviderId(String userId, String s, String sort, int i) {
        return null;
    }

    @Override
    public List<SocialUserConnection> findByUserIdAndProviderIdAndProviderUserIdIn(String userId, String providerId, List<String> value, String sort, int i) {
        return null;
    }

    @Override
    public SocialUserConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId) {
        return null;
    }

    @Override
    public void deleteByUserIdAndProviderId(String userId, String providerId) {

    }

    @Override
    public void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId) {

    }

    @Override
    public List<SocialUserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        return null;
    }

    @Override
    public List<SocialUserConnection> findByProviderIdAndProviderUserIdIn(String providerId, Set<String> providerUserIds) {
        return null;
    }

    @Override
    public void save(SocialUserConnection socialUserConnection) {

        session.save(socialUserConnection);
    }
}
