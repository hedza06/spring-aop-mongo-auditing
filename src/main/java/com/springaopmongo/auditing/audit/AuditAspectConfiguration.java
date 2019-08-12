package com.springaopmongo.auditing.audit;

import com.springaopmongo.auditing.aspects.AuditAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AuditAspectConfiguration {

    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }

}
