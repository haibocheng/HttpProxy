package de.lusiardi.proxy.scripting;

import de.lusiardi.proxy.Configuration;
import de.lusiardi.proxy.data.HttpHost;
import de.lusiardi.proxy.data.HttpRequest;
import de.lusiardi.proxy.data.HttpResponse;
import de.lusiardi.proxy.writers.HttpRequestWriter;
import de.lusiardi.proxy.writers.HttpResponseWriter;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * Class that contains functions that are provided to the javascript part of the
 * proxy.
 *
 * @author Joachim Lusiardi
 */
public class ProvidedFunctions {

    private static Logger logger = Logger.getLogger(ProvidedFunctions.class);
    private static Logger scriptLogger;
    private HttpRequestWriter requestWriter;
    private HttpResponseWriter responseWriter;
    private Configuration config;
    private Socket target = null;
    private Socket source;

    /**
     * Constructs with the given source socket and configuration.
     *
     * @param source the socket between proxy and client
     * @param config the configuration of the proxy
     */
    public ProvidedFunctions(Socket source, Configuration config) {
        scriptLogger = Logger.getLogger("SCRIPT");
        requestWriter = new HttpRequestWriter();
        responseWriter = new HttpResponseWriter();
        this.config = config;
        this.source = source;
    }

    /**
     * Return the socket to the target host. This will non-null after
     * {@link #sendRequest(de.lusiardi.soa.proxy.data.HttpRequest)} was called.
     *
     * @return the open socket
     */
    Socket getTargetSocket() {
        return target;
    }

    /**
     * Logs a request to the script logger.
     *
     * @param request the request
     */
    public void logRequest(HttpRequest request) {
        scriptLogger.info("\n" + requestWriter.writeToString(request, "      "));
    }

    /**
     * Logs a response to the script logger.
     *
     * @param response the response
     */
    public void logResponse(HttpResponse response) {
        scriptLogger.info("\n" + responseWriter.writeToString(response, "      "));
    }

    /**
     * Writes a debug message to the script logger.
     *
     * @param message the string to write
     */
    public void debug(String message) {
        scriptLogger.debug(message);
    }

    /**
     * Analyses a request and send it to the correct target host. If the request
     * has a host http header, the value from the header is used as target, else
     * the configured default target is used.
     *
     * @param request the request to send
     * @see Configuration#getDefaultTarget()
     */
    public void sendRequest(HttpRequest request) {
        try {
            HttpHost targetHost = request.getHeaders().getHost();
            if (targetHost != null && !targetHost.equals(config.getListeningHost())) {
                logger.debug("using '" + targetHost + "' as target");
                target = new Socket(targetHost.getHost(), targetHost.getPort());
            } else {
                logger.debug("using '" + config.getDefaultTarget() + "' as target");
                target = new Socket(config.getDefaultTarget().getHost(), config.getDefaultTarget().getPort());
            }
            final OutputStream targetOutput = target.getOutputStream();
            requestWriter.writeToStream(request, targetOutput);
            targetOutput.flush();
        } catch (Exception ex) {
            logger.error("Fail", ex);
        }
    }

    /**
     * Sends the given response object to the client of the proxy.
     *
     * @param response the response to send
     */
    public void sendResponse(HttpResponse response) {
        try {
            final OutputStream sourceOutput = source.getOutputStream();
            responseWriter.writeToStream(response, sourceOutput);
            sourceOutput.flush();
        } catch (Exception ex) {
            logger.error("Fail", ex);
        }
    }
    
    public void removeHeaderFrom(String header, HttpRequest request) {
        request.getHeaders().removeHeader(header);
    }
}
