package org.springframework.social.connect.neo4j.converters;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;

/**
 * Created by SWijerathna on 10/7/2015.
 */
public class ConnectionConverter {

    public static ConnectionData toConnectionData(SocialUserConnection dbCon, TextEncryptor textEncryptor) {

        return new ConnectionData(dbCon.providerId,
                dbCon.providerUserId,
                dbCon.displayName,
                dbCon.profileUrl,
                dbCon.imageUrl,
                textEncryptor.decrypt(dbCon.accessToken),
                textEncryptor.decrypt(dbCon.secret),
                textEncryptor.decrypt(dbCon.refreshToken),
                dbCon.expireTime);
    }

    public static SocialUserConnection toSocialUserConnection(String userId, Integer rank, ConnectionData data, TextEncryptor textEncryptor){
        return new SocialUserConnection(userId,
                data.getProviderId(),
                data.getProviderUserId(),
                rank,
                data.getDisplayName(),
                data.getProfileUrl(),
                data.getImageUrl(),
                textEncryptor.encrypt(data.getAccessToken()),
                textEncryptor.encrypt(data.getSecret()),
                textEncryptor.encrypt(data.getRefreshToken()),
                data.getExpireTime());
    }

    public static SocialUserConnection toSocialUserConnection(ConnectionData data, SocialUserConnection dbCon, TextEncryptor textEncryptor){
        return dbCon.update(data.getProviderId(),
                data.getProviderUserId(),
                data.getDisplayName(),
                data.getProfileUrl(),
                data.getImageUrl(),
                textEncryptor.encrypt(data.getAccessToken()),
                textEncryptor.encrypt(data.getSecret()),
                textEncryptor.encrypt(data.getRefreshToken()),
                data.getExpireTime());
    }
}
