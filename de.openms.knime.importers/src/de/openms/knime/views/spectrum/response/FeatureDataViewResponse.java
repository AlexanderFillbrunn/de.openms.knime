package de.openms.knime.views.spectrum.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Wrapped response to the interactive client.
 * @author Alexander Fillbrunn
 *
 */
public class FeatureDataViewResponse implements WrappedSpectrumViewResponse {
    private SpectrumPoint[] m_points;
    
    /**
     * Instantiates a new <code>FeatureDataViewResponse</code> with intensity points.
     * @param points the intensity points of the feature
     */
    public FeatureDataViewResponse(SpectrumPoint[] points) {
        m_points = points;
    }
    
    /**
     * @return the intensity points contained in the feature.
     */
    public SpectrumPoint[] getPoints() {
        return m_points;
    }
    
    /**
     * Sets the feature's intensity points contained in the feature.
     * @param points an array of intensity points
     */
    public void setPoints(SpectrumPoint[] points) {
        m_points = points;
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
        FeatureDataViewResponse other = (FeatureDataViewResponse)obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(m_points, other.m_points)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(super.hashCode())
                .append(m_points)
                .toHashCode();
    }
}
