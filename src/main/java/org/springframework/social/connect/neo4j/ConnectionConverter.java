package org.springframework.social.connect.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

@Component
public class ConnectionConverter {
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final TextEncryptor textEncryptor;

	@Autowired
	public ConnectionConverter(ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor) {

		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	
	public Connection<?> convert(Neo4jTemplate neoTpl) {
		return null;
	}
	private ConnectionData fillConnectionData(Neo4jTemplate uc) {
		return null;
	}

	public Neo4jTemplate convert(Connection<?> cnn) {
		return null;
	}
	
	private String decrypt(String encryptedText) {
		return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
	}

	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}
}