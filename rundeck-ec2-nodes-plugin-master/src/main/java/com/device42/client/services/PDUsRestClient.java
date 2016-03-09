package com.device42.client.services;

import com.device42.client.model.PDU;
import com.device42.client.parser.BasicPDUsJsonParser;
import com.device42.client.services.parameters.PDUParameters;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Iterator;
import java.util.List;

public class PDUsRestClient extends AbstractAsynchronousRestClient {
    private BasicPDUsJsonParser pdusJsonParser = new BasicPDUsJsonParser();

    PDUsRestClient(String baseUrl, CloseableHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public List<PDU> getPDUs() {
        return get("/api/1.0/pdus/", pdusJsonParser, new PDUParameters.PDUParametersBuilder().build());
    }

    public List<PDU> getPDUsById(long id) {
        List<PDU> pdus = getPDUs();
        Iterator<PDU> iter = pdus.iterator();
        while (iter.hasNext()) {
            PDU pdu = iter.next();
            if (pdu.getId() != id) {
                iter.remove();
            }
        }
        return pdus;
    }

    public List<PDU> getPDUsByName(String name) {
        List<PDU> pdus = getPDUs();
        Iterator<PDU> iter = pdus.iterator();
        while (iter.hasNext()) {
            PDU pdu = iter.next();
            if (!pdu.getName().equals(name)) {
                iter.remove();
            }
        }
        return pdus;
    }
}
