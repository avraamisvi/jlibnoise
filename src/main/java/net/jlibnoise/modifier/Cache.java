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

package net.jlibnoise.modifier;

import net.jlibnoise.Module;
import net.jlibnoise.exception.NoModuleException;

/**
 * Noise module that caches the last output value generated by a source
 * module.
 *
 * If an application passes an input value to the GetValue() method that
 * differs from the previously passed-in input value, this noise module
 * instructs the source module to calculate the output value.  This
 * value, as well as the ( x, y, z ) coordinates of the input
 * value, are stored (cached) in this noise module.
 *
 * If the application passes an input value to the GetValue() method
 * that is equal to the previously passed-in input value, this noise
 * module returns the cached output value without having the source
 * module recalculate the output value.
 *
 * If an application passes a new source module to the setSourceModule()
 * method, the cache is invalidated.
 *
 * Caching a noise module is useful if it is used as a source module for
 * multiple noise modules.  If a source module is not cached, the source
 * module will redundantly calculate the same output value once for each
 * noise module in which it is included.
 *
 * This noise module requires one source module.
 */
public class Cache extends Module {
	// The cached output value at the cached input value.
	double cachedValue;

	// Determines if a cached output value is stored in this noise
	// module.
	boolean isCached = false;

	// x coordinate of the cached input value.
	double xCache;

	// y coordinate of the cached input value.
	double yCache;

	// z coordinate of the cached input value.
	double zCache;

	public Cache() {
		super(1);
	}

	@Override
	public int getSourceModuleCount() {
		return 1;
	}

	@Override
	public void setSourceModule(int index, Module sourceModule) {
		super.setSourceModule(index, sourceModule);
		isCached = false;
	}

	@Override
	public double getValue(double x, double y, double z) {
		if (sourceModule[0] == null)
			throw new NoModuleException();

		if (!(isCached && x == xCache && y == yCache && z == zCache)) {
			cachedValue = sourceModule[0].getValue(x, y, z);
			xCache = x;
			yCache = y;
			zCache = z;
		}
		isCached = true;
		return cachedValue;
	}

}
