plugins { id("buildlogic.java-application-conventions") }

dependencies {
	implementation("org.apache.commons:commons-text")
	implementation(project(":ui"))
	implementation(project(":os"))
}

application { mainClass = "com.twentyfeel.core.Main" }
