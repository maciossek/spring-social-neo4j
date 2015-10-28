package org.springframework.social.connect.neo4j.repositories.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.testutil.Neo4jIntegrationTestRule;
import org.springframework.social.connect.neo4j.domain.SocialUserConnection;
import org.springframework.social.connect.neo4j.repositories.SocialUserConnectionRepository;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SocialUserConnectionRepositoryImplTest {

    public static final String SPRINGFRAMEWORK_SOCIAL_NEO4J_DOMAIN = "org.springframework.social.connect.neo4j.domain";

    private static final String TEST_USER_ID = "user@email.com";

    SocialUserConnectionRepository socialUserConnectionRepository;

    @Rule
    public Neo4jIntegrationTestRule databaseServerRule = new Neo4jIntegrationTestRule();

    private Session session;

    @Before
    public void setUp() throws Exception {

        SessionFactory sessionFactory = new SessionFactory(SPRINGFRAMEWORK_SOCIAL_NEO4J_DOMAIN);
        session = sessionFactory.openSession(databaseServerRule.url());
        socialUserConnectionRepository = new SocialUserConnectionRepositoryImpl(session);
    }

    @After
    public void tearDown() throws Exception {

        databaseServerRule.clearDatabase();
    }

    @Test
    public void testSave() throws Exception {

        socialUserConnectionRepository.save(getSampleUserConnection());

        Collection<SocialUserConnection> savedUserCons = session.loadAll(SocialUserConnection.class);

        assertEquals(1, savedUserCons.size());

        SocialUserConnection savedUserCon = savedUserCons.iterator().next();

        assertNotNull(savedUserCon.id);
        assertEquals(TEST_USER_ID, savedUserCon.userId);
    }

    private SocialUserConnection getSampleUserConnection() {

        SocialUserConnection userConnection = new SocialUserConnection();

        userConnection.userId = TEST_USER_ID;

        return userConnection;
    }
}