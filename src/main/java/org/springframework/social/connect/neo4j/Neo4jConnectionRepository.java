package org.springframework.social.connect.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Neo4jConnectionRepository implements ConnectionRepository {

	private final String userId;

	private final GraphDatabaseService graphDb;

	private final IndexManager index;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;
	protected static Logger log = Logger.getLogger("controller");

	public Neo4jConnectionRepository(String userId, GraphDatabaseService graphDatabaseService,
			ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		this.userId = userId;
		this.graphDb = graphDatabaseService;
		this.index = graphDb.index();
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;

	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {

		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();

		// get nodes with HAS_SOCIAL_CONNECTION relationship
		// where query in cypher: http://docs.neo4j.org/chunked/milestone/query-where.html


		ExecutionEngine engine = new ExecutionEngine(graphDb);
		ExecutionResult result = engine.execute("START n=node(*), RETURN n");
		
		log.debug("\n" + result);

		return connections;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		// get Nodes with startnode user with userId and relationship HAS_SOCIAL_CONNECTIONs
		
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
		// TODO Auto-generated method stub

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

}
