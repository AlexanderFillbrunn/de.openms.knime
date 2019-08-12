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

import java.sql.SQLType;

<<<<<<< HEAD
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

=======
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
import org.knime.core.data.DoubleValue;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
<<<<<<< HEAD
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.database.datatype.mapping.DBTypeMappingRegistry;
import org.knime.database.datatype.mapping.DBTypeMappingService;
=======
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.database.datatype.mapping.DBTypeMappingRegistry;
import org.knime.database.datatype.mapping.DBTypeMappingService;
import org.knime.database.node.component.sqleditor.DBDataTypeMappingConfigurationSupplier;
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
import org.knime.database.port.DBSessionPortObjectSpec;
import org.knime.database.session.DBSession;
import org.knime.datatype.mapping.DataTypeMappingConfiguration;
import org.knime.datatype.mapping.DataTypeMappingDirection;
import org.knime.node.datatype.mapping.DialogComponentDataTypeMapping;

/**
 *
 * @author Alexander Fillbrunn
 */
public class SpectrumViewNodeDialog extends DefaultNodeSettingsPane {

    SpectrumViewConfig m_config = new SpectrumViewConfig();
    DialogComponentDataTypeMapping<SQLType> m_mapping;
    
    public SpectrumViewNodeDialog() {
<<<<<<< HEAD
=======
        DialogComponentBoolean hideInWizard = new DialogComponentBoolean(m_config.getHideInWizardSettingsModel(),
                "Hide in wizard");
        addDialogComponent(hideInWizard);

>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
        createNewGroup("Coordinate columns");
        setHorizontalPlacement(true);
        DialogComponentColumnNameSelection mzStartCol = new DialogComponentColumnNameSelection(
                m_config.getMzStartColumnSettingsModel(), "MZ Start column", 0, DoubleValue.class);
        addDialogComponent(mzStartCol);
        
        DialogComponentColumnNameSelection mzEndCol = new DialogComponentColumnNameSelection(
                m_config.getMzEndColumnSettingsModel(), "MZ End column", 0, DoubleValue.class);
        addDialogComponent(mzEndCol);
        
        setHorizontalPlacement(false);
        setHorizontalPlacement(true);
        
        DialogComponentColumnNameSelection rtStartCol = new DialogComponentColumnNameSelection(
                m_config.getRtStartColumnSettingsModel(), "RT start column", 0, DoubleValue.class);
        addDialogComponent(rtStartCol);
        
        DialogComponentColumnNameSelection rtEndCol = new DialogComponentColumnNameSelection(
                m_config.getRtEndColumnSettingsModel(), "RT end column", 0, DoubleValue.class);
        addDialogComponent(rtEndCol);
        
        closeCurrentGroup();
<<<<<<< HEAD

=======
        
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
        DialogComponentColumnNameSelection intensityCol = new DialogComponentColumnNameSelection(
                m_config.getIntensityColumnSettingsModel(), "Intensity column", 0, DoubleValue.class);
        addDialogComponent(intensityCol);
        
        DialogComponentColumnNameSelection qualityCol = new DialogComponentColumnNameSelection(
                m_config.getQualityColumnSettingsModel(), "Quality column", 0, DoubleValue.class);
        addDialogComponent(qualityCol);
        
        setHorizontalPlacement(false);
        
        DialogComponentString tableName = new DialogComponentString(m_config.getTableNameSettingsModel(), "Table name");
        addDialogComponent(tableName);
        
<<<<<<< HEAD
        createNewTab("View Settings");
        
        DialogComponentBoolean hideInWizard = new DialogComponentBoolean(m_config.getHideInWizardSettingsModel(),
                "Hide in wizard");
        addDialogComponent(hideInWizard);
        
        DialogComponentBoolean subscribeToSelection =
                new DialogComponentBoolean(m_config.getSubscribeToSelectionSettingsModel(), "Subscribe to selection");
        addDialogComponent(subscribeToSelection);
        
        setHorizontalPlacement(true);
        
        DialogComponentBoolean allowPanAndZoom = new DialogComponentBoolean(m_config.getAllowPanAndZoomSettingsModel(), "Allow panning and zooming");
        addDialogComponent(allowPanAndZoom);
        
        DialogComponentBoolean allowRectZoom = new DialogComponentBoolean(m_config.getAllowRectZoomSettingsModel(), "Allow rectangle zooming");
        addDialogComponent(allowRectZoom);
        
        setHorizontalPlacement(false);
        setHorizontalPlacement(true);
        
        DialogComponentButtonGroup zoomMode = new DialogComponentButtonGroup(m_config.getZoomModeSettingsModel(), false, "Zoom Mode", SpectrumViewConfig.ZOOM_MODES);
        addDialogComponent(zoomMode);
        
        DialogComponentButtonGroup colorMode = new DialogComponentButtonGroup(m_config.getColorModeSettingsModel(), false, "Color Mode", SpectrumViewConfig.COLOR_MODES);
        addDialogComponent(colorMode);
        
        setHorizontalPlacement(false);
        createNewGroup("Viewport");
        
        SettingsModelBoolean useCustomBoundsSM = m_config.getUseCustomViewBoundsSettingsModel();
        DialogComponentBoolean useCustomBounds = new DialogComponentBoolean(useCustomBoundsSM, "Use custom view bounds");
        addDialogComponent(useCustomBounds);
        
        setHorizontalPlacement(true);
        DialogComponentNumber minMz = new DialogComponentNumber(m_config.getMinMzSettingsModel(), "Minimum MZ", 1.0);
        addDialogComponent(minMz);
        
        DialogComponentNumber maxMz = new DialogComponentNumber(m_config.getMaxMzSettingsModel(), "Maximum MZ", 1.0);
        addDialogComponent(maxMz);
        
        setHorizontalPlacement(false);
        setHorizontalPlacement(true);
        
        DialogComponentNumber minRt = new DialogComponentNumber(m_config.getMinRtSettingsModel(), "Minimum RT", 1.0);
        addDialogComponent(minRt);
        
        DialogComponentNumber maxRt = new DialogComponentNumber(m_config.getMaxRtSettingsModel(), "Maximum RT", 1.0);
        addDialogComponent(maxRt);
        closeCurrentGroup();
        
        useCustomBoundsSM.addChangeListener((e) -> {
            m_config.getMinMzSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
            m_config.getMaxMzSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
            m_config.getMinRtSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
            m_config.getMaxRtSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
        });

=======
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
        createNewTab("Type Mapping");
        m_mapping = new DialogComponentDataTypeMapping(
                SpectrumViewNodeModel.createExternalToKnimeMappingModel());
        addDialogComponent(m_mapping);
    }
    
