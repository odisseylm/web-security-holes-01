package com.mvv.security.xxe

import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.ByteArrayInputStream
import java.io.StringWriter
//import javax.servlet.ServletRequest
import jakarta.servlet.ServletRequest
import javax.xml.XMLConstants
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
    //documentBuilderFactory.isNamespaceAware = false

    // it is enabled by default
    // !!! XEE attack is allowed by DEFAULT!!!
    documentBuilderFactory.isExpandEntityReferences = false

    // As stated in the documentation "Feature for Secure Processing (FSP)" is the central mechanism to
    // help safeguard XML processing. It instructs XML processors, such as parsers, validators,
    // and transformers, to try and process XML securely. This can be used as an alternative to
    // dbf.setExpandEntityReferences(false); to allow some safe level of Entity Expansion
    // Exists from JDK6.
    documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)

    val featuresToDisable = arrayOf(
        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
        // JDK7+ - http://xml.org/sax/features/external-general-entities
        //This feature has to be used together with the following one, otherwise it will not protect you from XXE for sure
        "http://xml.org/sax/features/external-general-entities",

        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
        // JDK7+ - http://xml.org/sax/features/external-parameter-entities
        //This feature has to be used together with the previous one, otherwise it will not protect you from XXE for sure
        "http://xml.org/sax/features/external-parameter-entities",

        // Disable external DTDs as well
        "http://apache.org/xml/features/nonvalidating/load-external-dtd"
    )

    featuresToDisable.forEach { documentBuilderFactory.setFeature(it, false) }

    // http://apache.org/xml/features/disallow-doctype-decl

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
