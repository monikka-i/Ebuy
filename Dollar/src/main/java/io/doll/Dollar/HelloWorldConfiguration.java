/**
 * 
 */
package io.doll.Dollar;

import org.hibernate.validator.constraints.NotEmpty;

import io.dropwizard.Configuration;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author AR364900
 *
 */
public class HelloWorldConfiguration  extends Configuration {
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
