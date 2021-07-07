package me.crypnotic.neutron.module.command;

import lombok.Getter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class CommandConfig {
    @Getter
    @Setting("hub-server")
    private String hubServer = "Lobby";
}
