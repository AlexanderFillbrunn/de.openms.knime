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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import de.openms.knime.views.spectrum.SpectrumViewConfig.ColorMode;
import de.openms.knime.views.spectrum.SpectrumViewConfig.ZoomMode;

/**
 *
 * @author Christian Albrecht, KNIME GmbH, Konstanz, Germany
 */
public class SpectrumViewViewValue extends JSONViewContent {

    private static final String CFG_SUBSCRIBE_TO_SELECTION = "subscribeToSelection";
    private static final String CFG_MIN_MZ = "minMz";
    private static final String CFG_MAX_MZ = "maxMz";
    private static final String CFG_MIN_RT = "minRt";
    private static final String CFG_MAX_RT = "maxRt";
    private static final String CFG_USE_CUSTOM_BOUNDS = "useCustomBounds";
    private static final String CFG_ZOOM_MODE = "zoomMode";
    private static final String CFG_COLOR_MODE = "colorMode";
    
    private boolean m_subscribeToSelection;
    private double m_minMz;
    private double m_maxMz;
    private double m_minRt;
    private double m_maxRt;
    private boolean m_useCustomBounds;
    private ZoomMode m_zoomMode;
    private ColorMode m_colorMode;
    

    public ZoomMode getZoomMode() {
        return m_zoomMode;
    }
    
    public ColorMode getColorMode() {
        return m_colorMode;
    }
    
    public boolean getUseCustomBounds() {
        return m_useCustomBounds;
    }
    
    public double getMinMz() {
        return m_minMz;
    }

    public void setMinMz(double minMz) {
        m_minMz = minMz;
    }

    public double getMaxMz() {
        return m_maxMz;
    }

    public void setMaxMz(double maxMz) {
        m_maxMz = maxMz;
    }

    public double getMinRt() {
        return m_minRt;
    }

    public void setMinRt(double minRt) {
        m_minRt = minRt;
    }

    public double getMaxRt() {
        return m_maxRt;
    }

    public void setMaxRt(double maxRt) {
        m_maxRt = maxRt;
    }

    public boolean getSubscribeToSelection() {
        return m_subscribeToSelection;
    }
    
    public void setSubscribeToSelection(boolean subscribeToSelection) {
        m_subscribeToSelection = subscribeToSelection;
    }
    
    public void setUseCustomBounds(boolean useCustomBounds) {
        m_useCustomBounds = useCustomBounds;
    }
    
    public void setZoomMode(ZoomMode zoomMode) {
        m_zoomMode = zoomMode;
    }
    
    public void setColorMode(ColorMode colorMode) {
        m_colorMode = colorMode;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToNodeSettings(final NodeSettingsWO settings) {
        settings.addBoolean(CFG_SUBSCRIBE_TO_SELECTION, m_subscribeToSelection);
        settings.addDouble(CFG_MIN_MZ, m_minMz);
        settings.addDouble(CFG_MAX_MZ, m_maxMz);
        settings.addDouble(CFG_MIN_RT, m_minRt);
        settings.addDouble(CFG_MAX_RT, m_maxRt);
        settings.addBoolean(CFG_USE_CUSTOM_BOUNDS, m_useCustomBounds);
        settings.addString(CFG_ZOOM_MODE, m_zoomMode.name());
        settings.addString(CFG_COLOR_MODE, m_colorMode.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFromNodeSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_subscribeToSelection = settings.getBoolean(CFG_SUBSCRIBE_TO_SELECTION);
        m_minMz = settings.getDouble(CFG_MIN_MZ);
        m_maxMz = settings.getDouble(CFG_MAX_MZ);
        m_minRt = settings.getDouble(CFG_MIN_RT);
        m_maxRt = settings.getDouble(CFG_MAX_RT);
        m_useCustomBounds = settings.getBoolean(CFG_USE_CUSTOM_BOUNDS);
        m_zoomMode = ZoomMode.valueOf(settings.getString(CFG_ZOOM_MODE));
        m_colorMode = ColorMode.valueOf(settings.getString(CFG_COLOR_MODE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SpectrumViewViewValue other = (SpectrumViewViewValue)obj;
        return new EqualsBuilder()
                .append(m_subscribeToSelection, other.m_subscribeToSelection)
                .append(m_minMz, other.m_minMz)
                .append(m_maxMz, other.m_maxMz)
                .append(m_minRt, other.m_minRt)
                .append(m_maxRt, other.m_maxRt)
                .append(m_useCustomBounds, other.m_useCustomBounds)
                .append(m_zoomMode, other.m_zoomMode)
                .append(m_colorMode, other.m_colorMode)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_subscribeToSelection)
                .append(m_minMz)
                .append(m_maxMz)
                .append(m_minRt)
                .append(m_maxRt)
                .append(m_useCustomBounds)
                .append(m_zoomMode)
                .append(m_colorMode)
                .toHashCode();
    }

}
