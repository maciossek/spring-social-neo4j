package org.springframework.social.connect.neo4j.repositories.impl;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import org.springframework.social.connect.neo4j.domain.UserConnection;
import org.springframework.social.connect.neo4j.repositories.OgmUserConnectionRepository;

import java.util.*;

/**
* Created by SWijerathna on 10/7/2015.
*/
public class Neo4jUsersConnectionRepository implements UsersConnectionRepository {


    private ConnectionFactoryLocator connectionFactoryLocator;
    private OgmUserConnectionRepository repository;
    private ConnectionSignUp connectionSignUp;
    private TextEncryptor textEncryptor;


    public Neo4jUsersConnectionRepository(String neo4jServerUri, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {

        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;

        init(neo4jServerUri);
    }

    public void init(String serverUri){

        SessionFactory sessionFactory = new SessionFactory("org.springframework.social.connect.neo4j.domain");
        Session session = sessionFactory.openSession(serverUri);
        repository = new OgmUserConnectionRepositoryImpl(session);
    }

    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();

        Collection<UserConnection> dbCons = repository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());

        if (dbCons.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null)
            {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }

        List<String> userIds = new ArrayList<String>();
        for(UserConnection dbCon: dbCons){
            userIds.add(dbCon.userId);
        }
        return userIds;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {

        final Set<String> localUserIds = new HashSet<String>();
        List<String> providerUserIdsList = new ArrayList<String>();
        providerUserIdsList.addAll(providerUserIds);
        Iterable<UserConnection> dbCons = repository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIdsList);
        for(UserConnection dbCon:dbCons) {
            localUserIds.add(dbCon.userId);
        }
        return localUserIds;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {

        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        return new Neo4jConnectionRepository(userId, repository, connectionFactoryLocator, textEncryptor);
    }


}
