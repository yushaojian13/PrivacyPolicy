
tasks.register("copyAARRelease", Copy::class) {
    from("$buildDir${File.separator}outputs${File.separator}aar")
    include("*.aar")
    into("$rootDir${File.separator}app${File.separator}libs")
}

afterEvaluate {
    tasks.named("assembleRelease").configure {
        finalizedBy("copyAARRelease")
    }
}
