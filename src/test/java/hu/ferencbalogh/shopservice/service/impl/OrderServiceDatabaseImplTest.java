package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.repository.OrderRepository;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.service.OrderServiceTest;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OrderServiceDatabaseImpl.class, ProductServiceDatabaseImpl.class})
@EnableJpaRepositories(basePackageClasses = {OrderRepository.class})
@EntityScan(basePackageClasses = Order.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderServiceDatabaseImplTest extends OrderServiceTest {

}