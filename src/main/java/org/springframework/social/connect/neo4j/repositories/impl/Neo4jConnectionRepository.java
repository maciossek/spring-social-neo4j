package org.springframework.social.connect.neo4j.repositories.impl;

import org.neo4j.ogm.cypher.query.SortOrder;
import org.springframework.social.connect.*;
import org.springframework.social.connect.neo4j.converters.ConnectionConverter;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.social.connect.neo4j.repositories.SocialUserConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
* Created by SWijerathna on 10/7/2015.
*/
public class Neo4jConnectionRepository implements ConnectionRepository{

    private final String userId;

    private final SocialUserConnectionRepository repository;

    private final ConnectionFactoryLocator connectionFactoryLocator;

    public Neo4jConnectionRepository(String userId, SocialUserConnectionRepository socialUserConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator) {
        this.userId = userId;
        this.repository = socialUserConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {

        SortOrder sort = new SortOrder().add(SortOrder.Direction.ASC, "providerId").add(SortOrder.Direction.ASC,"rank");

        Collection<SocialUserConnection> dbConnections = repository.findByUserId(userId, sort);

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();

        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
        }

        for(SocialUserConnection dbCon:dbConnections) {
            ConnectionData conData = ConnectionConverter.toConnectionData(dbCon);
            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(conData.getProviderId());
            Connection<?> connection = connectionFactory.createConnection(conData);

            String providerId = connection.getKey().getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, connection);
        }

        return connections;
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {

        SortOrder sort = new SortOrder().add(SortOrder.Direction.ASC, "rank");
        Collection<SocialUserConnection> dbConnections = repository.findByUserIdAndProviderId(userId, providerId, sort);
        List<Connection<?>> connections = new ArrayList<Connection<?>>();
        for (SocialUserConnection dbCon:dbConnections) {
            ConnectionData conData = ConnectionConverter.toConnectionData(dbCon);
            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(conData.getProviderId());
            connections.add(connectionFactory.createConnection(conData));
        }

        return connections;
    }

    @Override
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {

        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }

        List<SocialUserConnection> dbConnections = new ArrayList<SocialUserConnection>();
        for (Iterator<Map.Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, List<String>> entry = it.next();
            String providerId = entry.getKey();
            SortOrder sort = new SortOrder();
            sort.add(SortOrder.Direction.ASC, "providerId").add(SortOrder.Direction.ASC, "rank");

            dbConnections.addAll(makeCollection(repository.findByUserIdAndProviderIdAndProviderUserIdIn(userId, providerId, entry.getValue(), sort)));
        }

        List<Connection<?>> resultList = getConnectionsListFromDbConnections(dbConnections);

        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();

        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            List<String> userIds = providerUsers.get(providerId);
            List<Connection<?>> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<Connection<?>>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            String providerUserId = connection.getKey().getProviderUserId();
            int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }

        return connectionsForUsers;
    }



    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {

        SocialUserConnection dbCon = repository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());

        if(dbCon == null) {
            throw new NoSuchConnectionException(connectionKey);
        }

        ConnectionData conData = ConnectionConverter.toConnectionData(dbCon);
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(conData.getProviderId());
        return connectionFactory.createConnection(conData);
    }

    @Override
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {

        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {

        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {

        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    @Override
    public void addConnection(Connection<?> connection) {

        ConnectionData data = connection.createData();
        Integer rank = 0;
        SortOrder sort = new SortOrder();
        sort.add(SortOrder.Direction.ASC,"rank");
        Collection<SocialUserConnection> existingCons = repository.findByUserIdAndProviderId(userId, data.getProviderId(), sort);
        if(existingCons.size()>0) {
            rank = existingCons.iterator().next().rank;
        }

        repository.save(ConnectionConverter.toSocialUserConnection(userId,rank,data));
    }

    @Override
    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        repository.save(ConnectionConverter.toSocialUserConnection(data,
                repository.findByUserIdAndProviderIdAndProviderUserId(userId,data.getProviderId(),data.getProviderUserId())));
    }

    @Override
    public void removeConnections(String providerId) {

        repository.deleteByUserIdAndProviderId(userId, providerId);
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {

        repository.deleteByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    private Connection<?> findPrimaryConnection(String providerId) {

        SortOrder sort = new SortOrder().add(SortOrder.Direction.ASC, "rank");

        List<Connection<?>> connections = getConnectionsListFromDbConnections(repository.findByUserIdAndProviderId(userId,providerId,sort));
        if (connections.size() > 0) {
            return connections.get(0);
        } else {
            return null;
        }
    }

    private List<Connection<?>> getConnectionsListFromDbConnections(Collection<SocialUserConnection> dbConnections) {

        List<Connection<?>> connections = new ArrayList<Connection<?>>();
        for(SocialUserConnection dbCon:dbConnections) {
            ConnectionData conData = ConnectionConverter.toConnectionData(dbCon);
            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(conData.getProviderId());
            connections.add(connectionFactory.createConnection(conData));
        }

        return connections;
    }

    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }

    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }
}
