package org.springframework.social.connect.neo4j.repositories.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.testutil.Neo4jIntegrationTestRule;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.social.connect.neo4j.repositories.SocialUserConnectionRepository;

import java.util.*;

import static org.junit.Assert.*;

public class SocialUserConnectionRepositoryImplTest {

    public static final String SPRING_SOCIAL_NEO4J_DOMAIN = "org.springframework.social.connect.neo4j.domain";

    private static final String TEST_USER_ID_1 = "user1@email.com";
    private static final String TEST_USER_ID_2 = "user2@email.com";

    private static final String TEST_PROVIDER_ID_1 = "provider1";
    private static final String TEST_PROVIDER_ID_2 = "provider2";

    private static final String TEST_PROVIDER_USER_ID_1 = "providerUserId1";
    private static final String TEST_PROVIDER_USER_ID_2 = "providerUserId2";

    SocialUserConnectionRepository socialUserConnectionRepository;

    @Rule
    public Neo4jIntegrationTestRule databaseServerRule = new Neo4jIntegrationTestRule();

    private Session session;

    @Before
    public void setUp() throws Exception {

        SessionFactory sessionFactory = new SessionFactory(SPRING_SOCIAL_NEO4J_DOMAIN);
        session = sessionFactory.openSession(databaseServerRule.url());
        socialUserConnectionRepository = new SocialUserConnectionRepositoryImpl(session);
    }

    @After
    public void tearDown() throws Exception {

        databaseServerRule.clearDatabase();
    }

    @Test
    public void testFindByUserId() throws Exception {

        setupData();

        Collection<SocialUserConnection> connections = socialUserConnectionRepository.findByUserId(TEST_USER_ID_1, new SortOrder().add("providerId"));

        assertEquals(3, connections.size());
        Iterator<SocialUserConnection> itr = connections.iterator();

        SocialUserConnection con1 = itr.next();
        SocialUserConnection con2 = itr.next();
        SocialUserConnection con3 = itr.next();

        assertEquals(TEST_USER_ID_1, con1.userId);
        assertEquals(TEST_PROVIDER_ID_1, con1.providerId);

        assertEquals(TEST_USER_ID_1, con2.userId);
        assertEquals(TEST_PROVIDER_ID_1, con2.providerId);

        assertEquals(TEST_USER_ID_1, con3.userId);
        assertEquals(TEST_PROVIDER_ID_2, con3.providerId);
    }

    @Test
    public void testFindByUserIdAndProviderId() throws Exception {

        setupData();

        Collection<SocialUserConnection> connections = socialUserConnectionRepository.findByUserIdAndProviderId(TEST_USER_ID_1, TEST_PROVIDER_ID_1, new SortOrder().add("providerUserId"));

        assertEquals(2, connections.size());
        SocialUserConnection con1 = connections.iterator().next();

        assertEquals(TEST_USER_ID_1, con1.userId);
        assertEquals(TEST_PROVIDER_ID_1, con1.providerId);
    }

    @Test
    public void testFindByUserIdAndProviderIdAndProviderUserIdIn() throws Exception {

        setupData();

        Iterable<SocialUserConnection> connections = socialUserConnectionRepository
                .findByUserIdAndProviderIdAndProviderUserIdIn(TEST_USER_ID_1, TEST_PROVIDER_ID_1,
                        new ArrayList<String>(Arrays.asList(TEST_PROVIDER_USER_ID_1)),
                        new SortOrder().add("providerUserId"));


        assertTrue(connections.iterator().hasNext());

        SocialUserConnection con1 = connections.iterator().next();

        assertEquals(TEST_USER_ID_1, con1.userId);
        assertEquals(TEST_PROVIDER_ID_1, con1.providerId);
        assertEquals(TEST_PROVIDER_USER_ID_1, con1.providerUserId);
    }

    @Test
    public void testFindByUserIdAndProviderIdAndProviderUserId() throws Exception {

        setupData();

        SocialUserConnection connection = socialUserConnectionRepository
                .findByUserIdAndProviderIdAndProviderUserId(TEST_USER_ID_1, TEST_PROVIDER_ID_1,
                        TEST_PROVIDER_USER_ID_1);


        assertNotNull(connection);


        assertEquals(TEST_USER_ID_1, connection.userId);
        assertEquals(TEST_PROVIDER_ID_1, connection.providerId);
        assertEquals(TEST_PROVIDER_USER_ID_1, connection.providerUserId);

    }

