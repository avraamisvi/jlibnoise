/* Copyright (C) 2011 Garrett Fleenor

 This library is free software; you can redistribute it and/or modify it
 under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation; either version 3.0 of the License, or (at
 your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 License (COPYING.txt) for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 This is a port of libnoise ( http://libnoise.sourceforge.net/index.html ).  Original implementation by Jason Bevins

*/

package net.jlibnoise.model;

import net.jlibnoise.Module;
import net.jlibnoise.exception.NoModuleException;

/**
 * Model that defines the surface of a plane.
 * 
 * This model returns an output value from a noise module given the coordinates
 * of an input value located on the surface of an ( @a x,
 * 
 * @a z ) plane.
 * 
 *    To generate an output value, pass the ( @a x, @a z ) coordinates of an
 *    input value to the GetValue() method.
 * 
 *    This model is useful for creating: - two-dimensional textures - terrain
 *    height maps for local areas
 * 
 *    This plane extends infinitely in both directions.
 */
public class Plane {
	Module module;

	/**
	 * Constructor
	 * 
	 * @param module The noise module that is used to generate the output
	 *            values.
	 */
	public Plane(Module module) {
		if (module == null)
			throw new IllegalArgumentException("module cannot be null");
		this.module = module;
	}

	/**
	 * Returns the noise module that is used to generate the output values.
	 * 
	 * @returns A reference to the noise module.
	 * @pre A noise module was passed to the SetModule() method.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Sets the noise module that is used to generate the output values.
	 * 
	 * @param module The noise module that is used to generate the output
	 *            values.
	 * 
	 *            This noise module must exist for the lifetime of this object,
	 *            until you pass a new noise module to this method.
	 */
	public void setModule(Module module) {
		if (module == null)
			throw new IllegalArgumentException("module cannot be null");
		this.module = module;
	}

	/**
	 * Returns the output value from the noise module given the ( @a x, @a z )
	 * coordinates of the specified input value located on the surface of the
	 * plane.
	 * 
	 * @param x The @a x coordinate of the input value.
	 * @param z The @a z coordinate of the input value.
	 * @return The output value from the noise module.
	 * @pre A noise module was passed to the SetModule() method.
	 * 
	 *      This output value is generated by the noise module passed to the
	 *      SetModule() method.
	 */
	double getValue(double x, double z) {
		if (module == null)
			throw new NoModuleException();
		return module.getValue(x, 0, z);
	}
}
