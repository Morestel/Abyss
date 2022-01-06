package com.pwa.project.abyss;

import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class Setup {
    @Inject
    Setup(Data setup) {
        // NB: to debug this initialization method, sometimes it is better to call it
        // from a controller (so it happen when we do a request, not at init)
        // ... see the SetupController for that
        setup.generateData();
    }
}
