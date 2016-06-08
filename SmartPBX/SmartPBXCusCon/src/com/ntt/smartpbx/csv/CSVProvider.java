package com.ntt.smartpbx.csv;

import java.util.Vector;

/**
 * 名称: CSVProvider interface
 * 機能概要: Define common method for an Action to handle CSV import/export.
 */
public interface CSVProvider {
    /**
     * Get the CSV data.
     *
     * @return The CSV data.
     */
    @SuppressWarnings("rawtypes")
    public Vector getCSVData();

    /**
     * Get the CSV file name.
     *
     * @return The CSV data.
     */
    public String getCSVFileName();

    /**
     * Get the CSV headers.
     *
     * @return The CSV headers.
     */
    public String[] getCSVHeaders();
}

//(C) NTT Communications  2013  All Rights Reserved
