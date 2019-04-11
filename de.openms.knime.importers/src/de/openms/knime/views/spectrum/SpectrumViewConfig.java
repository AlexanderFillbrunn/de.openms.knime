/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   4 Apr 2018 (albrecht): created
 */
package de.openms.knime.views.spectrum;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 *
 * @author Alexander Fillbrunn
 */
public class SpectrumViewConfig {

    private static final String CFG_HIDE = "hideInWizard";
    private static final String CFG_MZ_START_COLUMN = "mzStartCol";
    private static final String CFG_MZ_END_COLUMN = "mzEndCol";
    private static final String CFG_RT_START = "rtStartCol";
    private static final String CFG_RT_END = "rtEndCol";
    private static final String CFG_INTENSITY = "intensity";
    private static final String CFG_QUALITY = "quality";
    private static final String CFG_TABLE_NAME = "tableName";
    
    private SettingsModelBoolean m_hideInWizard = createHideInWizardSettingsModel();
    private SettingsModelColumnName m_mzStartColumn = createMzStartColumnSettingsModel();
    private SettingsModelColumnName m_mzEndColumn = createMzEndColumnSettingsModel();
    private SettingsModelColumnName m_rtStartColumn = createRtStartColumnSettingsModel();
    private SettingsModelColumnName m_rtEndColumn = createRtEndColumnSettingsModel();
    private SettingsModelColumnName m_intensityColumn = createIntensityColumnSettingsModel();
    private SettingsModelColumnName m_qualityColumn = createQualityColumnSettingsModel();
    private SettingsModelString m_tableName = createTableNameSettingsModel();

    private static SettingsModelString createTableNameSettingsModel() {
        return new SettingsModelString(CFG_TABLE_NAME, "");
    }
    
    private static SettingsModelBoolean createHideInWizardSettingsModel() {
        return new SettingsModelBoolean(CFG_HIDE, false);
    }

    private static SettingsModelColumnName createMzStartColumnSettingsModel() {
        return new SettingsModelColumnName(CFG_MZ_START_COLUMN, null);
    }
    
    private static SettingsModelColumnName createMzEndColumnSettingsModel() {
        return new SettingsModelColumnName(CFG_MZ_END_COLUMN, null);
    }
    
    private static SettingsModelColumnName createRtEndColumnSettingsModel() {
        return new SettingsModelColumnName(CFG_RT_END, null);
    }
    
    private static SettingsModelColumnName createRtStartColumnSettingsModel() {
        return new SettingsModelColumnName(CFG_RT_START, null);
    }
    
    private static SettingsModelColumnName createIntensityColumnSettingsModel() {
        return new SettingsModelColumnName(CFG_INTENSITY, null);
    }
    
    private static SettingsModelColumnName createQualityColumnSettingsModel() {
        return new SettingsModelColumnName(CFG_QUALITY, null);
    }
    
    public SettingsModelString getTableNameSettingsModel() {
        return m_tableName;
    }
    
    public SettingsModelBoolean getHideInWizardSettingsModel() {
        return m_hideInWizard;
    }
    
    public SettingsModelColumnName getMzStartColumnSettingsModel() {
        return m_mzStartColumn;
    }
    
    public SettingsModelColumnName getMzEndColumnSettingsModel() {
        return m_mzEndColumn;
    }
    
    public SettingsModelColumnName getRtStartColumnSettingsModel() {
        return m_rtStartColumn;
    }
    
    public SettingsModelColumnName getRtEndColumnSettingsModel() {
        return m_rtEndColumn;
    }
    
    public SettingsModelColumnName getIntensityColumnSettingsModel() {
        return m_intensityColumn;
    }
    
    public SettingsModelColumnName getQualityColumnSettingsModel() {
        return m_qualityColumn;
    }
    
    public String getTableName() {
        return m_tableName.getStringValue();
    }
    
    /**
     * @return the hideInWizard
     */
    public boolean isHideInWizard() {
        return m_hideInWizard.getBooleanValue();
    }

    public String getMzStartColumn() {
        return m_mzStartColumn.getColumnName();
    }
    
    public String getMzEndColumn() {
        return m_mzEndColumn.getColumnName();
    }
    
    public String getRtStartColumn() {
        return m_rtStartColumn.getColumnName();
    }

    public String getRtEndColumn() {
        return m_rtEndColumn.getColumnName();
    }

    public String getIntensityColumn() {
        return m_intensityColumn.getColumnName();
    }

    public String getQualityColumn() {
        return m_qualityColumn.getColumnName();
    }

    void saveToSettings(final NodeSettingsWO settings) {
        m_hideInWizard.saveSettingsTo(settings);
        m_mzStartColumn.saveSettingsTo(settings);
        m_mzEndColumn.saveSettingsTo(settings);
        m_rtEndColumn.saveSettingsTo(settings);
        m_rtStartColumn.saveSettingsTo(settings);
        m_intensityColumn.saveSettingsTo(settings);
        m_qualityColumn.saveSettingsTo(settings);
        m_tableName.saveSettingsTo(settings);
    }

    void loadFromSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_hideInWizard.loadSettingsFrom(settings);
        m_mzStartColumn.loadSettingsFrom(settings);
        m_mzEndColumn.loadSettingsFrom(settings);
        m_rtEndColumn.loadSettingsFrom(settings);
        m_rtStartColumn.loadSettingsFrom(settings);
        m_intensityColumn.loadSettingsFrom(settings);
        m_qualityColumn.loadSettingsFrom(settings);
        m_tableName.loadSettingsFrom(settings);
    }

    public void setHideInWizard(boolean hide) {
        m_hideInWizard.setBooleanValue(hide);
    }
    
    public void setMzStartColumn(String colName) {
        m_mzStartColumn.setStringValue(colName);
    }
    
    public void setMzEndColumn(String colName) {
        m_mzEndColumn.setStringValue(colName);
    }
    
    public void setRtStartColumn(String colName) {
        m_rtStartColumn.setStringValue(colName);
    }
    
    public void setRtEndColumn(String colName) {
        m_rtEndColumn.setStringValue(colName);
    }
    
    public void setIntensityColumn(String colName) {
        m_intensityColumn.setStringValue(colName);
    }
    
    public void setQualityColumn(String colName) {
        m_qualityColumn.setStringValue(colName);
    }
    
    public void setTableName(String tableName) {
        m_tableName.setStringValue(tableName);
    }
}
