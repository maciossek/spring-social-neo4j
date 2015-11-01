package org.springframework.social.connect.neo4j.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by SWijerathna on 10/7/2015.
 */
@NodeEntity
public class UserConnection {

    @GraphId
    public Long id;

    public String userId;

    public String providerId;

    public String providerUserId;

    public int rank;

    public String displayName;

    public String profileUrl;

    public String imageUrl;

    public String accessToken;

    public String secret;

    public String refreshToken;

    public Long expireTime;

    public UserConnection() {

    }

    public UserConnection(String userId,
                          String providerId,
                          String providerUserId,
                          int rank,
                          String displayName,
                          String profileUrl,
                          String imageUrl,
                          String accessToken,
                          String secret,
                          String refreshToken,
                          Long expireTime) {
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.rank = rank;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    public UserConnection update(String providerId,
                                       String providerUserId,
                                       String displayName,
                                       String profileUrl,
                                       String imageUrl,
                                       String accessToken,
                                       String secret,
                                       String refreshToken,
                                       Long expireTime) {

        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;

        return this;
    }

}
