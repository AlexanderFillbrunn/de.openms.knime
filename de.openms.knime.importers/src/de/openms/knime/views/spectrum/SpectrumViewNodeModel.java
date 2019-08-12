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

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.container.DataContainer;
import org.knime.core.data.filestore.FileStoreFactory;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.interactive.ViewRequestHandlingException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.database.SQLQuery;
import org.knime.database.agent.reader.DBReader;
import org.knime.database.agent.reader.ReaderFunction;
import org.knime.database.datatype.mapping.DBTypeMappingRegistry;
import org.knime.database.datatype.mapping.DBTypeMappingService;
import org.knime.database.node.datatype.mapping.SettingsModelDatabaseDataTypeMapping;
import org.knime.database.port.DBSessionPortObject;
import org.knime.database.session.DBSession;
import org.knime.datatype.mapping.DataTypeMappingConfiguration;
import org.knime.datatype.mapping.DataTypeMappingDirection;
import org.knime.js.core.JSONViewRequestHandler;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.openms.knime.views.spectrum.SpectrumViewViewRequest.RequestType;
import de.openms.knime.views.spectrum.response.Feature;
import de.openms.knime.views.spectrum.response.FeatureDataViewResponse;
import de.openms.knime.views.spectrum.response.FeaturesChunkedViewResponse;
import de.openms.knime.views.spectrum.response.SpectrumPoint;
import de.openms.knime.views.spectrum.response.SpectrumViewViewResponse;

/**
 *
 * @author Christian Albrecht, KNIME GmbH, Konstanz, Germany
 */
