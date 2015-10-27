package org.springframework.social.connect.neo4j.converters;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;

/**
 * Created by SWijerathna on 10/7/2015.
 */
public class ConnectionConverter {

    public static ConnectionData toConnectionData(SocialUserConnection dbCon) {

        return new ConnectionData(dbCon.providerId,
                dbCon.providerUserId,
                dbCon.displayName,
                dbCon.profileUrl,
                dbCon.imageUrl,
                dbCon.accessToken,
                dbCon.secret,
                dbCon.refreshToken,
                dbCon.expireTime);
    }

    public static SocialUserConnection toSocialUserConnection(String userId, Integer rank, ConnectionData data){
        return new SocialUserConnection(userId,
                data.getProviderId(),
                data.getProviderUserId(),
                rank,
                data.getDisplayName(),
                data.getProfileUrl(),
                data.getImageUrl(),
                data.getAccessToken(),
                data.getSecret(),
                data.getRefreshToken(),
                data.getExpireTime());
    }

    public static SocialUserConnection toSocialUserConnection(ConnectionData data, SocialUserConnection dbCon){
        return dbCon.update(data.getProviderId(),
                data.getProviderUserId(),
                data.getDisplayName(),
                data.getProfileUrl(),
                data.getImageUrl(),
                data.getAccessToken(),
                data.getSecret(),
                data.getRefreshToken(),
                data.getExpireTime());
    }
}
