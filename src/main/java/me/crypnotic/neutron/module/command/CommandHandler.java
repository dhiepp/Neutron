package me.crypnotic.neutron.module.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandHandler {

    private final CommandModule module;
    private final CommandConfig config;
}
