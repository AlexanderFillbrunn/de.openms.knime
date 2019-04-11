package de.openms.knime.views.spectrum.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * One intensity at a mz/rt location.
 * @author Alexander Fillbrunn
 *
 */
public class SpectrumPoint {
    private double m_mass;
    private double m_rt;
    private double m_intensity;
    
    /**
     * Instantiates a default <code>SpectrumPoint</code>.
     */
    public SpectrumPoint() {}
    
    /**
     * Instantiates a <code>SpectrumPoint</code> with mass, retention time and intensity.
     * @param mass the mass coordinate
     * @param rt the retention time coordinate
     * @param intensity the intensity at the coordinates
     */
    public SpectrumPoint(double mass, double rt, double intensity) {
        super();
        m_mass = mass;
        m_rt = rt;
        m_intensity = intensity;
    }

    /**
     * @return the mass coordinate
     */
    public double getMass() {
        return m_mass;
    }
    
    /**
     * Sets the mass coordinate
     * @param mass the mass coordinate
     */
    public void setMass(double mass) {
        m_mass = mass;
    }
    
    /**
     * @return the retention time coordinate
     */
    public double getRt() {
        return m_rt;
    }
    
    /**
     * Sets the retention time coordinate.
     * @param rt the retention time coordinate
     */
    public void setRt(double rt) {
        m_rt = rt;
    }
    
    public double getIntensity() {
        return m_intensity;
    }
    
    public void setIntensity(double intensity) {
        m_intensity = intensity;
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
        SpectrumPoint other = (SpectrumPoint)obj;
        return new EqualsBuilder()
                .append(m_mass, other.m_mass)
                .append(m_rt, other.m_rt)
                .append(m_intensity, other.m_intensity)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_mass)
                .append(m_rt)
                .append(m_intensity)
                .toHashCode();
    }
}
