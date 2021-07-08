plugins {
    java
    kotlin("jvm") version "1.3.61"
}

group = "com.kcb"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.testng:testng:7.1.0")
    testCompile("junit", "junit", "4.12")
    testCompile( "org.hamcrest", "hamcrest-library","1.3")
    testCompile("com.fasterxml.jackson.core", "jackson-databind", "2.0.1")
    testCompile("com.fasterxml.jackson.dataformat", "jackson-dataformat-csv", "2.12.1")

    compile("org.codehaus.groovy:groovy-all:2.3.11")
    implementation(kotlin("stdlib-jdk8"))
    implementation ("com.github.jsqlparser:jsqlparser:4.0")


    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation ("org.slf4j:slf4j-log4j12:1.7.30")

    implementation("org.apache.logging.log4j:log4j-api:2.10.0")
    implementation("org.apache.logging.log4j:log4j-core:2.10.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.10.0")
    implementation("org.apache.logging.log4j:log4j-1.2-api:2.10.0")

}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<Jar>() {

    // build시 jsqlParser dependency 추가.
    configurations["compileClasspath"]
        .filter{ file ->file.name.contains("jsqlparser")}
        .forEach{ file: File -> from(zipTree(file.absoluteFile))}
}