package ir.ac.iust.dml.kg.raw.utils

import java.util.*

object URIs {

  val prefixNames = mutableMapOf<String, String>()
  val prefixAddresses = mutableMapOf<String, String>()

  val fkgResourcePrefix = "fkgr"
  val fkgOntologyPrefix = "fkgo"
  val fkgNotMappedPropertyPrefix = "fkgp"
  val fkgManualTriplePrefix = "fkgm"
  val fkgTablePrefix = "fkgl"
  val fkgRawTextPrefix = "fkgt"

  val defaultContext = "http://fkg.iust.ac.ir/"

  private val adjacentSpaceRegex = Regex("([\u00A0]|\\s)+")

  val typeOfAllPropertiesPrefixed = "owl:ObjectProperty"
  val typeOfAllNotMappedPropertiesPrefixed = "rdf:Property"
  // "rdfs:Resource" equivalent to "owl:Thing" in OWL:Full
  val typeOfAllResourcesPrefixed = "owl:NamedIndividual"
  val typeOfAllClassesPrefixed = "owl:Class" // equivalent to "rdfs:Class" in OWL:Full
  val subClassOfPrefixed = "rdfs:subClassOf"
  val commentPrefixed = "rdfs:comment"
  val classTreePrefixed = "fkgo:classTree"
  val labelPrefixed = "rdfs:label"
  val propertyDomainPrefixed = "rdfs:domain"
  val propertyRangePrefixed = "rdfs:range"
  val equivalentPropertyPrefixed = "owl:equivalentProperty"
  val equivalentClassPrefixed = "owl:equivalentClass"
  val disjointWithPrefixed = "owl:disjointWith"
  val typePrefixed = "rdf:type"
  val variantLabelPrefixed = "fkgo:variantLabel"
  val instanceOfPrefixed = "rdf:instanceOf"
  val disambiguatedFromPrefixed = "fkgo:wikiDisambiguatedFrom"
  val redirectPrefixed = "fkgo:wikiPageRedirects"
  val namePrefixed = "foaf:name"

  val typeOfAllProperties: String
  val typeOfAllNotMappedProperties: String
  val typeOfAllResources: String
  val typeOfAllClasses: String
  val subClassOf: String
  val comment: String
  val classTree: String
  val label: String
  val propertyDomain: String
  val propertyRange: String
  val equivalentProperty: String
  val equivalentClass: String
  val disjointWith: String
  val type: String
  val variantLabel: String
  val instanceOf: String
  val disambiguatedFrom: String
  val redirect: String
  val name: String

  init {
    reload()
    typeOfAllProperties = prefixedToUri(typeOfAllPropertiesPrefixed)!!
    typeOfAllNotMappedProperties = prefixedToUri(typeOfAllNotMappedPropertiesPrefixed)!!
    typeOfAllResources = prefixedToUri(typeOfAllResourcesPrefixed)!!
    typeOfAllClasses = prefixedToUri(typeOfAllClassesPrefixed)!!
    subClassOf = prefixedToUri(subClassOfPrefixed)!!
    comment = prefixedToUri(commentPrefixed)!!
    classTree = prefixedToUri(classTreePrefixed)!!
    label = prefixedToUri(labelPrefixed)!!
    propertyDomain = prefixedToUri(propertyDomainPrefixed)!!
    propertyRange = prefixedToUri(propertyRangePrefixed)!!
    equivalentProperty = prefixedToUri(equivalentPropertyPrefixed)!!
    equivalentClass = prefixedToUri(equivalentClassPrefixed)!!
    disjointWith = prefixedToUri(disjointWithPrefixed)!!
    type = prefixedToUri(typePrefixed)!!
    variantLabel = prefixedToUri(variantLabelPrefixed)!!
    instanceOf = prefixedToUri(instanceOfPrefixed)!!
    disambiguatedFrom = prefixedToUri(disambiguatedFromPrefixed)!!
    redirect = prefixedToUri(redirectPrefixed)!!
    name = prefixedToUri(namePrefixed)!!
  }

