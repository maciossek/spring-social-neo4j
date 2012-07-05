package org.springframework.social.connect.neo4j;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

public class Neo4jUsersConnectionRepository implements UsersConnectionRepository {
	private final ConnectionService neo4jService;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	private ConnectionSignUp connectionSignUp;

	public Neo4jUsersConnectionRepository(ConnectionService neo4jService,
			ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		
		this.neo4jService = neo4jService;
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
		// TODO Auto-generated method stub
		return null;
	}

}
