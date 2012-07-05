package org.springframework.social.connect.neo4j;


import java.util.List;
import java.util.Set;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.MultiValueMap;

public interface ConnectionService {

	int getMaxRank(String userId, String providerId);

	void create(String userId, Connection<?> userConn, int rank);

	void update(String userId, Connection<?> userConn);

	void remove(String userId, ConnectionKey connectionKey);

	void remove(String userId, String providerId);

	Connection<?> getPrimaryConnection(String userId,
			String providerId);

	Connection<?> getConnection(String userId,
			String providerId, String providerUserId);

	List<Connection<?>> getConnections(String userId);

	List<Connection<?>> getConnections(String userId,
			String providerId);

	List<Connection<?>> getConnections(String userId,
			MultiValueMap<String, String> providerUsers);

	Set<String> getUserIds(String providerId,
			Set<String> providerUserIds);

	List<String> getUserIds(String providerId,
			String providerUserId);

}