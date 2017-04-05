package ir.ac.iust.dml.kg.raw.utils.dump.wiki

import javax.xml.bind.annotation.XmlElement

data class Contributor(
      @XmlElement var username: String? = null,
      @XmlElement var id: Long? = null)