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

/**
 *
 * @author Christian Albrecht, KNIME GmbH, Konstanz, Germany
 */
public class SpectrumViewViewRepresentation extends JSONViewContent {

    private static final String CFG_MAX_RT = "maxrt";
    private static final String CFG_MIN_RT = "minrt";
    private static final String CFG_MAX_MZ = "maxmz";
    private static final String CFG_MIN_MZ = "minmz";
    private static final String CFG_HAS_DB = "hasDB";
    private static final String CFG_TABLE_ID = "tableId";
    
    private int m_maxRt;
    private int m_minRt;
    private int m_maxMz;
    private int m_minMz;
    private boolean m_hasDB;
    private int m_tableId;
    
    public int getTableId() {
        return m_tableId;
    }
    
    public void setTableId(int tableId) {
        m_tableId = tableId;
    }
    
    public boolean isHasDB() {
        return m_hasDB;
    }
    
    public void setHasDB(boolean hasDB) {
        m_hasDB = hasDB;
    }
    
    public int getMaxRt() {
        return m_maxRt;
    }

    public void setMaxRt(int maxRt) {
        m_maxRt = maxRt;
    }

    public int getMinRt() {
        return m_minRt;
    }

    public void setMinRt(int inRt) {
        m_minRt = inRt;
    }

    public int getMaxMz() {
        return m_maxMz;
    }

    public void setMaxMz(int maxMz) {
        m_maxMz = maxMz;
    }

    public int getMinMz() {
        return m_minMz;
    }

    public void setMinMz(int minMz) {
        m_minMz = minMz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToNodeSettings(final NodeSettingsWO settings) {
        settings.addInt(CFG_MAX_MZ, m_maxMz);
        settings.addInt(CFG_MIN_MZ, m_minMz);
        settings.addInt(CFG_MAX_RT, m_maxRt);
        settings.addInt(CFG_MIN_RT, m_minRt);
        settings.addBoolean(CFG_HAS_DB, m_hasDB);
        settings.addInt(CFG_TABLE_ID, m_tableId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFromNodeSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_maxMz = settings.getInt(CFG_MAX_MZ);
        m_minMz = settings.getInt(CFG_MIN_MZ);
        m_maxRt = settings.getInt(CFG_MAX_RT);
        m_minRt = settings.getInt(CFG_MIN_RT);
        m_hasDB = settings.getBoolean(CFG_HAS_DB);
        m_tableId = settings.getInt(CFG_TABLE_ID);
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
        SpectrumViewViewRepresentation other = (SpectrumViewViewRepresentation)obj;
        return new EqualsBuilder()
                .append(m_tableId, other.m_tableId)
                .append(m_maxMz, other.m_maxMz)
                .append(m_minMz, other.m_minMz)
                .append(m_maxRt, other.m_maxRt)
                .append(m_minRt, other.m_minRt)
                .append(m_hasDB, other.m_hasDB)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_tableId)
                .append(m_maxMz)
                .append(m_minMz)
                .append(m_maxRt)
                .append(m_minRt)
                .append(m_hasDB)
                .toHashCode();
    }

}
