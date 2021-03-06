package com.ebuy.main;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class AppConfiguration extends Configuration{
	@NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";
   
    public String getTemplate() {
        return template;
    }

    
    public void setTemplate(String template) {
        this.template = template;
    }

 
    public String getDefaultName() {
        return defaultName;
    }


    public void setDefaultName(String name) {
        this.defaultName = name;
    }
}
