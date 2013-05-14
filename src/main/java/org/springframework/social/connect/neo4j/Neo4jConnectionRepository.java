package org.springframework.social.connect.neo4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
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

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;
	protected static Logger log = Logger.getLogger("controller");

	public Neo4jConnectionRepository(String userId, GraphDatabaseService graphDatabaseService,
			ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		this.userId = userId;
		this.graphDb = graphDatabaseService;
		this.engine = new ExecutionEngine(graphDb);
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;

	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		ExecutionResult results = engine.execute("START user=node:User('username:" + userId
				+ "') MATCH user-[:HAS_SOCIAL_CONNECTION]->connection RETURN connection");

		List<Connection<?>> resultList = createResultList(results);

		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();

		for (String registeredProviderId : registeredProviderIds) {
			connections.put(registeredProviderId, Collections.<Connection<?>> emptyList());
		}

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
		// return jdbcTemplate.query(selectFromUserConnection() +
		// " where userId = ? and providerId = ? order by rank",
		// connectionMapper, userId, providerId);
		
		// get Nodes with startnode user with userId and relationship
		// HAS_SOCIAL_CONNECTION
		ExecutionResult results = engine.execute("START user=node:User('username:" + userId 
				+ ", providerId:"+ providerId
				+ "') MATCH user-[:HAS_SOCIAL_CONNECTION]->connection RETURN connection");
		List<Connection<?>> connections = createResultList(results);
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
			String query = "START user=node:User(username={userId}) "
					+ "CREATE (userconnection {providerId:{providerId},providerUserId:{providerUserId}, "
					+ "displayName:{displayName}, profileUrl:{profileUrl}, imageUrl:{imageUrl}, accessToken:{accessToken}, "
					+ "secret:{secret}, refreshToken:{refreshToken}, expireTime:{expireTime}})<-[:HAS_SOCIAL_CONNECTION]-user ";
			// + "RETURN connection";
			ExecutionResult result = engine.execute(query, mapUserConnectionParams(connection));
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

	private HashMap<String, Object> mapUserConnectionParams(Connection<?> connection) {
		ConnectionData data = connection.createData();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("userId", userId);
		params.put("providerId", data.getProviderId());
		params.put("providerUserId", data.getProviderUserId());
		params.put("displayName", data.getDisplayName());
		params.put("profileUrl", data.getProfileUrl());
		params.put("imageUrl", data.getImageUrl());
		params.put("accessToken", encrypt(data.getAccessToken()));
		params.put("secret", encrypt(data.getSecret()));
		params.put("refreshToken", encrypt(data.getRefreshToken()));
		params.put("expireTime", data.getExpireTime());

		// remove null entries, to work with Neo4j
		for (Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() == null) {
				entry.setValue("");
			}
		}

		return (HashMap<String, Object>) params;
	}

	private String decrypt(String encryptedText) {
		return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
	}

	private Long expireTime(long expireTime) {
		return expireTime == 0 ? null : expireTime;
	}

//	private List<Connection<?>> mapResultList(ExecutionResult results) {
//		// create resultList with Connections & convert Cypher query
//		List<Connection<?>> resultList = new LinkedList<Connection<?>>();
//
//		// NumberFormatException
//		Iterator<Node> userConnections = results.columnAs("connection");
//		while (userConnections.hasNext()) {
//			Node uc = userConnections.next();
//			ConnectionData connectionData = new ConnectionData(
//					(String) uc.getProperty("providerId"),
//					(String) uc.getProperty("providerUserId"),
//					(String) uc.getProperty("displayName"), 
//					(String) uc.getProperty("profileUrl"),
//					(String) uc.getProperty("imageUrl"),
//					decrypt((String) uc.getProperty("accessToken")),
//					decrypt((String) uc.getProperty("secret")),
//					decrypt((String) uc.getProperty("refreshToken")),
//					expireTime((Long) uc.getProperty("expireTime")));
//			ConnectionFactory<?> connectionFactory = connectionFactoryLocator
//					.getConnectionFactory(connectionData.getProviderId());
//			resultList.add(connectionFactory.createConnection(connectionData));
//		}
//		return resultList;
//	}
	
	private Connection<?> mapEntry(ConnectionData connectionData) {
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}
	
	private ConnectionData mapConnectionData(Node node) {
		return new ConnectionData((String) node.getProperty("providerId"),
				(String) node.getProperty("providerUserId"),
				(String) node.getProperty("displayName"), 
				(String) node.getProperty("profileUrl"),
				(String) node.getProperty("imageUrl"),
				decrypt((String) node.getProperty("accessToken")),
				decrypt((String) node.getProperty("secret")),
				decrypt((String) node.getProperty("refreshToken")),
				expireTime((Long) node.getProperty("expireTime")));
	}
	
	private List<Connection<?>> createResultList(ExecutionResult er) {
		List<Connection<?>> resultList = new LinkedList<Connection<?>>();
		Iterator<Node> userConnections = er.columnAs("connection");
		while (userConnections.hasNext()) {
			Node userConnectionNode = userConnections.next();
			resultList.add(mapEntry(mapConnectionData(userConnectionNode)));
		}
		
		
		return resultList;
	}

}
