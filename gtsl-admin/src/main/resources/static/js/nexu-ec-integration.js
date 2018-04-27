var NexU = (function() {

    var latestVersion = "1.6.1";
    
    var bindingPorts = "9795";
    var bindingPortsHttps = "9895";
    
    var targetURLScheme;
    var runningPort;
    var runningVersion;
    var isInitialized;
    var that;
    
    function NexU(successCallback, oldVersionDetectedCallback, notInstalledCallback, cannotLoadScriptCallback) {
        that = this;
        isInitialized = false;
        targetURLScheme = _getTargetURLScheme();
        _initRunningPortAndVersion(targetURLScheme, successCallback, oldVersionDetectedCallback, notInstalledCallback, cannotLoadScriptCallback);
    }
    
    function _getTargetURLScheme() {
        var protocol = window.location.protocol;
        if(protocol === "https:") {
            return "https";
        } else {
            return "http";
        }
    }
    
    function _initRunningPortAndVersion(scheme, successCallback, oldVersionDetectedCallback, notInstalledCallback, cannotLoadScriptCallback) {
        var portsStr = (scheme === "https") ? bindingPortsHttps : bindingPorts;
        _initRunningPortAndVersionRecursive(scheme, successCallback, oldVersionDetectedCallback, notInstalledCallback,
                cannotLoadScriptCallback, 0, portsStr.split(","));
    }
    
    function _initRunningPortAndVersionRecursive(scheme, successCallback, oldVersionDetectedCallback, notInstalledCallback,
            cannotLoadScriptCallback, index, ports) {
        port = ports[index].trim();
        $.get(scheme + "://localhost:" + port + "/nexu-info", function(data) {
            runningPort = port;
            runningVersion = data.version;
            if(runningVersion === latestVersion) {
                $.getScript(scheme + "://localhost:" + port + "/nexu.js")
                    .done(function() {
                        isInitialized = true;
                        successCallback(that);
                    })
                    .fail(function() {
                        isInitialized = true;
                        cannotLoadScriptCallback(that);
                    });
            } else {
                isInitialized = true;
                oldVersionDetectedCallback(that);
            }
        }).fail(function() {
            if(index+1 < ports.length) {
                _initRunningPortAndVersionRecursive(scheme, successCallback, oldVersionDetectedCallback, notInstalledCallback,
                        cannotLoadScriptCallback, index+1, ports);
            } else {
                isInitialized = true;
                notInstalledCallback(that);
            }
        });
    }
    
    NexU.prototype.getTargetURLScheme = function() {
        return targetURLScheme;
    };
    
    NexU.prototype.getRunningPort = function() {
        return runningPort;
    };
    
    NexU.prototype.getRunningVersion = function() {
        return runningVersion;
    };
    
    NexU.prototype.isInitialized = function() {
        return isInitialized;
    };
    
    NexU.prototype.isInstalled = function() {
        return typeof runningPort !== 'undefined';
    };
    
    return NexU;
}());
