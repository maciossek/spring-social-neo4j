package org.springframework.social.connect.neo4j.repositories.impl;


import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.social.connect.neo4j.repositories.SocialUserConnectionRepository;

import java.util.*;

/**
 * Created by SWijerathna on 10/28/2015.
 */
public class SocialUserConnectionRepositoryImpl implements SocialUserConnectionRepository {

    Session session;

    public SocialUserConnectionRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public Collection<SocialUserConnection> findByUserId(String userId, SortOrder sortOrder) {

        return session.loadAll(SocialUserConnection.class, new Filter("userId", userId), sortOrder);
    }

    @Override
    public Collection<SocialUserConnection> findByUserIdAndProviderId(String userId, String providerId, SortOrder sortOrder) {

        Filters filters = new Filters();
        Filter f1 = new Filter("userId", userId);
        Filter f2 = new Filter("providerId", providerId);
        f2.setBooleanOperator(BooleanOperator.AND);
        filters.add(f1, f2);

        return session.loadAll(SocialUserConnection.class, filters, sortOrder);
    }

    @Override
    public Iterable<SocialUserConnection> findByUserIdAndProviderIdAndProviderUserIdIn(String userId, String providerId, List<String> value, SortOrder sortOrder) {

        String query = "MATCH (n:SocialUserConnection) " +
                        "WHERE n.userId = {userId} " +
                        "AND n.providerId = {providerId}" +
                        "AND n.providerUserId IN {providerUserIds} " +
                        "RETURN n;";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "userId", userId );
        params.put( "providerId", providerId );
        params.put( "providerUserIds", value.toArray() );

        return session.query(SocialUserConnection.class, query, params);
    }

    @Override
    public SocialUserConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId) {

        Filters filters = new Filters();
        Filter f1 = new Filter("userId", userId);
        Filter f2 = new Filter("providerId", providerId);
        f2.setBooleanOperator(BooleanOperator.AND);
        Filter f3 = new Filter("providerUserId", providerUserId);
        f3.setBooleanOperator(BooleanOperator.AND);
        filters.add(f1, f2, f3);

        Collection<SocialUserConnection> connections = session.loadAll(SocialUserConnection.class, filters);

        if(connections.size() > 0) {
            return connections.iterator().next();
        }

        return null;
    }

    @Override
    public void deleteByUserIdAndProviderId(String userId, String providerId) {

        Collection<SocialUserConnection> cons = findByUserIdAndProviderId(userId, providerId, new SortOrder());
        Iterator<SocialUserConnection> itr = cons.iterator();

        while(itr.hasNext()) {
            session.delete(itr.next());
        }
    }

    @Override
    public void deleteByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId) {

        SocialUserConnection cons = findByUserIdAndProviderIdAndProviderUserId(userId, providerId, providerUserId);
        session.delete(cons);
    }

    @Override
    public Collection<SocialUserConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId) {

        Filters filters = new Filters();
        Filter f1 = new Filter("providerId", providerId);
        Filter f2 = new Filter("providerUserId", providerUserId);
        f2.setBooleanOperator(BooleanOperator.AND);
        filters.add(f1, f2);

        return session.loadAll(SocialUserConnection.class, filters, new SortOrder());
    }

    @Override
    public Iterable<SocialUserConnection> findByProviderIdAndProviderUserIdIn(String providerId, List<String> providerUserIds) {

        String query = "MATCH (n:SocialUserConnection) " +
                "WHERE n.providerId = {providerId}" +
                "AND n.providerUserId IN {providerUserIds} " +
                "RETURN n;";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "providerId", providerId );
        params.put( "providerUserIds", providerUserIds.toArray() );

        return session.query(SocialUserConnection.class, query, params);
    }

    @Override
    public void save(SocialUserConnection socialUserConnection) {
        session.save(socialUserConnection);
    }
}
