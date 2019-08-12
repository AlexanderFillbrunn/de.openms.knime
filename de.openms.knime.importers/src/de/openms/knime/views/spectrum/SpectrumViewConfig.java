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
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * Class holding the configuration for the view.
 * @author Alexander Fillbrunn
 */
public class SpectrumViewConfig {
    
    public static final String[] ZOOM_MODES = {
            "Pan and Zoom", "Rectangle"
    };
    
    public static final String[] COLOR_MODES = {
            "Intensity", "Quality"
    };
    
    public enum ZoomMode {
        PAN_AND_ZOOM,
        RECTANGLE
    }
    
    public enum ColorMode {
        INTENSITY,
        QUALITY
    }

    // Config keys for saving the object to settings
    private static final String CFG_HIDE = "hideInWizard";
    private static final String CFG_MZ_START_COLUMN = "mzStartCol";
    private static final String CFG_MZ_END_COLUMN = "mzEndCol";
    private static final String CFG_RT_START = "rtStartCol";
    private static final String CFG_RT_END = "rtEndCol";
    private static final String CFG_INTENSITY = "intensity";
    private static final String CFG_QUALITY = "quality";
    private static final String CFG_TABLE_NAME = "tableName";
    private static final String CFG_SUBSCRIBE_TO_SELECTION = "subscribeToSelection";
    private static final String CFG_ALLOW_PAN_ZOOM = "allowPanAndZoom";
    private static final String CFG_ALLOW_RECTANGLE_ZOOM = "allowRectZoom";
    private static final String CFG_MIN_MZ = "minMZ";
    private static final String CFG_MAX_MZ = "maxMZ";
    private static final String CFG_MIN_RT = "minRT";
    private static final String CFG_MAX_RT = "maxRT";
    private static final String CFG_USE_CUSTOM_VIEW = "useCustomView";
    private static final String CFG_ZOOM_MODE = "zoomMode";
    private static final String CFG_COLOR_MODE = "colorMode";
    
    // The settings models
    
    private SettingsModelBoolean m_hideInWizard = createHideInWizardSettingsModel();
    private SettingsModelColumnName m_mzStartColumn = createMzStartColumnSettingsModel();
    private SettingsModelColumnName m_mzEndColumn = createMzEndColumnSettingsModel();
    private SettingsModelColumnName m_rtStartColumn = createRtStartColumnSettingsModel();
    private SettingsModelColumnName m_rtEndColumn = createRtEndColumnSettingsModel();
    private SettingsModelColumnName m_intensityColumn = createIntensityColumnSettingsModel();
    private SettingsModelColumnName m_qualityColumn = createQualityColumnSettingsModel();
    private SettingsModelString m_tableName = createTableNameSettingsModel();
    private SettingsModelBoolean m_subscribeToSelection = createSubscribeToSelectionSettingsModel();
    private SettingsModelBoolean m_allowPanAndZoom = createAllowPanAndZoomSettingsModel();
    private SettingsModelBoolean m_allowRectZoom = createAllowRectZoomSettingsModel();
    private SettingsModelDouble m_minMz = createMinMzSettingsModel();
    private SettingsModelDouble m_maxMz = createMaxMzSettingsModel();
    private SettingsModelDouble m_minRt = createMinRtSettingsModel();
    private SettingsModelDouble m_maxRt = createMaxRtSettingsModel();
    private SettingsModelBoolean m_useCustomViewBounds = createUseCustomViewBoundsSettingsModel();
    private SettingsModelString m_zoomMode = createZoomModeSettingsModel();
    private SettingsModelString m_colorMode = createColorModeSettingsModel();
    
    private SettingsModelBoolean createUseCustomViewBoundsSettingsModel() {
        return new SettingsModelBoolean(CFG_USE_CUSTOM_VIEW, false);
    }
    
    private SettingsModelString createZoomModeSettingsModel() {
        return new SettingsModelString(CFG_ZOOM_MODE, ZOOM_MODES[0]);
    }

    private SettingsModelString createColorModeSettingsModel() {
        return new SettingsModelString(CFG_COLOR_MODE, COLOR_MODES[0]);
    }

    private SettingsModelDouble createMinMzSettingsModel() {
        return new SettingsModelDouble(CFG_MIN_MZ, Double.NEGATIVE_INFINITY);
    }
    
    private SettingsModelDouble createMaxMzSettingsModel() {
        return new SettingsModelDouble(CFG_MAX_MZ, Double.POSITIVE_INFINITY);
    }
    