    @Override
    public void loadAdditionalSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
            throws NotConfigurableException {
        super.loadAdditionalSettingsFrom(settings, specs);
<<<<<<< HEAD
        
        SettingsModelBoolean useCustomBoundsSM = m_config.getUseCustomViewBoundsSettingsModel();
        m_config.getMinMzSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
        m_config.getMaxMzSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
        m_config.getMinRtSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
        m_config.getMaxRtSettingsModel().setEnabled(useCustomBoundsSM.getBooleanValue());
        
        final DBSessionPortObjectSpec dbPortSpec = (DBSessionPortObjectSpec)specs[1];

        if (dbPortSpec != null) {
            final DBSession session = dbPortSpec.getDBSession();
=======
        final DBSessionPortObjectSpec dbPortSpec = (DBSessionPortObjectSpec)specs[1];

        final DBSession session;
        final DBDataTypeMappingConfigurationSupplier externalToKnimeSupplier;
        if (dbPortSpec != null) {
            session = dbPortSpec.getDBSession();
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6

            final DBTypeMappingService<?, ?> mappingService =
                DBTypeMappingRegistry.getInstance().getDBTypeMappingService(session.getDBType());
            m_mapping.setMappingService(mappingService);

            final DataTypeMappingConfiguration<SQLType> externalToKnime;
            try {
                externalToKnime = dbPortSpec.getExternalToKnimeTypeMapping().resolve(mappingService,
                    DataTypeMappingDirection.EXTERNAL_TO_KNIME);
            } catch (final InvalidSettingsException e) {
                throw new NotConfigurableException(e.getMessage(), e);
            }
            m_mapping.setInputDataTypeMappingConfiguration(externalToKnime);
<<<<<<< HEAD
=======

            externalToKnimeSupplier = () -> externalToKnime.with(
                    m_mapping.getDataTypeMappingModel().getDataTypeMappingConfiguration(mappingService));
        } else {
            session = null;
            externalToKnimeSupplier = null;
>>>>>>> 09812e6732b1ce92aae8a710afd46d2635b511f6
        }
        m_mapping.loadSettingsFrom(settings, specs);
    }
}
