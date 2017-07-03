package ir.ac.iust.dml.kg.raw.utils

import com.google.common.base.CaseFormat
import java.util.*

object PrefixService {

  val prefixNames = mutableMapOf<String, String>()
  val prefixAddresses = mutableMapOf<String, String>()

  val KG_RESOURCE_PREFIX = "fkgr"
  val KG_ONTOLOGY_PREFIX = "fkgo"
  val KG_AUTO_PROPERTY_PREFIX = "fkgp"
  val KG_MANUAL_PREFIX = "fkgm"
  val KG_TABLE_PREFIX = "fkgl"
  val KG_RAW_TEXT_PREFIX = "fkgt"

  val TYPE_OF_ALL_PROPERTIES = "owl:ObjectProperty"
  // "rdfs:Resource" equivalent to "owl:Thing" in OWL:Full
  val TYPE_OF_ALL_RESOURCES = "owl:NamedIndividual"
  val TYPE_OF_ALL_CLASSES = "owl:Class" // equivalent to "rdfs:Class" in OWL:Full
  val SUB_CLASS_OF = "rdfs:subClassOf"
  val COMMENT_URL = "rdfs:comment"
  val CLASS_TREE = "fkgo:classTree"
  val LABEL_URL = "rdfs:label"
  val PROPERTY_DOMAIN_URL = "rdfs:domain"
  val PROPERTY_RANGE_URL = "rdfs:range"
  val EQUIVALENT_PROPERTY_URL = "owl:equivalentProperty"
  val EQUIVALENT_CLASS_URL = "owl:equivalentClass"
  val DISJOINT_WITH_URL = "owl:disjointWith"
  val TYPE_URL = "rdf:type"
  val VARIANT_LABEL_URL = "fkgo:variantLabel"
  val INSTANCE_OF_URL = "rdf:instanceOf"
  val DISAMBIGUATED_FROM_URI = "fkgo:wikiDisambiguatedFrom"
  val REDIRECTS_URI = "fkgo:wikiPageRedirects"

  init {
    reload()
  }

  fun reload() {
    val prefixServices = Properties()
    prefixServices.load(this.javaClass.getResourceAsStream("/prefixes.properties"))
    prefixServices.keys.forEach {
      prefixNames[it as String] = prefixServices.getProperty(it)!!
      prefixAddresses[prefixServices.getProperty(it)!!] = it
    }
  }

  fun replacePrefixes(text: String): String {
    var result = text
    prefixAddresses.keys.asSequence()
        .filter { result.contains(it) }
        .forEach { result = result.replace(it, prefixAddresses[it]!! + ":") }
    return result
  }

  fun prefixToUri(source: String?): String? {
    if (source == null || !source.contains(':') || source.startsWith("http")) return source
    val splits = source.split(":")
    var address = prefixNames[splits[0]]
    if (address != null && !address.startsWith("http://") && !address.startsWith("https://"))
      address = "http://" + address
    return if (address == null) splits[1] else address + splits[1]
  }

  val adjacentSpaceRegex = Regex("([\u00A0]|\\s)+")

  fun getFkgManualUrl(name: String) = prefixNames[KG_MANUAL_PREFIX] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgManual(name: String) = KG_MANUAL_PREFIX + ":" + name.replace(' ', '_')

  fun getFkgTableUrl(name: String) = prefixNames[KG_TABLE_PREFIX] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgTable(name: String) = KG_TABLE_PREFIX + ":" + name.replace(' ', '_')

  fun getFkgRawTextUrl(name: String) = prefixNames[KG_RAW_TEXT_PREFIX] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgRawText(name: String) = KG_RAW_TEXT_PREFIX + ":" + name.replace(' ', '_')

  fun getFkgResourceUrl(name: String) = prefixNames[KG_RESOURCE_PREFIX] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgResource(name: String) = KG_RESOURCE_PREFIX + ":" + name.replace(' ', '_')

  fun getFkgOntologyPropertyUrl(name: String) =
      prefixNames[KG_ONTOLOGY_PREFIX] +
          CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name.replace(' ', '_'))

  fun getFkgOntologyClassUrl(name: String) =
      prefixNames[KG_ONTOLOGY_PREFIX] +
          CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name.replace(' ', '_'))

  fun getFkgOntologyProperty(name: String) =
      KG_ONTOLOGY_PREFIX + ":" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name.replace(' ', '_'))

  fun getFkgOntologyClass(name: String) =
      KG_ONTOLOGY_PREFIX + ":" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name.replace(' ', '_'))

  fun convertFkgResourceUrl(url: String): String {
    if (url.startsWith("http://fa.wikipedia.org/wiki/")
        || url.startsWith("fa.wikipedia.org/wiki/"))
      return prefixNames[KG_RESOURCE_PREFIX] + url.substringAfterLast("/")
    return url
  }

  fun convertFkgOntologyUrl(url: String): String {
    return prefixNames[KG_ONTOLOGY_PREFIX] + url.substringAfterLast("/")
  }

  // this is different from dbpedia. they converts xx_yy to xxYy. but we don't change that.
  // because persian letters has not upper case
  fun generateOntologyProperty(rawProperty: String, prefix: String = "dbo")
      = prefix + ":" + PropertyNormaller.removeDigits(rawProperty).replace(' ', '_')

  fun convertFkgProperty(property: String): String? {
    if (property.contains("://")) return property
    val p =
        if (!property.contains(":")) generateOntologyProperty(property, KG_AUTO_PROPERTY_PREFIX)
        else property.replace(' ', '_')
    return prefixToUri(p)
  }

  fun isUrlFast(str: String?): Boolean {
    if (str == null) return false
    if ((str.startsWith("http://") || str.startsWith("https://")) && !str.contains(' ')) return true
    return false
  }
}