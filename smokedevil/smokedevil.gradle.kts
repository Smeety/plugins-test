version = "0.6.9"

project.extra["PluginName"] = "Smoke Devil"
project.extra["PluginDescription"] = "Plugin for killing Thermonuclear Smoke Devil"
project.extra["PluginProvider"] = "ImNo & Smeety"
project.extra["Support"] = "https://discord.gg/dWXAAE"

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