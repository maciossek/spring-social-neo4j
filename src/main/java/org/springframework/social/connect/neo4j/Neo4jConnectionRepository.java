package org.springframework.social.connect.neo4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.index.IndexManager;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Neo4jConnectionRepository implements ConnectionRepository {

	private final String userId;

	private final GraphDatabaseService graphDb;

	private final ExecutionEngine engine;

	private final IndexManager index;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;
	protected static Logger log = Logger.getLogger("controller");

	public Neo4jConnectionRepository(String userId, GraphDatabaseService graphDatabaseService,
			ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		this.userId = userId;
		this.graphDb = graphDatabaseService;
		this.engine = new ExecutionEngine(graphDb);
		this.index = graphDb.index();
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;

	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		log.debug(userId);
		String shortQuery = "START n=node(1) CREATE (userconnection {userId:'maciossek'})-[:HAS_SOCIAL_CONNECTION]->n RETURN p";
		ExecutionResult result = engine.execute(shortQuery);
		log.debug("\n" + result);

		ExecutionResult results = engine.execute("START user=node:User('username:" + userId
				+ "') MATCH user-[:HAS_SOCIAL_CONNECTION]->connection RETURN connection");
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			connections.put(registeredProviderId, Collections.<Connection<?>> emptyList());
		}
		log.debug("\n" + results);

		List<Connection<?>> resultList = new ArrayList<Connection<?>>();

		for (Iterator<Map<String, Object>> it = results.iterator(); it.hasNext();) {
			log.debug(it);
			// resultList.add((Connection<?>) it.next());
		}

		log.debug(resultList);

		// map resultList to List<Connection<?>>
		// call iterator() to get a Map<String,Object> of result objects for
		// each match in the Cypher query
		// use them to create a Connection object that you add to a
		// List<Connection<?>>

		for (Connection<?> connection : resultList) {
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
		// get Nodes with startnode user with userId and relationship
		// HAS_SOCIAL_CONNECTIONs

		List<Connection<?>> connections = new ArrayList<Connection<?>>();

		return connections;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		List<?> connections = findConnections(getProviderId(apiType));
		return (List<Connection<A>>) connections;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(
			MultiValueMap<String, String> providerUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addConnection(Connection<?> connection) {
		try {
			// Map<String, Object> props = mapUserConnection(connection);
			// Map<String, Object> params = new HashMap<String, Object>();
			// // params.put( "userId", userId);
			// params.put("props", props);
			// String query = "START user=node:User(username='" + userId + "') "
			// + "CREATE ({props})-[:HAS_SOCIAL_CONNECTION]->user";
			// ExecutionResult result = engine.execute(query, params);

			String query = "START user=node:User(username={userId}) "
					+ "CREATE (userconnection {__type__:'{__type__}', providerId:'{providerId}',providerUserId:'{providerUserId}', "
					+ "displayName:'{displayName}', profileUrl:'{profileUrl}', imageUrl:'{imageUrl}', accessToken:'{accessToken}', "
					+ "secret:'{secret}', refreshToken:'{refreshToken}', expireTime:'{expireTime}'		})-[:xHAS_SOCIAL_CONNECTION]->user ";
					//+ "RETURN connection";
			ExecutionResult result = engine.execute(query, mapUserConnection(connection));
			log.debug(result);

		} catch (DuplicateKeyException e) {
			throw new DuplicateConnectionException(connection.getKey());
		}
	}

	@Override
	public void updateConnection(Connection<?> connection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConnections(String providerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConnection(ConnectionKey connectionKey) {
		// TODO Auto-generated method stub

	}

	private <A> String getProviderId(Class<A> apiType) {
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}

	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}

	private HashMap<String, Object> mapUserConnection(Connection<?> connection) {
		ConnectionData data = connection.createData();

		// TODO: Rank!?

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("__type__", "org.springframework.social.connect.neo4j.UserConnection");
		params.put("userId", userId);
		params.put("providerId", data.getProviderId());
		params.put("providerUserId", data.getProviderUserId());
		// params.put("rank", rank);
		params.put("displayName", data.getDisplayName());
		params.put("profileUrl", data.getProfileUrl());
		params.put("imageUrl", data.getImageUrl());
		params.put("accessToken", encrypt(data.getAccessToken()));
		params.put("secret", encrypt(data.getSecret()));
		params.put("refreshToken", encrypt(data.getRefreshToken()));
		params.put("expireTime", data.getExpireTime());
		log.debug(encrypt(data.getRefreshToken()));
		log.debug(encrypt(data.getSecret()));
		log.debug(encrypt(data.getAccessToken()));
		log.debug(data.getExpireTime());
		log.debug(data.getImageUrl());
		log.debug(data.getProfileUrl());

		return (HashMap<String, Object>) params;
	}

}
