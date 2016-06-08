package com.ntt.smartpbx.action;

import java.awt.Font;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.TrafficReportData;
import com.ntt.smartpbx.model.db.TrafficInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: TrafficReportGraphAction class.
 * 機能概要: Process the traffic report screen
 */
public class TrafficReportGraphAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(TrafficReportGraphAction.class);
    // End step 2.5 #1946
    /** The start time string */
    private String startTimeString;
    /** The end time string */
    private String endTimeString;
    /** The start time */
    private Timestamp startTime;
    /** The end time */
    private Timestamp endTime;
    /** The action type */
    private int actionType;
    /** JFreeChart */
    private JFreeChart chart;


    /**
     * Default constructor
     */
    public TrafficReportGraphAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
        this.startTimeString = Const.EMPTY;
        this.endTimeString = Const.EMPTY;
        this.startTime = null;
        this.endTime = null;
        this.actionType = ACTION_INIT;
        this.error = null;
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: TrafficReportGraph.jsp
     *      VIEW: TrafficReportGraph.jsp
     *      INPUT: TrafficReportGraph.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        // START Step1.x #1091
        /*
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }
         */
        // END Step1.x #1091
        //Check input date
        if (!validateSearchFields(startTimeString, endTimeString) || !checkTime()) {
            return ERROR;
        }

        switch (actionType) {
            case ACTION_VIEW:

                return doView();
            default:
                return doInit();
        }
    }

    /**
     * The view method of action
     *
     * @return
     *      VIEW: TrafficReportGraph.jsp
     *      INPUT: TrafficReportGraph.jsp
     *      ERROR: SystemError.jsp
     */
    private String doView() {
        try {
            chart = (JFreeChart) session.get(Const.Session.G1202_CHART);
            session.remove(Const.Session.G1202_CHART);
        } catch (Exception e) {
            return ERROR;
        }
        return VIEW;
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: TrafficReportGraph.jsp
     *      INPUT: TrafficReportGraph.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        // get the NNumberId from session
        long nNumberInfoId = 0;
        if (session.get(Const.Session.N_NUMBER_INFO_ID) != null) {
            try {
                nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
            } catch (NumberFormatException e) {
                log.debug("getData error: " + e.getMessage());
                return ERROR;
            }
        }

        try {
            chart = createChart(nNumberInfoId);
            if (chart == null) {
                if (error == null) {
                    return INPUT;
                }
                //START #616
                return ERROR;
                //END #616
            }
        } catch (Exception e) {
            error.setErrorCode(e.getMessage());
            return ERROR;
        }
        session.put(Const.Session.G1202_CHART, chart);

        return SUCCESS;
    }

    /**
     * Getter of chart.
     *
     * @return chart
     */
    public JFreeChart getChart() {
        return chart;
    }

    /**
     * Create chart.
     *
     * @param nNumberInfoId
     * @return
     * @throws Exception
     */
    private JFreeChart createChart(long nNumberInfoId) throws Exception {
        XYDataset dataset = createDataset(nNumberInfoId);
        if (dataset == null) {
            return null;
        }
        ValueAxis xAxis = new DateAxis(Const.Chart.X_TITLE());
        ValueAxis yAxis = new NumberAxis(Const.Chart.Y_TITLE());

        //START #634
        xAxis.setUpperMargin(0.1);
        //END #634

        //START #622
        xAxis.setLabelFont(new Font("Sans-serif", Font.BOLD, 13));
        yAxis.setLabelFont(new Font("Sans-serif", Font.BOLD, 14));
        //START step1.x #1051
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //END step1.x #1051
        //Start step1.x #1082
        ((DateAxis)xAxis).setDateFormatOverride(new SimpleDateFormat("yyyy/MM/dd HH:mm"));
        //End step1.x #1082
        //END #622
        if (dataset.getSeriesCount() == 0) {
            xAxis.setAutoTickUnitSelection(false);
            yAxis.setAutoTickUnitSelection(false);
        }
        // set my chart variable
        JFreeChart localJFreeChart = new JFreeChart(Const.Chart.CHART_NAME(), JFreeChart.DEFAULT_TITLE_FONT, new XYPlot(dataset, xAxis, yAxis, new StandardXYItemRenderer(StandardXYItemRenderer.LINES)), true);
        localJFreeChart.setBackgroundPaint(java.awt.Color.pink);
        //START #634
        RectangleInsets offset = new RectangleInsets(0.0, 0.0, 0.0, 35.0);
        //END #634
        localJFreeChart.getXYPlot().setAxisOffset(offset);
        return localJFreeChart;
    }

    /**
     * Create dataset.
     *
     * @param nNumberInfoId
     * @return
     * @throws Exception
     */
    private XYDataset createDataset(long nNumberInfoId) throws Exception {
        //Get data from DB
        TrafficReportData data = getDataFromDB(nNumberInfoId);
        if (data == null) {
            return null;
        }
        //Create variable
        TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
        //Create time series data
        for (int i = 0; i < data.getSubData().size(); i++) {
            String timeseriesLable = "";
            //START #569
            //Start Step.1x #796
            if ( data.getLocationNumberArray().get(i) != null && !Const.EMPTY.equals(data.getLocationNumberArray().get(i))) {
                //END #569
                //End Step.1x #796
                timeseriesLable = String.format(Const.Chart.SERIES_SUB_LABLE(), data.getLocationNumberArray().get(i));
            } else {
                timeseriesLable = String.format(Const.Chart.SERIES_MAIN_LABLE());
            }

            TimeSeries localTimeSeries = new TimeSeries(timeseriesLable);
            List<TrafficInfo> tempData = data.getSubData().get(i);
            if (tempData.size() < 2) {
                break;
            }
            RegularTimePeriod timePeriod = null;
            for (int j = 0; j < tempData.size(); j++) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(tempData.get(j).getMeasurementDate().getTime());
                timePeriod = new Minute(cal.get(Calendar.MINUTE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));

                localTimeSeries.addOrUpdate(timePeriod, tempData.get(j).getUseNumber());
            }
            localTimeSeriesCollection.addSeries(localTimeSeries);
        }
        //START #569
        return localTimeSeriesCollection;
        //END #569
    }

    /**
     * Get data from DB.
     *
     * @param actionType : type of action (search, next , pre)
     * @return ERROR if process have happened error and null if it is success
     */
    private TrafficReportData getDataFromDB(long nNumberInfoId) {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        TrafficReportData result = new TrafficReportData();

        //Get list location number
        Result<List<String>> resultLocationNumber = DBService.getInstance().getListLocationNumber(loginId, sessionId, nNumberInfoId, startTime, endTime);

        if (resultLocationNumber.getRetCode() == Const.ReturnCode.NG) {
            error = resultLocationNumber.getError();
            return null;
        }
        result.setLocationNumberArray(resultLocationNumber.getData());
        //Get subdata from DB
        //START #560
        Result<TrafficReportData> resultFinal = DBService.getInstance().getSubData(false,loginId, sessionId, resultLocationNumber.getData(), nNumberInfoId, startTime, endTime,0,0);
        //END #560
        if (resultFinal.getRetCode() == Const.ReturnCode.OK) {
            if (resultFinal.getData() != null) {
                result.setSubData(resultFinal.getData().getSubData());
            }
        } else {
            error = resultFinal.getError();
            return null;
        }
        return result;
    }

    /**
     * Check input date.
     * @return true if OK or false if input invalid
     */
    private boolean checkTime() {
        startTime = null;
        endTime = null;
        if (startTimeString != null && startTimeString.compareTo("") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd 00:00:00.000000");

            try {
                //START #608
                Date date = new SimpleDateFormat("yyyy/mm/dd").parse(startTimeString);
                //END #608
                String temp = sdf.format(date);
                startTime = Timestamp.valueOf(temp);
            } catch (Exception e) {
                return false;
            }
        }

        if (endTimeString != null && endTimeString.compareTo("") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd 23:59:59.999999");

            try {
                //START #608
                Date date = new SimpleDateFormat("yyyy/mm/dd").parse(endTimeString);
                //END #608
                String temp = sdf.format(date);
                endTime = Timestamp.valueOf(temp);
            } catch (Exception e) {
                return false;
            }

        }
        if (startTime != null && endTime != null) {
            if (startTime.after(endTime)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Getter of actionType.
     *
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter of actionType.
     *
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter of startTimeString.
     *
     * @return startTimeString
     */
    public String getStartTimeString() {
        return startTimeString;
    }

    /**
     * Setter of startTimeString.
     *
     * @param startTimeString
     */
    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    /**
     * Getter of endTimeString.
     *
     * @return endTimeString
     */
    public String getEndTimeString() {
        return endTimeString;
    }

    /**
     * Setter of endTimeString.
     *
     * @param endTimeString
     */
    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }
}
// (C) NTT Communications  2013  All Rights Reserved
