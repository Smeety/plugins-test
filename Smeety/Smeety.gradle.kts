version = "0.0.1"

project.extra["PluginName"] = "Smeety"
project.extra["PluginDescription"] = "Smeetys Cock converter plugin"
project.extra["PluginProvider"] = "ImNo"
project.extra["Support"] = "https://www.youtube.com/watch?v=hPS6bl9QOHw"

tasks {
    jar {
        manifest {
            attributes(mapOf(
                    "Plugin-Version" to project.version,
                    "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                    "Plugin-Provider" to project.extra["PluginProvider"],
                    "Plugin-Description" to project.extra["PluginDescription"],
                    "Plugin-License" to project.extra["PluginLicense"],
                    "Support" to project.extra["Support"]
            ))
        }
    }
}