    private SettingsModelDouble createMinRtSettingsModel() {
        return new SettingsModelDouble(CFG_MIN_RT, Double.NEGATIVE_INFINITY);
    }
    
    private SettingsModelDouble createMaxRtSettingsModel() {
        return new SettingsModelDouble(CFG_MAX_RT, Double.POSITIVE_INFINITY);
    }
    
    private SettingsModelBoolean createAllowRectZoomSettingsModel() {
        return new SettingsModelBoolean(CFG_ALLOW_RECTANGLE_ZOOM, true);
    }
    
    private SettingsModelBoolean createAllowPanAndZoomSettingsModel() {
        return new SettingsModelBoolean(CFG_ALLOW_PAN_ZOOM, true);
    }
    
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
    
    private static SettingsModelBoolean createSubscribeToSelectionSettingsModel() {
        return new SettingsModelBoolean(CFG_SUBSCRIBE_TO_SELECTION, true);
    }
    
    /**
     * @return the settings model for the zoom mode
     */
    public SettingsModelString getZoomModeSettingsModel() {
        return m_zoomMode;
    }
    
    /**
     * @return the settings model for the color mode
     */
    public SettingsModelString getColorModeSettingsModel() {
        return m_colorMode;
    }
    
    /**
     * @return the settings model for whether the bounds of the view are given by the user
     */
    public SettingsModelBoolean getUseCustomViewBoundsSettingsModel() {
        return m_useCustomViewBounds;
    }
    
    /**
     * @return the settings model for whether the user is allowed to zoom by drawing a rectangle
     */
    public SettingsModelBoolean getAllowRectZoomSettingsModel() {
        return m_allowRectZoom;
    }
    
    /**
     * @return the settings model for the minimum mz value shown in the view
     */
    public SettingsModelDouble getMinMzSettingsModel() {
        return m_minMz;
    }
    
    /**
     * @return the settings model for the maximum mz value shown in the view
     */
    public SettingsModelDouble getMaxMzSettingsModel() {
        return m_maxMz;
    }
    
    /**
     * @return the settings model for the minimum rt value shown in the view
     */
    public SettingsModelDouble getMinRtSettingsModel() {
        return m_minRt;
    }
    
    /**
     * @return the settings model for the maximum rt value shown in the view
     */
    public SettingsModelDouble getMaxRtSettingsModel() {
        return m_maxRt;
    }
    
    /**
     * @return the settings model for allowing panning and zooming
     */
    public SettingsModelBoolean getAllowPanAndZoomSettingsModel() {
        return m_allowPanAndZoom;
    }
    /**
     * @return the settings model determining whether the view
     *         subscribes to selection events
     */
    public SettingsModelBoolean getSubscribeToSelectionSettingsModel() {
        return m_subscribeToSelection;
    }
    
    /**
     * @return the settings model for the name of the table with the intensities
     *         in the database that is connected to the node
     */
    public SettingsModelString getTableNameSettingsModel() {
        return m_tableName;
    }
    
    /**
     * @return the settings model determining whether the view is hidden in the wizard
     */
    public SettingsModelBoolean getHideInWizardSettingsModel() {
        return m_hideInWizard;
    }
    
    /**
     * @return the settings model for the column name of the column
     *         holding the start of a feature's bounding box on the mz axis
     */
    public SettingsModelColumnName getMzStartColumnSettingsModel() {
        return m_mzStartColumn;
    }
    
    /**
     * @return the settings model for the column name of the column
     *         holding the end of a feature's bounding box on the mz axis
     */
    public SettingsModelColumnName getMzEndColumnSettingsModel() {
        return m_mzEndColumn;
    }
    
    /**
     * @return the settings model for the column name of the column
     *         holding the start of a feature's bounding box on the rt axis
     */
    public SettingsModelColumnName getRtStartColumnSettingsModel() {
        return m_rtStartColumn;
    }
    
    /**
     * @return the settings model for the column name of the column
     *         holding the end of a feature's bounding box on the rt axis
     */
    public SettingsModelColumnName getRtEndColumnSettingsModel() {
        return m_rtEndColumn;
    }
    
    /**
     * @return the settings model for the name of the intensity column
     */
    public SettingsModelColumnName getIntensityColumnSettingsModel() {
        return m_intensityColumn;
    }
    
    /**
     * @return the settings model for the name of the quality column
     */
    public SettingsModelColumnName getQualityColumnSettingsModel() {
        return m_qualityColumn;
    }
    
