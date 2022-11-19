package com.mockmock.server;

import com.mockmock.AppStarter;
import com.mockmock.Settings;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

@Service
public class WebApiServer implements Server {
    private int port;

    private Settings settings;

    public void setPort(int port) {
        this.port = port;
    }

    public void start() {
        try
        {
            System.out.println("Starting MockMock API on port " + this.port);
            SpringApplication app = new SpringApplication(AppStarter.class);
            app.setDefaultProperties(Collections.singletonMap("server.port", (Object) this.port));
            app.run();
        }
        catch (Exception e)
        {
            System.err.println("Could not start MockMock. Maybe port " + port + " is already in use?");
        }        
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
