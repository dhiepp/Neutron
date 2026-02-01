package me.crypnotic.neutron.module.command;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommandConfig {
    @Getter
    @Setting("hub-server")
    private String hubServer = "Lobby";
}
