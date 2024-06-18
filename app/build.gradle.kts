plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.jetbrains.kotlin.android)
}

val JAVA_VERSION = JavaVersion.VERSION_17

android {
	namespace = "og.ogstartracker"
	compileSdk = 34

	defaultConfig {
		applicationId = "og.ogstartracker"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JAVA_VERSION
		targetCompatibility = JAVA_VERSION
	}
	kotlinOptions {
		jvmTarget = JAVA_VERSION.toString()
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.navigation.runtime.ktx)
	debugImplementation(libs.androidx.ui.tooling)

	implementation(libs.accompanistSystemUiController)

	implementation(libs.hiltAndroid)
	implementation(libs.hiltNavCompose)
//	ksp(libs.hiltCompiler)
}