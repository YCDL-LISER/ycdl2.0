
package com.liser.common.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.com.liser.common.webservice package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetPersonInfo_QNAME = new QName("http://webservice.common.com.liser.com/", "getPersonInfo");
    private final static QName _GetPersonInfoResponse_QNAME = new QName("http://webservice.common.com.liser.com/", "getPersonInfoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.com.liser.common.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetPersonInfo }
     * 
     */
    public GetPersonInfo createGetPersonInfo() {
        return new GetPersonInfo();
    }

    /**
     * Create an instance of {@link GetPersonInfoResponse }
     * 
     */
    public GetPersonInfoResponse createGetPersonInfoResponse() {
        return new GetPersonInfoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPersonInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.com.liser.com/", name = "getPersonInfo")
    public JAXBElement<GetPersonInfo> createGetPersonInfo(GetPersonInfo value) {
        return new JAXBElement<GetPersonInfo>(_GetPersonInfo_QNAME, GetPersonInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPersonInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.common.com.liser.com/", name = "getPersonInfoResponse")
    public JAXBElement<GetPersonInfoResponse> createGetPersonInfoResponse(GetPersonInfoResponse value) {
        return new JAXBElement<GetPersonInfoResponse>(_GetPersonInfoResponse_QNAME, GetPersonInfoResponse.class, null, value);
    }

}
