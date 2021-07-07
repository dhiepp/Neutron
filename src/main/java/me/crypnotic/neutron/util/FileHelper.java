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
package me.crypnotic.neutron.util;

import me.crypnotic.neutron.NeutronPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

    public static File getOrCreate(Path folder, String name) {
        File directory = getOrCreateDirectory(folder.getParent(), folder.getFileName().toString());
        File file = new File(directory, name);
        if (!file.exists()) {
            try {
                try (InputStream input = NeutronPlugin.class.getResourceAsStream("/" + name)) {
                    if (input != null) {
                        Files.copy(input, file.toPath());
                    } else {
                        file.createNewFile();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return file;
    }

    public static File getOrCreateDirectory(Path folder, String name) {
        File file = new File(folder.toFile(), name);
        if (file.exists()) {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdirs();
            }
        } else {
            file.mkdirs();
        }
        return file;
    }

    public static File getOrCreateLocale(Path folder, String localName) {
        File file = new File(folder.toFile(), localName);
        if (!file.exists()) {
            try {
                try (InputStream input = NeutronPlugin.class.getResourceAsStream("/locales/" + localName)) {
                    if (input != null) {
                        Files.copy(input, file.toPath());
                    } else {
                        file.createNewFile();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return file;
    }
}
