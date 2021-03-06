package com.liser.common.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.2.4
 * 2018-04-10T20:56:26.032+08:00
 * Generated source version: 3.2.4
 *
 */
@WebService(targetNamespace = "http://webservice.common.com.liser.com/", name = "LiserBusiness")
@XmlSeeAlso({ObjectFactory.class})
public interface LiserBusiness {

    @WebMethod
    @RequestWrapper(localName = "getPersonInfo", targetNamespace = "http://webservice.common.com.liser.com/", className = "com.com.liser.common.webservice.GetPersonInfo")
    @ResponseWrapper(localName = "getPersonInfoResponse", targetNamespace = "http://webservice.common.com.liser.com/", className = "com.com.liser.common.webservice.GetPersonInfoResponse")
    @WebResult(name = "return", targetNamespace = "")
    public String getPersonInfo(
            @WebParam(name = "inputxml", targetNamespace = "")
                    String inputxml
    );
}
