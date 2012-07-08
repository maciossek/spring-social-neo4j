package org.springframework.social.connect.neo4j;

import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Neo4jConnectionRepository implements ConnectionRepository {

	private final String userId;

	private final GraphDatabaseService graphDatabaseService;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;
	protected static Logger log = Logger.getLogger("controller");
	
	public Neo4jConnectionRepository(String userId, GraphDatabaseService graphDatabaseService,
			ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		this.userId = userId;
		this.graphDatabaseService = graphDatabaseService;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
		
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		Transaction tx = graphDatabaseService.beginTx();
		try
		{
			Node refNode = graphDatabaseService.getReferenceNode();
		    tx.success();
		}
		finally
		{
		    tx.finish();
		}
		
		ExecutionEngine engine = new ExecutionEngine( graphDatabaseService );
		ExecutionResult result = engine.execute( "start n=node(1) return n, n.password" );
		log.debug("\n" + result);
		
		return connections;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		// TODO Auto-generated method stub
		return null;
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

}
