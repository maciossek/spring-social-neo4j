package org.springframework.social.connect.neo4j.repositories.impl;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.social.connect.*;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.social.connect.neo4j.repositories.SocialUserConnectionRepository;

import java.util.*;

/**
* Created by SWijerathna on 10/7/2015.
*/
public class Neo4jUserConnectionRepository implements UsersConnectionRepository {


    private ConnectionFactoryLocator connectionFactoryLocator;
    private SocialUserConnectionRepository repository;
    private ConnectionSignUp connectionSignUp;


    public Neo4jUserConnectionRepository(String neo4jServerUri, ConnectionFactoryLocator connectionFactoryLocator) {

        this.connectionFactoryLocator = connectionFactoryLocator;
        init(neo4jServerUri);
    }

    public void init(String serverUri){

        SessionFactory sessionFactory = new SessionFactory("org.springframework.social.connect.neo4j.domain");
        Session session = sessionFactory.openSession(serverUri);
        repository = new SocialUserConnectionRepositoryImpl(session);
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();

        Collection<SocialUserConnection> dbCons = repository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());

        if (dbCons.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null)
            {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }

        List<String> userIds = new ArrayList<String>();
        for(SocialUserConnection dbCon: dbCons){
            userIds.add(dbCon.userId);
        }
        return userIds;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {

        final Set<String> localUserIds = new HashSet<String>();
        List<String> providerUserIdsList = new ArrayList<String>();
        providerUserIdsList.addAll(providerUserIds);
        Iterable<SocialUserConnection> dbCons = repository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIdsList);
        for(SocialUserConnection dbCon:dbCons) {
            localUserIds.add(dbCon.userId);
        }
        return localUserIds;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {

        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        return new Neo4jConnectionRepository(userId,repository,connectionFactoryLocator);
    }


}
