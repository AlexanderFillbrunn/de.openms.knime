package de.openms.knime.views.spectrum;

import java.io.Closeable;
import java.util.Iterator;

import org.knime.core.data.DataRow;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.container.CloseableRowIterator;

import de.openms.knime.views.spectrum.response.Feature;

/**
 * An iterator that turns data rows into instances of <code>Feature</code> on the fly.
 * @author Alexander Fillbrunn
 *
 */
public class RowToFeatureIterator implements Iterator<Feature>, Closeable {

    CloseableRowIterator m_iter;
    int m_rtStartCol;
    int m_rtEndCol;
    int m_mzStartCol;
    int m_mzEndCol;
    int m_intensityCol;
    int m_qualityCol;
    
    /**
     * Instantiates a new <code>RowToFeatureIterator</code>.
     * @param iter the underlying row iterator
     * @param rtStartCol the column representing the start of the rt interval
     * @param rtEndCol the column representing the end of the rt interval
     * @param mzStartCol the column representing the start of the mz interval
     * @param mzEndCol the column representing the end of the mz interval
     * @param intensityCol the feature's intensity
     * @param qualityCol the feature's quality
     */
    public RowToFeatureIterator(CloseableRowIterator iter, int rtStartCol, int rtEndCol, int mzStartCol, int mzEndCol, int intensityCol,
            int qualityCol) {
        super();
        m_iter = iter;
        m_rtStartCol = rtStartCol;
        m_rtEndCol = rtEndCol;
        m_mzStartCol = mzStartCol;
        m_mzEndCol = mzEndCol;
        m_intensityCol = intensityCol;
        m_qualityCol = qualityCol;
    }

    @Override
    public boolean hasNext() {
        return m_iter.hasNext();
    }

    @Override
    public Feature next() {
        DataRow row = m_iter.next();
        double rtStart = ((DoubleValue)row.getCell(m_rtStartCol)).getDoubleValue();
        double rtEnd = ((DoubleValue)row.getCell(m_rtEndCol)).getDoubleValue();
        double mzStart = ((DoubleValue)row.getCell(m_mzStartCol)).getDoubleValue();
        double mzEnd = ((DoubleValue)row.getCell(m_mzEndCol)).getDoubleValue();
        double intensity = ((DoubleValue)row.getCell(m_intensityCol)).getDoubleValue();
        double quality = ((DoubleValue)row.getCell(m_qualityCol)).getDoubleValue();
        return new Feature(mzStart, mzEnd, rtStart, rtEnd, intensity, quality);
    }
    
    /**
     * Closes the underlying <code>CloseableRowIterator</code>.
     * This is only necessary if not the whole table is iterated.
     */
    public void close() {
        m_iter.close();
    }
}
