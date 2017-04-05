package ir.ac.iust.dml.kg.raw.utils

import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

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

   fun getConfig(vararg keyValues: String): Properties {
      val map = mutableMapOf<String, String>()
      for (i in 0..keyValues.size / 2) map[keyValues[i]] = keyValues[i + 1]
      return getConfig(map)
   }

   fun getConfig(keyValues: Map<String, Any>): Properties {
      val configPath = Paths.get(System.getProperty("user.home")).resolve(".pkg").resolve("config.properties")

      Files.createDirectories(configPath.parent)

      val config = Properties()

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
            config[it.key] = it.value
         }
      }

      FileOutputStream(configPath.toFile()).use {
         config.store(it, null)
      }

      return config
   }

   fun getPath(string: String): Path {
      val splits = string.split("/")
      val first = if (splits[0] == "~") System.getProperty("user.home")!! else splits[0]
      return Paths.get(first, *splits.subList(1, splits.size).toTypedArray())
   }
}