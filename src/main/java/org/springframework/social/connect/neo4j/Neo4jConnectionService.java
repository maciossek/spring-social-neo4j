package org.springframework.social.connect.neo4j;

import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.MultiValueMap;

public class Neo4jConnectionService implements ConnectionService{
	
	private final Neo4jTemplate neoTpl;
	private final ConnectionConverter converter;
	
	public Neo4jConnectionService(Neo4jTemplate neoTpl, ConnectionConverter converter) {
		this.neoTpl = neoTpl;
		this.converter = converter;
	}

	@Override
	public int getMaxRank(String userId, String providerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void create(String userId, Connection<?> userConn, int rank) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String userId, Connection<?> userConn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String userId, ConnectionKey connectionKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String userId, String providerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection<?> getPrimaryConnection(String userId, String providerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection<?> getConnection(String userId, String providerId, String providerUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Connection<?>> getConnections(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Connection<?>> getConnections(String userId, String providerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Connection<?>> getConnections(String userId,
			MultiValueMap<String, String> providerUsers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getUserIds(String providerId, Set<String> providerUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUserIds(String providerId, String providerUserId) {
		// TODO Auto-generated method stub
		return null;
	}

}
