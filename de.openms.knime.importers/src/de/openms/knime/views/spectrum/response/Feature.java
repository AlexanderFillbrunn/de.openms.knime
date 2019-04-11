package de.openms.knime.views.spectrum.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Feature {
    private double m_mzStart;
    private double m_mzEnd;
    private double m_rtStart;
    private double m_rtEnd;
    private double m_intensity;
    private double m_quality;

    /**
     * Instantiates a new <code>Feature</code>.
     * @param mzStart the start of the feature's bounding box on the mz coordinate
     * @param mzEnd the end of the feature's bounding box on the mz coordinate
     * @param rtStart the start of the feature's bounding box on the rt coordinate
     * @param rtEnd  the end of the feature's bounding box on the rt coordinate
     * @param intensity the feature's intensity
     * @param quality the feature's quality
     */
    public Feature(double mzStart, double mzEnd, double rtStart, double rtEnd, double intensity, double quality) {
        super();
        m_mzStart = mzStart;
        m_mzEnd = mzEnd;
        m_rtStart = rtStart;
        m_rtEnd = rtEnd;
        m_intensity = intensity;
        m_quality = quality;
    }
    
    /**
     * Instantiates a default <code>Feature</code>.
     */
    public Feature() {
    }
    
    /**
     * @return the end of the feature's bounding box on the mz coordinate
     */
    public double getMzEnd() {
        return m_mzEnd;
    }
    
    /**
     * @return the start of the feature's bounding box on the mz coordinate
     */
    public double getMzStart() {
        return m_mzStart;
    }
    
    /**
     * Sets the start of the feature's bounding box on the mz coordinate
     * @param mzStart the start of the bounding box
     */
    public void setMzStart(double mzStart) {
        m_mzStart = mzStart;
    }
    
    /**
     * Sets the end of the feature's bounding box on the mz coordinate
     * @param mzEnd the end of the bounding box
     */
    public void setMzEnd(double mzEnd) {
        m_mzEnd = mzEnd;
    }
    
    /**
     * @return the start of the feature's bounding box on the rt coordinate
     */
    public double getRtStart() {
        return m_rtStart;
    }
    
    /**
     * Sets the start of the feature's bounding box on the rt coordinate
     * @param rtStart the start of the bounding box
     */
    public void setRtStart(double rtStart) {
        m_rtStart = rtStart;
    }
    
    /**
     * @return the end of the feature's bounding box on the rt coordinate
     */
    public double getRtEnd() {
        return m_rtEnd;
    }
    
    /**
     * Sets the end of the feature's bounding box on the rt coordinate
     * @param rtEnd the end of the bounding box
     */
    public void setRtEnd(double rtEnd) {
        m_rtEnd = rtEnd;
    }
    
    /**
     * @return the feature's intensity
     */
    public double getIntensity() {
        return m_intensity;
    }
    
    /**
     * Sets the feature's intensity
     * @param intensity the intensity value
     */
    public void setIntensity(double intensity) {
        m_intensity = intensity;
    }
    
    /**
     * @return the feature's quality
     */
    public double getQuality() {
        return m_quality;
    }
    
    /**
     * Sets the feature's quality
     * @param quality the quality value
     */
    public void setQuality(double quality) {
        m_quality = quality;
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
        Feature other = (Feature)obj;
        return new EqualsBuilder()
                .append(m_mzStart, other.m_mzStart)
                .append(m_mzEnd, other.m_mzEnd)
                .append(m_rtEnd, other.m_rtEnd)
                .append(m_rtStart, other.m_rtStart)
                .append(m_intensity, other.m_intensity)
                .append(m_quality, other.m_quality)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_mzStart)
                .append(m_mzEnd)
                .append(m_rtEnd)
                .append(m_rtStart)
                .append(m_intensity)
                .append(m_quality)
                .toHashCode();
    }
}
