

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
//import servlet.IndexingServlet;
//import utils.IndexingServerConfig;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import x.spirit.websocketdemo.config.AppConfig;
import x.spirit.websocketdemo.servlet.RegisterServlet;

/**
 * 
 * @author zhangwei
 */
public class App {
    static Options options = new Options();
    private static Tomcat tomcat = null;
    private static void buildOptions() {
        // build option tables
        options.addOption(new Option("help", "print this message"));
        options.addOption(Option.builder("port").hasArg()
                .desc("port number")
                .build());
        options.addOption(Option.builder("ip").hasArg()
                .desc("external ip address")
                .build());
    }
    public static String[] parseArgs(String[] args) {
        String[] rst = new String[2];
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("indexing server", options);
                System.exit(0);
            }
            if (line.hasOption("ip")) {
                rst[0] = line.getOptionValue("ip");
            } else {
                throw new ParseException("argument 'ip' is required.");
            }
            if (line.hasOption("port")) {
                rst[1] = line.getOptionValue("port");
            } else {
                throw new ParseException("argument 'port' is required.");
            }
        } catch (ParseException exp) {
            System.out.println("Arguments Error:" + exp.getMessage());
            System.exit(-1);
        }
        return rst;
    }
    public static void main(String[] args) throws Exception {
        AppConfig config = AppConfig.getInstance();
        buildOptions();
        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String[] argValues = parseArgs(args);
        config.setIp_addr(argValues[0]);
        config.setPort_num(Integer.valueOf(argValues[1]));
        String appHome = System.getProperty("app.home");
        appHome = appHome == null?"":appHome;
        String webappDirLocation = appHome + "/webapp/";
        tomcat = new Tomcat();
        tomcat.setPort(config.getPort_num());
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getPath());
        System.out.println("configuring app with basedir: " + new File(webappDirLocation).getPath());
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        // Any servlet class annotated with WebServlet will be scanned and initialized as a servlet instance.
        File additionWebInfClasses = new File(appHome + "/classes");
        WebResourceRoot resources = new StandardRoot(ctx);

        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

//        String servletName = RegisterServlet.class.getAnnotation(WebServlet.class).name();
//        String servletClass = RegisterServlet.class.getName();
//        tomcat.addServlet(ctx, servletName, servletClass );
//        for (String urlPattern : RegisterServlet.class.getAnnotation(WebServlet.class).urlPatterns()
//                ) {
//            ctx.addServletMapping(urlPattern, servletName);
//        }

        String serverAddr = String.format("http://%s:%s", config.getIp_addr(), config.getPort_num());
        System.out.println("======================== NOTICE =====================");
        System.out.println(String.format("==> Register Server Initiated at %s\n" +
                "==> To register, send post request to %s/register with parameter '' and ''\n" +
                "==> To test, send get request to %s/get with parameter 'action'",
                serverAddr, serverAddr, serverAddr));
        System.out.println("======================== NOTICE =====================");
        
        tomcat.start();
        tomcat.getServer().await();
    }
}