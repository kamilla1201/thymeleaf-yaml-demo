package pl.codeleak.demos.sbt.home;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Arrays;
import java.util.List;

public class ThymeleafTest {

    public static final String LOCATOR_CLUSTER = "locator-cluster";
    public static final String SERVER_CLUSTER_1 = "server-cluster-1";
    public static final String SERVER_CLUSTER_2 = "server-cluster-2";
    public static final String LOCATOR = "locator";
    public static final String SERVER_1 = "server-1";
    public static final String SERVER_2 = "server-2";
    public static final String ADDRESS = "127.0.0.1";

    @Test
    public void test() {
        String fcLocator = getFilterChain(LOCATOR, LOCATOR_CLUSTER);
        String fcServer1 = getFilterChain(SERVER_1, SERVER_CLUSTER_1);
        String fcServer2 = getFilterChain(SERVER_2, SERVER_CLUSTER_2);

        String clusterLocator = getCluster(LOCATOR_CLUSTER, ADDRESS, "1111");
        String clusterServer1 = getCluster(SERVER_CLUSTER_1, ADDRESS, "2222");
        String clusterServer2 = getCluster(SERVER_CLUSTER_2, ADDRESS, "3333");

        String config = getConfig(Arrays.asList(fcLocator, fcServer1, fcServer2),
                Arrays.asList(clusterLocator, clusterServer1, clusterServer2));
        System.out.println(config);
    }

    private String getConfig(List<String> filterChains, List<String> clusters) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(textTemplateResolver());
        Context context = new Context();
        context.setVariable("filterChains", filterChains);
        context.setVariable("clusters", clusters);

        return templateEngine.process("envoy", context);
    }
    private String getCluster(String clusterName, String address, String port) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(textTemplateResolver());
        Context context = new Context();
        context.setVariable("clusterName", clusterName);
        context.setVariable("address", address);
        context.setVariable("port", port);
        //context.setVariable("tags", Arrays.asList("form.getTags()", "hi there"));

        return templateEngine.process("cluster", context);
    }

    public String getFilterChain(String serverName, String clusterName) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(textTemplateResolver());
        Context context = new Context();
        context.setVariable("serverName", serverName);
        context.setVariable("clusterName", clusterName);
        //context.setVariable("tags", Arrays.asList("form.getTags()", "hi there"));

        return templateEngine.process("filterChain", context);
    }

    private ITemplateResolver textTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/text/");
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
