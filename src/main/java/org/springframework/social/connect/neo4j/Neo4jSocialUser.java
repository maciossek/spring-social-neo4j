package org.springframework.social.connect.neo4j;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * 
 * @author Mathias Maciossek
 * @version 0.0.1
 * @see http://static.springsource.org/spring-social/docs/1.0.x/reference/html/serviceprovider.html#service-providers-persisting-connections-jdbc
 * 
 */
@NodeEntity
public class Neo4jSocialUser {

	@GraphId @NotNull @Indexed
	private Long userId;
	@Indexed
	private Long providerId;
	@Indexed
	private Long providerUserId;
	@NotNull
	private int rank;
	private String displayName;
	private String profileUrl;
	private String imageUrl;
	@NotNull
	private String accessToken;
	private String secret;
	private String refreshToken;
	private Long expireTime;
	private Date createDate = new Date();
	


	// Constructor
	public Neo4jSocialUser() {
	}
	
	// Getters and Setters
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Long getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(Long providerUserId) {
		this.providerUserId = providerUserId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}