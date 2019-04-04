/**
 * 
 */
package io.doll.Dollar;

import io.doll.Dollar.resource.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author AR364900
 *
 */
public class HelloWorldApplication extends Application<HelloWorldConfiguration > {

	public static void main(String[] args) throws Exception {
		System.out.println("this class");
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {
    		final HelloWorldResource resource = new HelloWorldResource(
                            configuration.getTemplate(),
                            configuration.getDefaultName()
                        );
                        environment.jersey().register(resource);}

}
