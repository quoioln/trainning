package com.ntt.smartpbx.csv;

import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * 名称: CSVResultIncomingGroup class
 * 機能概要: export the CSV file.
 */
public class CSVResultIncomingGroup implements Result, StrutsStatics {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    private static Logger log = Logger.getLogger(CSVResultIncomingGroup.class);


    /**
     * Execute the export CSV file action.
     *
     * @param invocation The ActionInvocation.
     * @throws Exception
     */
    @Override
    public void execute(ActionInvocation invocation) {
        try {
            CSVProvider csvProvider = (CSVProvider) invocation.getAction();
            Vector data = csvProvider.getCSVData();
            String[] headers = csvProvider.getCSVHeaders();

            HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + csvProvider.getCSVFileName() + "\"");
            response.setHeader("Refresh", "0");
            ServletOutputStream outstream = response.getOutputStream();
            CSVHandler.exportCSVIncomingGroup(outstream, data, headers);
            outstream.flush();
        } catch (Exception e) {
            log.debug("Export CSV error: " + e.getMessage());
        }
    }

}

//(C) NTT Communications  2014  All Rights Reserved
