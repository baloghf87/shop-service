package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.repository.ProductRepository;
import hu.ferencbalogh.shopservice.service.ProductServiceIntegrationTest;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductServiceDatabaseImpl.class})
@EnableJpaRepositories(basePackageClasses = {ProductRepository.class})
@EntityScan(basePackageClasses = Product.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductServiceDatabaseImplIntegrationTest extends ProductServiceIntegrationTest {

}