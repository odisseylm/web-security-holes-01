package com.mvv.security.xxe

import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.ByteArrayInputStream
import java.io.StringWriter
//import javax.servlet.ServletRequest
import jakarta.servlet.ServletRequest
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


fun unsafeDocumentBuilderFactory(): DocumentBuilderFactory {
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()

    // it is disabled by default
    documentBuilderFactory.isXIncludeAware = true
    // it is enabled or disabled by default ???
    documentBuilderFactory.isNamespaceAware = true

    // it is enabled by default
    // !!! XEE attack is allowed by DEFAULT!!!
    documentBuilderFactory.isExpandEntityReferences = true

    return documentBuilderFactory
}
fun unsafeDocumentBuilder(): DocumentBuilder = unsafeDocumentBuilderFactory().newDocumentBuilder()


fun safeDocumentBuilderFactory(): DocumentBuilderFactory {
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()

    // it is disabled by default
    documentBuilderFactory.isXIncludeAware = false
    // it is enabled or disabled by default ???
    documentBuilderFactory.isNamespaceAware = false

    // it is enabled by default
    // !!! XEE attack is allowed by DEFAULT!!!
    documentBuilderFactory.isExpandEntityReferences = false

    return documentBuilderFactory
}
fun safeDocumentBuilder(): DocumentBuilder = safeDocumentBuilderFactory().newDocumentBuilder()


fun defaultDocumentBuilderFactory(): DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
fun defaultDocumentBuilder(): DocumentBuilder = defaultDocumentBuilderFactory().newDocumentBuilder()

//fun String.toXml(): Document = unsafeDocumentBuilder().toXml(this)

fun DocumentBuilder.toXml(xml: String): Document = this.parse(ByteArrayInputStream(xml.toByteArray(Charsets.UTF_8)))


fun Node.toXmlString(): String {

    val t = TransformerFactory.newInstance().newTransformer()
    val w = StringWriter()
    t.transform(DOMSource(this), StreamResult(w))

    return w.toString()
}

fun Node.printXml() {
    println(this.toXmlString())
}

fun ServletRequest.getContentAsString(): String =
    this.reader.readText()
