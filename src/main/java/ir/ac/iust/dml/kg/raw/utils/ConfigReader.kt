package ir.ac.iust.dml.kg.raw.utils

import nu.studer.java.util.OrderedProperties
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


object ConfigReader {

  val logger = LoggerFactory.getLogger(this.javaClass)!!

  fun getPath(key: String, defaultValue: String) = getPath(getString(key, defaultValue))

  fun getString(key: String, defaultValue: String): String {
    val p = getConfig(mapOf(key to defaultValue))
    return p.getProperty(key)
  }

  fun getInt(key: String, defaultValue: String): Int {
    val p = getConfig(mapOf(key to defaultValue))
    return p.getProperty(key).toInt()
  }

  fun getLong(key: String, defaultValue: String): Long {
    val p = getConfig(mapOf(key to defaultValue))
    return p.getProperty(key).toLong()
  }

  fun getBoolean(key: String, defaultValue: String): Boolean {
    val p = getConfig(mapOf(key to defaultValue))
    return p.getProperty(key).toBoolean()
  }

  fun getConfig(vararg keyValues: String): OrderedProperties {
    val map = mutableMapOf<String, String>()
    for (i in 0..keyValues.size / 2) map[keyValues[i]] = keyValues[i + 1]
    return getConfig(map)
  }

  fun getConfig(keyValues: Map<String, Any>): OrderedProperties {
    val configPath =
       if (Files.exists(Paths.get("/srv/.pkg/config.properties")))
         Paths.get("/srv/.pkg/config.properties")
       else
         Paths.get(System.getProperty("user.home")).resolve(".pkg").resolve("config.properties")

    Files.createDirectories(configPath.parent)

    val config = OrderedProperties()

    if (!Files.exists(configPath)) logger.error("There is no file ${configPath.toAbsolutePath()} existed.")
    else
      FileInputStream(configPath.toFile()).use {
        try {
          config.load(it)
        } catch (e: Throwable) {
          logger.error("error in reading config file ${configPath.toAbsolutePath()}.", e)
        }
      }

    keyValues.forEach {
      try {
        config.getProperty(it.key)!!
      } catch (e: Throwable) {
        config.setProperty(it.key, it.value.toString())
      }
    }

    FileOutputStream(configPath.toFile()).use {
      config.store(it, null)
    }

    return config
  }

  fun getPath(string: String): Path {
    if (string.startsWith('/')) return Paths.get(string)
    var s =
       if (string.startsWith('~'))
         string.replace("~", System.getProperty("user.home")!!)
       else string
    if (s.contains("/")) return Paths.get(s)
    s = s.replace('/', File.separatorChar)
    return Paths.get(s)
  }
}