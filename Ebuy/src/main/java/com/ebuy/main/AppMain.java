package com.ebuy.main;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.ebuy.service.EbuyService;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AppMain extends Application<AppConfiguration >{
	public static void main(String[] args) throws Exception {
		new AppMain().run(args);
	}

	@Override
	public void initialize(Bootstrap<AppConfiguration> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(AppConfiguration configuration, Environment environment) throws Exception {
		final EbuyService resource = new EbuyService(
                configuration.getTemplate(),
                configuration.getDefaultName()
            );
            environment.jersey().register(resource);
            
	}
}
