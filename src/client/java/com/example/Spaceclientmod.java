package com.example;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spaceclientmod implements ClientModInitializer {
    public static final String MOD_ID = "spacemod";
    public static final String PREFIX = ".";
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}