  fun reload() {
    val prefixServices = Properties()
    prefixServices.load(this.javaClass.getResourceAsStream("/prefixes.properties"))
    prefixServices.keys.forEach {
      prefixNames[it as String] = prefixServices.getProperty(it)!!
      prefixAddresses[prefixServices.getProperty(it)!!] = it
    }
  }

  fun replaceAllPrefixesInString(text: String): String {
    var result = text
    prefixAddresses.keys.asSequence()
        .filter { result.contains(it) }
        .forEach { result = result.replace(it, prefixAddresses[it]!! + ":") }
    return result
  }

  fun prefixedToUri(source: String?): String? {
    if (source == null || !source.contains(':') || source.startsWith("http")) return source
    val splits = source.split(":")
    var address = prefixNames[splits[0]]
    if (address != null && !address.startsWith("http://") && !address.startsWith("https://"))
      address = "http://" + address
    return if (address == null) splits[1] else address + splits[1]
  }

  fun getFkgManualUri(name: String) = prefixNames[fkgManualTriplePrefix] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgManualPrefixed(name: String) = fkgManualTriplePrefix + ":" + name.replace(' ', '_')

  fun getFkgTableUri(name: String) = prefixNames[fkgTablePrefix] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgTablePrefixed(name: String) = fkgTablePrefix + ":" + name.replace(' ', '_')

  fun getFkgRawTextUri(name: String) = prefixNames[fkgRawTextPrefix] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgRawTextPrefixed(name: String) = fkgRawTextPrefix + ":" + name.replace(' ', '_')

  fun getFkgResourceUri(name: String) = prefixNames[fkgResourcePrefix] + name.replace(adjacentSpaceRegex, "_")

  fun getFkgResourcePrefixed(name: String) = fkgResourcePrefix + ":" + name.replace(' ', '_')

  fun getFkgOntologyPropertyUri(name: String) = prefixNames[fkgOntologyPrefix] + camelCase(false, name.replace(' ', '_'))

  fun getFkgOntologyClassUri(name: String) = prefixNames[fkgOntologyPrefix] + camelCase(true, name.replace(' ', '_'))

  fun getFkgOntologyPropertyPrefixed(name: String) = fkgOntologyPrefix + ":" + camelCase(false, name.replace(' ', '_'))

  fun getFkgOntologyClassPrefixed(name: String) = fkgOntologyPrefix + ":" + camelCase(true, name.replace(' ', '_'))

  fun convertWikiUriToResourceUri(uri: String): String {
    if (uri.startsWith("http://fa.wikipedia.org/wiki/")
        || uri.startsWith("fa.wikipedia.org/wiki/"))
      return prefixNames[fkgResourcePrefix] + uri.substringAfterLast("/")
    return uri
  }

  fun convertAnyUrisToFkgOntologyUri(uri: String): String {
    return prefixNames[fkgOntologyPrefix] + uri.substringAfterLast("/")
  }

  // this is different from dbpedia. they converts xx_yy to xxYy. but we don't change that.
  // because persian letters has not upper case
  fun generateOntologyPropertyPrefixed(rawProperty: String, prefix: String)
      = prefix + ":" + PropertyNormaller.removeDigits(rawProperty).replace(' ', '_')

  fun convertToNotMappedFkgPropertyUri(property: String): String? {
    if (property.contains("://")) return property
    val p =
        if (!property.contains(":")) generateOntologyPropertyPrefixed(property, fkgNotMappedPropertyPrefix)
        else property.replace(' ', '_')
    return prefixedToUri(p)
  }

  fun isHttpUriFast(str: String?): Boolean {
    if (str == null) return false
    if ((str.startsWith("http://") || str.startsWith("https://")) && !str.contains(' ')) return true
    return false
  }

  private fun camelCase(uppercase: Boolean, string: String): String {
    val builder = StringBuilder()
    string.forEachIndexed { index, c ->
      if (index == 0) builder.append(if (uppercase) c.toUpperCase() else c)
      else if (string[index - 1] == '_') builder.append(c.toUpperCase())
      else {
        val char = string[index]
        if (char != '_') builder.append(char)
      }
    }
    return builder.toString()
  }

}