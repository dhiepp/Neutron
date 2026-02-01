/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2020 Crypnotic <crypnoticofficial@gmail.com>
* Copyright (c) 2020 Contributors
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package me.crypnotic.neutron.module.command;

import lombok.Getter;
import me.crypnotic.neutron.api.StateResult;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.module.Module;
import me.crypnotic.neutron.util.ConfigHelper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandModule extends Module {

    private Map<Commands, CommandWrapper> commands = new HashMap<Commands, CommandWrapper>();

    @Getter
    private CommandConfig config;

    private CommandHandler handler;

    public static CommandModule instance;

    public CommandModule() {
        instance = this;
    }

    @Override
    public StateResult init() {
        ConfigurationNode options = getRootNode().node("options");
        if (options.virtual()) {
            getNeutron().getLogger().warn("No config entry found for command module options");
            return StateResult.fail();
        }

        for (Commands spec : Commands.values()) {
            ConfigurationNode node = options.node(spec.getKey());
            if (node.virtual()) {
                getNeutron().getLogger().warn("No config entry for command: " + spec.getKey());
                continue;
            }

            CommandWrapper wrapper = spec.getSupplier().get();

            try {
                List<String> aliases = node.node("aliases").getList(String.class);

                wrapper.setEnabled(node.node("enabled").getBoolean());
                wrapper.setAliases(aliases.stream().skip(1).toArray(String[]::new));

                if (wrapper.isEnabled()) {
                    getNeutron().getProxy().getCommandManager().register(
                            aliases.get(0), wrapper, wrapper.getAliases());
                }
            } catch (SerializationException exception) {
                exception.printStackTrace();
            }

            commands.put(spec, wrapper);
        }

        this.config = ConfigHelper.getSerializable(getRootNode(), new CommandConfig());
        if (config == null) {
            return StateResult.fail();
        }

        this.handler = new CommandHandler(this, config);

        getNeutron().getProxy().getEventManager().register(getNeutron(), handler);

        return StateResult.success();
    }

    @Override
    public StateResult reload() {
        return StateResult.of(shutdown(), init());
    }

    @Override
    public StateResult shutdown() {
        commands.values().stream().map(CommandWrapper::getAliases).flatMap(Arrays::stream)
                .forEach(getNeutron().getProxy().getCommandManager()::unregister);

        commands.clear();

        getNeutron().getProxy().getEventManager().unregisterListener(getNeutron(), handler);

        return StateResult.success();
    }

    @Override
    public String getName() {
        return "command";
    }
}
