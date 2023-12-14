package com.mvv.security

import javax.servlet.ServletRequest


fun validateClientIsLocal(request: ServletRequest) {
    // Actually web.xml already contains such protection, by to make sure if somebody copy it to other web-app...
    //
    // Add other YOUR local address if you need
    val allowedClients: Set<String> = HashSet(mutableListOf("0:0:0:0:0:0:0:1", "::1", "127.0.0.1"))

    val remoteAddr = request.remoteAddr
    if (remoteAddr !in allowedClients) {
        System.err.println("Access is allowed only for local clients.")
        throw IllegalArgumentException("Forbidden.")
    }
}
