metadata {
    definition(name: "HTTP GET Sample Driver", namespace: "example", author: "Your Name") {
        capability "Initialize"
        capability "Refresh"
    }
    preferences {
        input name: "serverUrl", type: "string", title: "API URL", required: true
    }
}

def installed() {
    log.info "Installed"
}

def updated() {
    log.info "Updated"
    initialize()
}

def initialize() {
    log.info "Initializing driver"
    refresh()
}

def refresh() {
    log.debug "Refreshing data from ${serverUrl}"
    if(!serverUrl) {
        log.warn "No URL set in preferences!"
        return
    }

    try {
        httpGet([uri: serverUrl, contentType: "application/json"]) { resp ->
            if (resp.success) {
                log.debug "Response data: ${resp.data}"

                // Example: parse a JSON field
                if(resp.data?.status) {
                    sendEvent(name: "status", value: resp.data.status.toString())
                }
            } else {
                log.warn "HTTP request failed with status ${resp.status}"
            }
        }
    } catch (Exception e) {
        log.error "Error during GET request: $e"
    }
}
