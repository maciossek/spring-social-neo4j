package org.springframework.social.connect.neo4j;

import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

public class Neo4jUsersConnectionRepository implements UsersConnectionRepository {

	private final GraphDatabaseService graphDatabaseService;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	private ConnectionSignUp connectionSignUp;

	public Neo4jUsersConnectionRepository(GraphDatabaseService graphDatabaseService,
			ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {

		this.graphDatabaseService = graphDatabaseService;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new Neo4jConnectionRepository(userId, graphDatabaseService, connectionFactoryLocator, textEncryptor);
	}

}
