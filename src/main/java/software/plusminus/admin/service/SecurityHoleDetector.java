package software.plusminus.admin.service;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SecurityHoleDetector {

    @EventListener(ContextRefreshedEvent.class)
    public void antiResheto() {
        // TODO
    }

}
