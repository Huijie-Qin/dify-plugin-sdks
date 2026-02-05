package com.dify.dify_plugin.core.entities.plugin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应 Python 的 setup.py，描述插件 Manifest 结构。
 */
enum PluginArch {
    AMD64,
    ARM64
}

enum PluginLanguage {
    PYTHON
}

enum PluginType {
    Plugin
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PluginResourceRequirements {
    private int memory;
    private Permission permission;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Permission {
        private Tool tool;
        private Model model;
        private Node node;
        private Endpoint endpoint;
        private App app;
        private Storage storage;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class Tool {
            private Boolean enabled = false;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class Model {
            private Boolean enabled = false;
            private Boolean llm = false;
            private Boolean textEmbedding = false;
            private Boolean rerank = false;
            private Boolean tts = false;
            private Boolean speech2text = false;
            private Boolean moderation = false;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class Node {
            private Boolean enabled = false;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class Endpoint {
            private Boolean enabled = false;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class App {
            private Boolean enabled = false;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class Storage {
            private Boolean enabled = false;
            private int size = 1048576;
        }
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PluginConfiguration {
    private String version;
    private PluginType type;
    private String author;
    private String name;
    private String repo;
    private Object description;
    private String icon;
    private String iconDark;
    private Object label;
    private LocalDateTime createdAt;
    private PluginResourceRequirements resource;
    private Plugins plugins;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Plugins {
        private List<String> tools = new ArrayList<>();
        private List<String> models = new ArrayList<>();
        private List<String> endpoints = new ArrayList<>();
        private List<String> agentStrategies = new ArrayList<>();
        private List<String> datasources = new ArrayList<>();
        private List<String> triggers = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Meta {
        private String version;
        private List<PluginArch> arch = new ArrayList<>();
        private PluginRunner runner;
        private String minimumDifyVersion;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class PluginRunner {
            private PluginLanguage language;
            private String version;
            private String entrypoint;
        }
    }
}
