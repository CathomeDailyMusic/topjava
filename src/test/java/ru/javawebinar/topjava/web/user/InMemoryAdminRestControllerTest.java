package ru.javawebinar.topjava.web.user;

import org.junit.*;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collection;

import static ru.javawebinar.topjava.UserTestData.ADMIN;

public class InMemoryAdminRestControllerTest {
    private static XmlWebApplicationContext webApplicationContext;
    private static AdminRestController controller;

    @BeforeClass
    public static void beforeClass() {
        webApplicationContext = new XmlWebApplicationContext();
        MockServletContext servletContext = new MockServletContext();
        webApplicationContext.setServletContext(servletContext);
        webApplicationContext.setServletConfig(new MockServletConfig(servletContext));
        webApplicationContext.setConfigLocations("spring/spring-app.xml", "spring/inmemory.xml");
        webApplicationContext.refresh();
        System.out.println("Beans:\n" + Arrays.toString(webApplicationContext.getBeanDefinitionNames()) + "\n");
        controller = webApplicationContext.getBean(AdminRestController.class);
    }

    @AfterClass
    public static void afterClass() {
        webApplicationContext.close();
    }

    @Before
    public void setUp() throws Exception {
        // re-initialize
        InMemoryUserRepository repository = webApplicationContext.getBean(InMemoryUserRepository.class);
        repository.init();
    }

    @Test
    public void delete() throws Exception {
        controller.delete(UserTestData.USER_ID);
        Collection<User> users = controller.getAll();
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(ADMIN, users.iterator().next());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        controller.delete(10);
    }
}