package com.mvv.security.xxe

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.text.Charsets.UTF_8


class XxeTest {

    @Test
    fun testXxe() {

        val documentBuilderFactory = unsafeDocumentBuilderFactory()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()

        val file = "/etc/hosts"
        val xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE foo [ <!ENTITY xxe SYSTEM "file://$file"> ]>
                <root>
                  <child1>
                    <sub-child1>&xxe;</sub-child1>
                  </child1>
                </root>
                """.trimIndent()

        println("Infected input XML file: \n$xml\n")

        val document = documentBuilder.parse(ByteArrayInputStream(xml.toByteArray(UTF_8)))
        println("\nResult XML file: \n${document.toXmlString()}")

        assertThat(document.toXmlString()).contains("127.0.0.1")
        assertThat(document.getElementByTagName("root")?.textContent).contains("127.0.0.1")
    }


    @Test
    fun testXInclude() {

        val documentBuilderFactory = unsafeDocumentBuilderFactory()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()

        val file = "/etc/hosts"
        /*
        val xml0 = """
                <?xml version="1.0" encoding="UTF-8"?>
                <foo xmlns:xi="http://www.w3.org/2001/XInclude">
                  <xi:include parse="text" href="file://$file" />
                </foo>
                """.trimIndent()
        */

        val xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <root>
                  <child1>
                    <sub-child1>
                      <include xmlns="http://www.w3.org/2001/XInclude" href="file://$file" parse="text"/>
                    </sub-child1>
                  </child1>
                </root>
                """.trimIndent()

        println("Infected input XML file: \n$xml\n")

        val document = documentBuilder.parse(ByteArrayInputStream(xml.toByteArray(UTF_8)))
        println("\nResult XML file: \n${document.toXmlString()}")

        assertThat(document.toXmlString()).contains("127.0.0.1")
        assertThat(document.getElementByTagName("root")?.textContent).contains("127.0.0.1")
    }


    @Test
    fun testXInclude_withAlternative() {

        // See https://ru.wikipedia.org/wiki/XInclude

        val documentBuilderFactory = unsafeDocumentBuilderFactory()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()

        val nonExistentFile = "/etc/hosts-nonexistent"
        val file = "/etc/hosts"

        val xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <root>
                  <div xmlns:xi="http://www.w3.org/2001/XInclude">
                    <xi:include href="file://$nonExistentFile" parse="text">
                      <xi:fallback>
                        <xi:include href="file://$file" parse="text">
                          <xi:fallback><strong>Error: </strong>Both resources are inaccessible.</xi:fallback>
                        </xi:include>
                      </xi:fallback>
                    </xi:include>
                  </div>
                </root>
                """.trimIndent()

        println("Infected input XML file: \n$xml\n")

        val document = documentBuilder.parse(ByteArrayInputStream(xml.toByteArray(UTF_8)))
        println("\nResult XML file: \n${document.toXmlString()}")

        assertThat(document.toXmlString()).contains("127.0.0.1")
        assertThat(document.getElementByTagName("root")?.textContent).contains("127.0.0.1")
    }

}


//private fun Node.toXmlString(): String {
//
//    val t = TransformerFactory.newInstance().newTransformer()
//    val w = StringWriter()
//    t.transform(DOMSource(this), StreamResult(w))
//
//    return w.toString()
//}
//
//private fun Node.printXml() {
//    println(this.toXmlString())
//}

/*
private fun Node.printXml_2() = this.printXmlNodesImpl("", HashSet<Node>())
// T O D O: It shows ONLY node name and content.
//          Add support of ATTRIBUTE_NODE, CDATA_SECTION_NODE, ENTITY_REFERENCE_NODE,
//                      ENTITY_NODE, PROCESSING_INSTRUCTION_NODE, COMMENT_NODE, DOCUMENT_NODE,
//                      DOCUMENT_TYPE_NODE, DOCUMENT_FRAGMENT_NODE, NOTATION_NODE
private fun Node.printXmlNodesImpl(padding: String, printedNodes: MutableSet<Node>) {

    val thisNodeAlreadyPrinted = this in printedNodes
    printedNodes.add(this)

    val nodeList = this.childNodes
    for (i in 0 until nodeList.length) {
        val item = nodeList.item(i)
        val rawTextContent = item.nodeValue ?: ""
        val textContentLines = rawTextContent.split('\n')
        //val textContent = if (item.hasChildNodes()) item.firstChild?.nodeValue ?: "" else ""

        //if (item.nodeType == Node.ELEMENT_NODE)
        if (item.nodeType != Node.TEXT_NODE)
            println("$padding<${item.nodeName}>")

        if (rawTextContent.isNotBlank())
            textContentLines.forEach { println("$padding  $it") }

        if (!thisNodeAlreadyPrinted) {
            item.printXmlNodesImpl("$padding  ", printedNodes)
        }

        //if (item.nodeType == Node.ELEMENT_NODE)
        if (item.nodeType != Node.TEXT_NODE)
            println("$padding</${item.nodeName}>")
    }
}
*/
