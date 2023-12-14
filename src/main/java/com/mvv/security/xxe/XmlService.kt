package com.mvv.security.xxe

import com.mvv.security.validateClientIsLocal
import org.apache.commons.text.StringEscapeUtils.escapeXml10
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.xml.parsers.DocumentBuilder


class XmlService (private val documentBuilder: DocumentBuilder) {


    fun serviceWhichReturnsFullContentInErrorWithPossibleXxeLocalFile(request: HttpServletRequest, response: HttpServletResponse) {
        // Actually web.xml already contains such protection, by to make sure if somebody copy it to other web-app.
        //
        // Add other YOUR local address if you need
        validateClientIsLocal(request)

        val inputString: String = request.getContentAsString()
        if (inputString.isBlank()) {
            response.status = 403
            response.writer.write("<error>No input</error>")
        } else {
            try {
                val result = serviceWhichReturnsFullContentInErrorWithPossibleXxeLocalFile(inputString)
                response.writer.write(result)
            } catch (ex: Exception) {
                response.status = 500 // should be different kind of errors

                // SECURITY HOLE - we paste to client ANY full errors and in our case
                // error/exception contains xxe-injected system file.
                response.writer.write("<error>${escapeXml10(ex.toString())}</error>")
            }
        }
    }


    /*
     * Expected XML:
     * <transfer>
     *   <amount/>
     *   <accountFrom/>
     *   <accountTo/>
     * </transfer>
     *
     */
    private fun serviceWhichReturnsFullContentInErrorWithPossibleXxeLocalFile(inputXmlString: String): String {
        println("serviceWhichReturnsFullContentInErrorWithPossibleXxeLocalFile: inputXmlString = $inputXmlString")

        val xml = documentBuilder.toXml(inputXmlString)
        println("serviceWhichReturnsFullContentInErrorWithPossibleXxeLocalFile: inputXml = ${xml.toXmlString()}")

        val amount = xml.getElementByTagName("transfer")?.getElementByTagName("amount")?.textContent

        if (amount.isNullOrBlank())
            throw IllegalArgumentException("transfer amount is missed in input ${xml.toXmlString()}")
        if (!amount.isAmountNumber())
            throw IllegalArgumentException("transfer amount has wrong format [$amount].")

        // There should be real response
        return "<success>"
    }

}

private fun String.isAmountNumber(): Boolean =
    try { this.trim().toInt(); true } catch (_: Exception) { false}

fun Node.getElementByTagName(tagName: String): Node? {
    val els = this.childNodes.asIterable().filter { it.nodeType == Node.ELEMENT_NODE && it.nodeName == tagName }.toList()
    return if (els.size == 1) els[0] else null
}

private fun NodeList.asIterable(): Iterable<Node> = Iterable { this.asIterator() }

private fun NodeList.asIterator(): Iterator<Node> {
    var index = -1
    return object : Iterator<Node> {
        override fun hasNext(): Boolean = index < this@asIterator.length - 1
        override fun next(): Node = this@asIterator.item(++index)
    }
}