public class SpectrumViewNodeModel
        extends AbstractWizardNodeModel<SpectrumViewViewRepresentation, SpectrumViewViewValue>
        implements JSONViewRequestHandler<SpectrumViewViewRequest, SpectrumViewViewResponse> {

    private static final int FEATURES_PER_BATCH = 5000;
    
    private final SpectrumViewConfig m_config;

    private final SettingsModelDatabaseDataTypeMapping m_externalToKnime = createExternalToKnimeMappingModel();

    private DBSessionPortObject m_database;

    private BufferedDataTable m_featureTable;
    private RowToFeatureIterator m_currentIter;

    /**
     * Returns the external to KNIME {@link SettingsModelDatabaseDataTypeMapping}.
     *
     * @return the {@link SettingsModelDatabaseDataTypeMapping}
     */
    static SettingsModelDatabaseDataTypeMapping createExternalToKnimeMappingModel() {
        return new SettingsModelDatabaseDataTypeMapping("external_to_knime_mapping",
                DataTypeMappingDirection.EXTERNAL_TO_KNIME);
    }

    /**
     * Instantiates a new <code>SpectrumViewNodeModel</code>.
     * 
     * @param viewName
     *            the name of the interactive view
     */
    public SpectrumViewNodeModel(final String viewName) {
        super(new PortType[] { BufferedDataTable.TYPE, DBSessionPortObject.TYPE_OPTIONAL }, new PortType[] {},
                viewName);
        m_config = new SpectrumViewConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpectrumViewViewRequest createEmptyViewRequest() {
        return new SpectrumViewViewRequest();
    }

    private SpectrumViewViewResponse handleFeaturesRequest(final SpectrumViewViewRequest request,
            final ExecutionMonitor exec) {
        SpectrumViewViewResponse response = new SpectrumViewViewResponse(request);
        if (m_currentIter == null) {
            m_currentIter = createFeatureIterator(m_featureTable);
        }
        List<Feature> chunk = new ArrayList<>();
        int count = 0;
        while (m_currentIter.hasNext() && count < FEATURES_PER_BATCH) {
            chunk.add(m_currentIter.next());
            count++;
        }
        if (count == 0) {
            m_currentIter = null;
        }

        response.setResponse(new FeaturesChunkedViewResponse(chunk.toArray(new Feature[0])));
        return response;
    }

    private SpectrumViewViewResponse handleFeatureDataRequest(final SpectrumViewViewRequest request,
            final ExecutionMonitor exec) throws ViewRequestHandlingException {
        try {
            // Query the connected database
            DataTable dt = queryDB(request.getRtStart(), request.getRtEnd(), request.getMzStart(), request.getMzEnd(),
                    exec);
            DataTableSpec spec = dt.getDataTableSpec();
            int timeIdx = spec.findColumnIndex("rt");
            int massIdx = spec.findColumnIndex("mz");
            int intensityIdx = spec.findColumnIndex("intensity");
            // Extract points
            List<SpectrumPoint> pts = new ArrayList<>();
            for (DataRow row : dt) {
                double time = ((DoubleValue) row.getCell(timeIdx)).getDoubleValue();
                double mass = ((DoubleValue) row.getCell(massIdx)).getDoubleValue();
                double intensity = ((DoubleValue) row.getCell(intensityIdx)).getDoubleValue();
                SpectrumPoint p = new SpectrumPoint(mass, time, intensity);
                pts.add(p);
            }
            SpectrumViewViewResponse resp = new SpectrumViewViewResponse(request);
            resp.setResponse(new FeatureDataViewResponse(pts.toArray(new SpectrumPoint[0])));
            return resp;
        } catch (Throwable e) {
            throw new ViewRequestHandlingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpectrumViewViewResponse handleRequest(final SpectrumViewViewRequest request, final ExecutionMonitor exec)
            throws ViewRequestHandlingException, InterruptedException, CanceledExecutionException {
        if (request.getType() == RequestType.FEATURES) {
            return handleFeaturesRequest(request, exec);
        } else if (request.getType() == RequestType.FEATURE_DATA) {
            return handleFeatureDataRequest(request, exec);
        } else {
            throw new ViewRequestHandlingException("Invalid request type");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpectrumViewViewRepresentation createEmptyViewRepresentation() {
        return new SpectrumViewViewRepresentation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpectrumViewViewValue createEmptyViewValue() {
        return new SpectrumViewViewValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJavascriptObjectID() {
        return "de.openms.knime.views.spectrumView";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHideInWizard() {
        return m_config.isHideInWizard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHideInWizard(final boolean hide) {
        m_config.setHideInWizard(hide);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationError validateViewValue(final SpectrumViewViewValue viewContent) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveCurrentValue(final NodeSettingsWO content) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        return new PortObjectSpec[] {};
    }

    private DataTable queryDB(double rtStart, double rtEnd, double mzStart, double mzEnd, final ExecutionMonitor exec)
            throws Throwable {
        DBSession dbSession = m_database.getDBSession();
        final DBReader reader = dbSession.getAgent(DBReader.class);
        final ReaderFunction<DataTable> function = rowOutput -> {
            final DataContainer container = new DataContainer(rowOutput.getKNIMETableSpec());
            while (rowOutput.hasNext()) {
                container.addRowToTable(rowOutput.next());
            }
            container.close();
            return container.getTable();
        };

        SQLQuery query = new SQLQuery(String.format(Locale.ENGLISH,
                "SELECT * FROM %s WHERE rt >= %f AND rt <= %f AND mz >= %f AND mz <= %f",
                m_config.getTableName(), rtStart, rtEnd, mzStart, mzEnd));
        
        final DBTypeMappingService<?, ?> mappingService =
                DBTypeMappingRegistry.getInstance().getDBTypeMappingService(dbSession.getDBType());
        
        final DataTypeMappingConfiguration<SQLType> externalToKnime = m_database.getExternalToKnimeTypeMapping()
                .resolve(mappingService, DataTypeMappingDirection.EXTERNAL_TO_KNIME)
                .with(m_externalToKnime.getDataTypeMappingConfiguration(mappingService));
        try {
            final FileStoreFactory fsf = FileStoreFactory.createNotInWorkflowFileStoreFactory();
            return reader.read(exec, fsf, function, query, externalToKnime);
        } catch (CanceledExecutionException | InvalidSettingsException | IOException | SQLException error) {
            // TODO
            throw error;
        }
    }

    private RowToFeatureIterator createFeatureIterator(BufferedDataTable table) {
        DataTableSpec featureSpec = table.getSpec();
        int rtStartCol = featureSpec.findColumnIndex(m_config.getRtStartColumn());
        int rtEndCol = featureSpec.findColumnIndex(m_config.getRtEndColumn());
        int mzStartCol = featureSpec.findColumnIndex(m_config.getMzStartColumn());
        int mzEndCol = featureSpec.findColumnIndex(m_config.getMzEndColumn());
        int qualityCol = featureSpec.findColumnIndex(m_config.getQualityColumn());
        int intensityCol = featureSpec.findColumnIndex(m_config.getIntensityColumn());
        return new RowToFeatureIterator(table.iterator(), rtStartCol, rtEndCol, mzStartCol, mzEndCol, intensityCol,
                qualityCol);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] performExecute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
        m_featureTable = (BufferedDataTable) inObjects[0];
        SpectrumViewViewRepresentation rep = getViewRepresentation();
        SpectrumViewViewValue val = getViewValue();
        
        DataTableSpec spec = m_featureTable.getSpec();
        DataColumnSpec mzStartSpec = spec.getColumnSpec(m_config.getMzStartColumn());
        DataColumnSpec mzEndSpec = spec.getColumnSpec(m_config.getMzEndColumn());
        DataColumnSpec rtStartSpec = spec.getColumnSpec(m_config.getRtStartColumn());
        DataColumnSpec rtEndSpec = spec.getColumnSpec(m_config.getRtEndColumn());
        
        if (mzStartSpec == null) {
            throw new InvalidSettingsException("No valid mz start column given");
        }
        if (mzEndSpec == null) {
            throw new InvalidSettingsException("No valid mz end column given");
        }
        if (rtStartSpec == null) {
            throw new InvalidSettingsException("No valid rt start column given");
        }
        if (rtEndSpec == null) {
            throw new InvalidSettingsException("No valid rt end column given");
        }
        
        rep.setMinMz((int) Math.floor(((DoubleValue) mzStartSpec.getDomain().getLowerBound()).getDoubleValue()));
        rep.setMaxMz((int) Math.ceil(((DoubleValue) mzEndSpec.getDomain().getUpperBound()).getDoubleValue()));
        rep.setMinRt((int) Math.floor(((DoubleValue) rtStartSpec.getDomain().getLowerBound()).getDoubleValue()));
        rep.setMaxRt((int) Math.ceil(((DoubleValue) rtEndSpec.getDomain().getUpperBound()).getDoubleValue()));
        
        rep.setRectZoomAllowed(m_config.isRectZoomAllowed());
        rep.setPanAndZoomAllowed(m_config.isPanAndZoomAllowed());

        if (inObjects[1] != null) {
            m_database = (DBSessionPortObject) inObjects[1];
            rep.setHasDB(true);
        } else {
            rep.setHasDB(false);
        }
        rep.setTableId(getInHiLiteHandler(0).getHiliteHandlerID().toString());
        
        if (isViewValueEmpty()) {
            val.setSubscribeToSelection(m_config.getSubscribeToSelection());
            val.setMinMz(m_config.getMinMz());
            val.setMaxMz(m_config.getMaxMz());
            val.setMinRt(m_config.getMinRt());
            val.setMaxRt(m_config.getMaxRt());
            val.setUseCustomBounds(m_config.getUseCustomViewBounds());
            val.setColorMode(m_config.getColorMode());
            val.setZoomMode(m_config.getZoomMode());
        }
        
        return new PortObject[] {};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performReset() {
        m_featureTable = null;
        m_currentIter = null;
        m_database = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void useCurrentValueAsDefault() {
        synchronized (getLock()) {
            copyViewValueToConfig();
        }
    }

    private void copyViewValueToConfig() {
        final SpectrumViewViewValue viewValue = getViewValue();
        m_config.setSubscribeToSelection(viewValue.getSubscribeToSelection());
        m_config.setUseCustomViewBounds(viewValue.getUseCustomBounds());
        m_config.setMinMz(viewValue.getMinMz());
        m_config.setMaxMz(viewValue.getMaxMz());
        m_config.setMinRt(viewValue.getMinRt());
        m_config.setMaxRt(viewValue.getMaxRt());
        m_config.setColorMode(viewValue.getColorMode());
        m_config.setZoomMode(viewValue.getZoomMode());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        m_config.saveToSettings(settings);
        m_externalToKnime.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        (new SpectrumViewConfig()).loadFromSettings(settings);
        m_externalToKnime.validateSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_config.loadFromSettings(settings);
        m_externalToKnime.loadSettingsFrom(settings);
    }

}
