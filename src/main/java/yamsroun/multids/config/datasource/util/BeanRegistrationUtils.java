package yamsroun.multids.config.datasource.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.support.StaticApplicationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanRegistrationUtils {

    private static final StaticApplicationContext staticApplicationContext = new StaticApplicationContext();

    public static void register(String beanName, Object beanObject) {
        var beanFactory = staticApplicationContext.getBeanFactory();
        beanFactory.registerSingleton(beanName, beanObject);
        beanFactory.initializeBean(beanObject, beanName);
    }
}