    /**
     * @return the default zoom mode in the view
     */
    public ZoomMode getZoomMode() {
        String val = m_zoomMode.getStringValue();
        if (val.equals(ZOOM_MODES[0])) {
            return ZoomMode.PAN_AND_ZOOM;
        } else {
            return ZoomMode.RECTANGLE;
        }
    }
    
    /**
     * @return the default coloring mode for features in the view
     */
    public ColorMode getColorMode() {
        String val = m_colorMode.getStringValue();
        if (val.equals(COLOR_MODES[0])) {
            return ColorMode.INTENSITY;
        } else {
            return ColorMode.QUALITY;
        }
    }
    
    /**
     * @return whether the view uses the view bounds specified by the user
     */
    public boolean getUseCustomViewBounds() {
        return m_useCustomViewBounds.getBooleanValue();
    }
    
    /**
     * @return whether panning and zooming the view is allowed
     */
    public boolean isPanAndZoomAllowed() {
        return m_allowPanAndZoom.getBooleanValue();
    }
    
    /**
     * @return the minimum mz value shown in the view
     */
    public double getMinMz() {
        return m_minMz.getDoubleValue();
    }
    
    /**
     * @return the maximum mz value shown in the view
     */
    public double getMaxMz() {
        return m_maxMz.getDoubleValue();
    }
    
    /**
     * @return the minimum rt value shown in the view
     */
    public double getMinRt() {
        return m_minRt.getDoubleValue();
    }
    
    /**
     * @return the maximum rt value shown in the view
     */
    public double getMaxRt() {
        return m_maxRt.getDoubleValue();
    }
    
    /**
     * @return whether the view subscribes to selection events
     */
    public boolean getSubscribeToSelection() {
        return m_subscribeToSelection.getBooleanValue();
    }
    
    /**
     * @return the name of the table containing intensity info in the connected database
     */
    public String getTableName() {
        return m_tableName.getStringValue();
    }
    
    /**
     * @return whether the view is hidden in the wizard
     */
    public boolean isHideInWizard() {
        return m_hideInWizard.getBooleanValue();
    }

    /**
     * @return the column determining the start of a feature's bounding box on the mz axis
     */
    public String getMzStartColumn() {
        return m_mzStartColumn.getColumnName();
    }
    
    /**
     * @return the column determining the end of a feature's bounding box on the mz axis
     */
    public String getMzEndColumn() {
        return m_mzEndColumn.getColumnName();
    }
    
    /**
     * @return the column determining the start of a feature's bounding box on the rt axis
     */
    public String getRtStartColumn() {
        return m_rtStartColumn.getColumnName();
    }

    /**
     * @return the column determining the end of a feature's bounding box on the rt axis
     */
    public String getRtEndColumn() {
        return m_rtEndColumn.getColumnName();
    }

    /**
     * @return the column with a feature's intensity
     */
    public String getIntensityColumn() {
        return m_intensityColumn.getColumnName();
    }

    /**
     * @return the column with a feature's quality
     */
    public String getQualityColumn() {
        return m_qualityColumn.getColumnName();
    }
    
    /**
     * @return whether the user is allowed to zoom by drawing a rectangle
     */
    public boolean isRectZoomAllowed() {
        return m_allowRectZoom.getBooleanValue();
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
        m_subscribeToSelection.saveSettingsTo(settings);
        m_allowPanAndZoom.saveSettingsTo(settings);
        m_allowRectZoom.saveSettingsTo(settings);
        m_minMz.saveSettingsTo(settings);
        m_maxMz.saveSettingsTo(settings);
        m_minRt.saveSettingsTo(settings);
        m_maxRt.saveSettingsTo(settings);
        m_useCustomViewBounds.saveSettingsTo(settings);
        m_zoomMode.saveSettingsTo(settings);
        m_colorMode.saveSettingsTo(settings);
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
        m_subscribeToSelection.loadSettingsFrom(settings);
        m_allowPanAndZoom.loadSettingsFrom(settings);
        m_allowRectZoom.loadSettingsFrom(settings);
        m_minMz.loadSettingsFrom(settings);
        m_maxMz.loadSettingsFrom(settings);
        m_minRt.loadSettingsFrom(settings);
        m_maxRt.loadSettingsFrom(settings);
        m_useCustomViewBounds.loadSettingsFrom(settings);
        m_zoomMode.loadSettingsFrom(settings);
        m_colorMode.loadSettingsFrom(settings);
    }
    
    /**
     * Sets the zoom mode for the view.
     * @param mode the zoom mode to use
     */
    public void setZoomMode(ZoomMode mode) {
        if (mode == ZoomMode.PAN_AND_ZOOM) {
            m_zoomMode.setStringValue(ZOOM_MODES[0]);
        } else {
            m_zoomMode.setStringValue(ZOOM_MODES[1]);
        }
    }
    
