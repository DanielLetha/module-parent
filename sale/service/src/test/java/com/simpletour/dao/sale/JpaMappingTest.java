package com.simpletour.dao.sale;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Created by zt on 2015/11/19.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class JpaMappingTest extends AbstractTestNGSpringContextTests {

//    private static Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Test
//    public void allClassMapping() throws Exception {
//        Metamodel model = em.getEntityManagerFactory().getMetamodel();
//        for (EntityType entityType : model.getEntities()) {
//            String entityName = entityType.getName();
//            em.createQuery("select o from " + entityName + " o").getResultList();
//            logger.info("ok: " + entityName);
//        }
//    }

}
