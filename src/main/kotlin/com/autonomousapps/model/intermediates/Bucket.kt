package com.autonomousapps.model.intermediates

import com.autonomousapps.internal.configuration.Configurations

/** Standard user-facing dependency buckets, variant-agnostic. */
internal enum class Bucket(val value: String) {
  API("api"),
  IMPL("implementation"),
  COMPILE_ONLY("compileOnly"),
  RUNTIME_ONLY("runtimeOnly"),

  // note that only the java-library plugin currently supports this configuration
  // COMPILE_ONLY_API("compileOnlyApi"),
  ANNOTATION_PROCESSOR("annotationProcessor"), // TODO or kapt

  /** Unused. */
  NONE("n/a"),
  ;

  fun matches(location: Location): Boolean {
    return this == location.bucket
  }

  companion object {
    fun of(configurationName: String): Bucket {
      if (Configurations.isAnnotationProcessor(configurationName)) return ANNOTATION_PROCESSOR

      return values().find { bucket ->
        configurationName.endsWith(bucket.value, true)
      } ?: throw IllegalArgumentException("No matching bucket for $configurationName")
    }
  }
}