    /**
     * Sets the color mode for features in the view.
     * @param mode the color mode to use
     */
    public void setColorMode(ColorMode mode) {
        if (mode == ColorMode.INTENSITY) {
            m_colorMode.setStringValue(COLOR_MODES[0]);
        } else {
            m_colorMode.setStringValue(COLOR_MODES[1]);
        }
    }
    
    /**
     * Sets whether the view bounds are given by the user.
     * @param useCustomViewBounds true if the view bounds given in the configuration should be used
     *            in the view.
     */
    public void setUseCustomViewBounds(boolean useCustomViewBounds) {
        m_useCustomViewBounds.setBooleanValue(useCustomViewBounds);
    }
    
    /**
     * Sets whether zooming by drawing a rectangle is allowed.
     * @param rectZoomAllowed whether rectangle zooming is allowed
     */
    public void setRectZoomAllowed(boolean rectZoomAllowed) {
        m_allowRectZoom.setBooleanValue(rectZoomAllowed);
    }
    
    /**
     * Sets the minimum value on the mz-axis in the view.
     * @param minMz the minimum mz value
     */
    public void setMinMz(double minMz) {
        m_minMz.setDoubleValue(minMz);
    }
    
    /**
     * Sets the maximum value on the mz-axis in the view.
     * @param maxMz the maximum mz value
     */
    public void setMaxMz(double maxMz) {
        m_maxMz.setDoubleValue(maxMz);
    }
    
    /**
     * Sets the minimum value on the rt-axis in the view.
     * @param minRt the minimum rt value
     */
    public void setMinRt(double minRt) {
        m_minRt.setDoubleValue(minRt);
    }
    
    /**
     * Sets the maximum value on the rt-axis in the view.
     * @param maxRt the maximum rt value
     */
    public void setMaxRt(double maxRt) {
        m_maxRt.setDoubleValue(maxRt);
    }
    
    /**
     * Sets whether panning and zooming is allowed in the view
     * @param panAndZoomAllowed true if panning and zooming is allowed, false otherwise
     */
    public void setPanAndZoomAllowed(boolean panAndZoomAllowed) {
        m_allowPanAndZoom.setBooleanValue(panAndZoomAllowed);
    }
    
    /**
     * Sets whether the view subscribes to selection events
     * @param subscribeToSelection true if the view subscribes to selection events
     */
    public void setSubscribeToSelection(boolean subscribeToSelection) {
        m_subscribeToSelection.setBooleanValue(subscribeToSelection);
    }

    /**
     * Sets whether the view should be visible in the wizard
     * @param hide true if the view should not be visible in the wizard
     */
    public void setHideInWizard(boolean hide) {
        m_hideInWizard.setBooleanValue(hide);
    }
    
    /**
     * Sets the column name of the column determining a feature's interval start on
     * the mz axis
     * 
     * @param colName the name of the column to use
     */
    public void setMzStartColumn(String colName) {
        m_mzStartColumn.setStringValue(colName);
    }
    
    /**
     * Sets the column name of the column determining a feature's interval end on
     * the mz axis
     * 
     * @param colName the name of the column to use
     */
    public void setMzEndColumn(String colName) {
        m_mzEndColumn.setStringValue(colName);
    }
    
    /**
     * Sets the column name of the column determining a feature's interval start on
     * the rt axis
     * 
     * @param colName the name of the column to use
     */
    public void setRtStartColumn(String colName) {
        m_rtStartColumn.setStringValue(colName);
    }
    
    /**
     * Sets the column name of the column determining a feature's interval end on
     * the rt axis
     * 
     * @param colName the name of the column to use
     */
    public void setRtEndColumn(String colName) {
        m_rtEndColumn.setStringValue(colName);
    }
    
    /**
     * Sets the name of the column containing a feature's intensity
     * 
     * @param colName the name of the column to use
     */
    public void setIntensityColumn(String colName) {
        m_intensityColumn.setStringValue(colName);
    }
    
    /**
     * Sets the name of the column containing a feature's quality
     * 
     * @param colName the name of the column to use
     */
    public void setQualityColumn(String colName) {
        m_qualityColumn.setStringValue(colName);
    }
    
    /**
     * Sets the name of the table in the database that contains the intensity
     * for a combination of retention time and mass over charge
     * 
     * @param tableName the name of the table in the database
     */
    public void setTableName(String tableName) {
        m_tableName.setStringValue(tableName);
    }
}
