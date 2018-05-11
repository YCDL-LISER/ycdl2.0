package com.liser.common.webservice;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 3.2.4
 * 2018-04-10T20:56:26.112+08:00
 * Generated source version: 3.2.4
 *
 */
@WebServiceClient(name = "LiserBusiness",
                  wsdlLocation = "http://localhost:8080/ssm/service/liserBusiness?wsdl",
                  targetNamespace = "http://webservice.common.com.liser.com/")
public class LiserBusiness_Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://webservice.common.com.liser.com/", "LiserBusiness");
    public final static QName LiserBusinessImplPort = new QName("http://webservice.common.com.liser.com/", "LiserBusinessImplPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/ssm/service/liserBusiness?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(LiserBusiness_Service.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/ssm/service/liserBusiness?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public LiserBusiness_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public LiserBusiness_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public LiserBusiness_Service() {
        super(WSDL_LOCATION, SERVICE);
    }

    public LiserBusiness_Service(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public LiserBusiness_Service(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public LiserBusiness_Service(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns LiserBusiness
     */
    @WebEndpoint(name = "LiserBusinessImplPort")
    public LiserBusiness getLiserBusinessImplPort() {
        return super.getPort(LiserBusinessImplPort, LiserBusiness.class);
    }

    /**
     *
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns LiserBusiness
     */
    @WebEndpoint(name = "LiserBusinessImplPort")
    public LiserBusiness getLiserBusinessImplPort(WebServiceFeature... features) {
        return super.getPort(LiserBusinessImplPort, LiserBusiness.class, features);
    }

}