package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws IOException {
        String filename = "application.properties";
        String key = "spring.application.name";

        String src = DemoApplication.class.getClassLoader().getResource(filename).getPath();
//        Enumeration<URL> resources = DemoApplication.class.getClassLoader().getResources("config/" + filename);
//        while (resources.hasMoreElements()){
//            var it = resources.nextElement();
//            System.out.println("resources " + it.getPath());
//        }
        System.out.printf("%s src: %s%n", filename, src);
        // 加载application.properties文件
        Properties properties = PropertiesLoaderUtils.loadAllProperties(filename);

        System.out.printf("spring.application.name = %s%n", properties.getProperty(key));
        // 修改属性值
        properties.setProperty("spring.application.name", "new");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resolverResources = resolver.getResources("**/" + filename);
        for (Resource resolverResource : resolverResources) {
            System.out.println("resolverResources URL " + resolverResource.getURL().getPath());
//            System.out.println("resolverResources URI " + resolverResource.getURI().getPath());

            // 将修改后的配置写回文件
            FileOutputStream fileOutputStream = new FileOutputStream(resolverResource.getFile());
            properties.store(fileOutputStream, null);
            fileOutputStream.flush();
            fileOutputStream.close();
        }


        SpringApplication app = new SpringApplication(DemoApplication.class);
        ConfigurableApplicationContext applicationContext = app.run(args);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String name = environment.getProperty(key);
        System.out.println(name);
        System.exit(0);
    }


}