    @Test
    public void testDeleteByUserIdAndProviderId() throws Exception{

        setupData();

        socialUserConnectionRepository.deleteByUserIdAndProviderId(TEST_USER_ID_1, TEST_PROVIDER_ID_1);

        String query = "MATCH (n:SocialUserConnection) " +
                            "WHERE n.userId = {userId} AND n.providerId = {providerId} " +
                            "RETURN n";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "userId", TEST_USER_ID_1 );
        params.put( "providerId", TEST_PROVIDER_ID_1 );
        Iterable<SocialUserConnection> conns = session.query(SocialUserConnection.class, query, params);

        assertFalse(conns.iterator().hasNext());
    }

    @Test
    public void testDeleteByUserIdAndProviderIdAndProviderUserId() {

        setupData();

        socialUserConnectionRepository.deleteByUserIdAndProviderIdAndProviderUserId(TEST_USER_ID_1, TEST_PROVIDER_ID_1, TEST_PROVIDER_USER_ID_1);

        String query = "MATCH (n:SocialUserConnection) " +
                "WHERE n.userId = {userId} AND n.providerId = {providerId} AND n.providerUserId = {providerUserId}" +
                "RETURN n";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "userId", TEST_USER_ID_1 );
        params.put( "providerId", TEST_PROVIDER_ID_1 );
        params.put( "providerUserId", TEST_PROVIDER_USER_ID_1);

        Iterable<SocialUserConnection> conns = session.query(SocialUserConnection.class, query, params);

        assertFalse(conns.iterator().hasNext());
    }

    @Test
    public void testFindByProviderIdAndProviderUserId() {

        setupData();

        Collection<SocialUserConnection> connections = socialUserConnectionRepository.findByProviderIdAndProviderUserId(TEST_PROVIDER_ID_1, TEST_PROVIDER_USER_ID_1);

        assertEquals(2, connections.size());
        SocialUserConnection con1 = connections.iterator().next();

        assertEquals(TEST_PROVIDER_ID_1, con1.providerId);
        assertEquals(TEST_PROVIDER_USER_ID_1, con1.providerUserId);

    }

    @Test
    public void testFindByProviderIdAndProviderUserIdIn () {

        setupData();

        Iterable<SocialUserConnection> connections = socialUserConnectionRepository
                .findByProviderIdAndProviderUserIdIn( TEST_PROVIDER_ID_1,
                        new ArrayList<String>(Arrays.asList(TEST_PROVIDER_USER_ID_1)));


        assertTrue(connections.iterator().hasNext());

        SocialUserConnection con1 = connections.iterator().next();

        assertEquals(TEST_PROVIDER_ID_1, con1.providerId);
        assertEquals(TEST_PROVIDER_USER_ID_1, con1.providerUserId);
    }

    @Test
    public void testSave() throws Exception {

        socialUserConnectionRepository.save(getSampleUserConnection(TEST_USER_ID_1, TEST_PROVIDER_ID_1, TEST_PROVIDER_USER_ID_1));

        Collection<SocialUserConnection> savedUserCons = session.loadAll(SocialUserConnection.class);

        assertEquals(1, savedUserCons.size());

        SocialUserConnection savedUserCon = savedUserCons.iterator().next();
        assertNotNull(savedUserCon.id);
        assertEquals(TEST_USER_ID_1, savedUserCon.userId);
    }

    private void setupData() {

        session.save(getSampleUserConnection(TEST_USER_ID_1,TEST_PROVIDER_ID_2, TEST_PROVIDER_USER_ID_1));
        session.save(getSampleUserConnection(TEST_USER_ID_1,TEST_PROVIDER_ID_1, TEST_PROVIDER_USER_ID_1));
        session.save(getSampleUserConnection(TEST_USER_ID_1,TEST_PROVIDER_ID_1, TEST_PROVIDER_USER_ID_2));
        session.save(getSampleUserConnection(TEST_USER_ID_2,TEST_PROVIDER_ID_1, TEST_PROVIDER_USER_ID_1));
    }

    private SocialUserConnection getSampleUserConnection(String userId, String providerId, String providerUserId) {

        SocialUserConnection userConnection = new SocialUserConnection();
        userConnection.userId = userId;
        userConnection.providerId = providerId;
        userConnection.providerUserId = providerUserId;
        return userConnection;
    }
}