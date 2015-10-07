package org.springframework.social.connect.neo4j.repositories;

import org.springframework.social.connect.*;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;

import java.util.*;

/**
 * Created by SWijerathna on 10/7/2015.
 */
public class Neo4jUserConnectionRepository implements UsersConnectionRepository {

    private SocialUserConnectionRepository repository;

    private ConnectionFactoryLocator connectionFactoryLocator;

    private ConnectionSignUp connectionSignUp;

    private String tablePrefix = "";

    public Neo4jUserConnectionRepository(SocialUserConnectionRepository socialUserConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator) {

        this.repository = socialUserConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    /**
     * The command to execute to create a new local user profile in the event no user id could be mapped to a connection.
     * Allows for implicitly creating a user profile from connection data during a provider sign-in attempt.
     * Defaults to null, indicating explicit sign-up will be required to complete the provider sign-in attempt.
     * @param connectionSignUp a {@link ConnectionSignUp} object
     * @see #findUserIdsWithConnection(Connection)
     */
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    /**
     * Sets a table name prefix. This will be prefixed to all the table names before queries are executed. Defaults to "".
     * This is can be used to qualify the table name with a schema or to distinguish Spring Social tables from other application tables.
     * @param tablePrefix the tablePrefix to set
     */
    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }


    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();

        List<SocialUserConnection> dbCons = repository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());

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
        List<SocialUserConnection> dbCons = repository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
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
