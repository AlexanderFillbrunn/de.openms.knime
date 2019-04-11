package de.openms.knime.views.spectrum.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A response to the interactive view that contains a chunk of a feature list.
 * @author Alexander Fillbrunn
 *
 */
public class FeaturesChunkedViewResponse implements WrappedSpectrumViewResponse {
    private Feature[] m_features;
    
    /**
     * Instantiates a new <code>FeaturesChunkedViewResponse</code>.
     * @param features the features of the chunk
     */
    public FeaturesChunkedViewResponse(Feature[] features) {
        m_features = features;
    }

    /**
     * @return the features of the chunk
     */
    public Feature[] getFeatures() {
        return m_features;
    }
    
    /**
     * Sets the features of the chunk.
     * @param features array of features
     */
    public void setFeatures(Feature[] features) {
        m_features = features;
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
        FeaturesChunkedViewResponse other = (FeaturesChunkedViewResponse)obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(m_features, other.m_features)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(super.hashCode())
                .append(m_features)
                .toHashCode();
    }
}
