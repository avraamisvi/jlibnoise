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

package net.jlibnoise;

import net.jlibnoise.exception.NoModuleException;

/**
 * Abstract base class for noise modules.
 *
 * A <i>noise module</i> is an object that calculates and outputs a value
 * given a three-dimensional input value.
 *
 * Each type of noise module uses a specific method to calculate an
 * output value.  Some of these methods include:
 *
 * - Calculating a value using a coherent-noise function or some other
 *   mathematical function.
 * - Mathematically changing the output value from another noise module
 *   in various ways.
 * - Combining the output values from two noise modules in various ways.
 *
 * An application can use the output values from these noise modules in
 * the following ways:
 *
 * - It can be used as an elevation value for a terrain height map
 * - It can be used as a grayscale (or an RGB-channel) value for a
 *   procedural texture
 * - It can be used as a position value for controlling the movement of a
 *   simulated lifeform.
 *
 * A noise module defines a near-infinite 3-dimensional texture.  Each
 * position in this "texture" has a specific value.
 *
 * <b>Combining noise modules</b>
 *
 * Noise modules can be combined with other noise modules to generate
 * complex output values.  A noise module that is used as a source of
 * output values for another noise module is called a <i>source
 * module</i>.  Each of these source modules may be connected to other
 * source modules, and so on.
 *
 * There is no limit to the number of noise modules that can be connected
 * together in this way.  However, each connected noise module increases
 * the time required to calculate an output value.
 *
 * <b>Noise-module categories</b>
 *
 * The noise module classes that are included in libnoise can be roughly
 * divided into five categories.
 *
 * <i>Generator Modules</i>
 *
 * A generator module outputs a value generated by a coherent-noise
 * function or some other mathematical function.
 *
 * Examples of generator modules include:
 * - {@link Constant} Outputs a constant value.
 * - {@link Perlin} Outputs a value generated by a Perlin-noise function.
 * - {@link Voronoi} Outputs a value generated by a Voronoi-cell function.
 *
 * <i>Modifier Modules</i>
 *
 * A modifer module mathematically modifies the output value from a
 * source module.
 *
 * Examples of modifier modules include:
 * - {@link Curve} Maps the output value from the source module
 *   onto an arbitrary function curve.
 * - {@link Invert} Inverts the output value from the source
 *   module.
 *
 * <i>Combiner Modules</i>
 *
 * A combiner module mathematically combines the output values from two
 * or more source modules together.
 *
 * Examples of combiner modules include:
 * - {@link Add} Adds the two output values from two source
 *   modules.
 * - {@link Max} Outputs the larger of the two output values from
 *   two source modules.
 *
 * <i>Selector Modules</i>
 *
 * A selector module uses the output value from a <i>control module</i>
 * to specify how to combine the output values from its source modules.
 *
 * Examples of selector modules include:
 * - {@link Blend} Outputs a value that is linearly interpolated
 *   between the output values from two source modules; the interpolation
 *   weight is determined by the output value from the control module.
 * - {@link Select} Outputs the value selected from one of two
 *   source modules chosen by the output value from a control module.
 *
 * <i>Transformer Modules</i>
 *
 * A transformer module applies a transformation to the coordinates of
 * the input value before retrieving the output value from the source
 * module.  A transformer module does not modify the output value.
 *
 * Examples of transformer modules include:
 * - RotatePoint: Rotates the coordinates of the input value around the
 *   origin before retrieving the output value from the source module.
 * - ScalePoint: Multiplies each coordinate of the input value by a
 *   constant value before retrieving the output value from the source
 *   module.
 *
 * <b>Connecting source modules to a noise module</b>
 *
 * An application connects a source module to a noise module by passing
 * the source module to the SetSourceModule() method.
 *
 * The application must also pass an <i>index value</i> to
 * getSourceModule() as well.  An index value is a numeric identifier for
 * that source module.  Index values are consecutively numbered starting
 * at zero.
 *
 * To retrieve a reference to a source module, pass its index value to
 * the getSourceModule() method.
 *
 * Each noise module requires the attachment of a certain number of
 * source modules before it can output a value.  For example, the
 * {@link Add} module requires two source modules, while the
 * {@link Perlin} module requires none.  Call the
 * getSourceModuleCount() method to retrieve the number of source modules
 * required by that module.
 *
 * For non-selector modules, it usually does not matter which index value
 * an application assigns to a particular source module, but for selector
 * modules, the purpose of a source module is defined by its index value.
 * For example, consider the {@link Select} noise module, which
 * requires three source modules.  The control module is the source
 * module assigned an index value of 2.  The control module determines
 * whether the noise module will output the value from the source module
 * assigned an index value of 0 or the output value from the source
 * module assigned an index value of 1.
 *
 * <b>Generating output values with a noise module</b>
 *
 * Once an application has connected all required source modules to a
 * noise module, the application can now begin to generate output values
 * with that noise module.
 *
 * To generate an output value, pass the ( @a x, @a y, @a z ) coordinates
 * of an input value to the GetValue() method.
 *
 * <b>Using a noise module to generate terrain height maps or textures</b>
 *
 * One way to generate a terrain height map or a texture is to first
 * allocate a 2-dimensional array of floating-point values.  For each
 * array element, pass the array subscripts as @a x and @a y coordinates
 * to the getValue() method (leaving the @a z coordinate set to zero) and
 * place the resulting output value into the array element.
 *
 * <b>Creating your own noise modules</b>
 *
 * Create a class that publicly derives from noise::module::Module.
 *
 * In the constructor, call the base class' constructor while passing the
 * return value from getSourceModuleCount() to it.
 *
 * Override the getSourceModuleCount() pure virtual method.  From this
 * method, return the number of source modules required by your noise
 * module.
 *
 * Override the getValue() pure virtual method.  For generator modules,
 * calculate and output a value given the coordinates of the input value.
 * For other modules, retrieve the output values from each source module
 * referenced in the protected sourceModule array, mathematically
 * combine those values, and return the combined value.
 *
 * When developing a noise module, you must ensure that your noise module
 * does not modify any source module or control module connected to it; a
 * noise module can only modify the output value from those source
 * modules.  You must also ensure that if an application fails to connect
 * all required source modules via the setSourceModule() method and then
 * attempts to call the getValue() method, your module will raise an
 * assertion.
 *
 * It shouldn't be too difficult to create your own noise module.  If you
 * still have some problems, take a look at the source code for
 * {@link Add}, which is a very simple noise module.
 */
public abstract class Module {
	protected Module[] sourceModule;

	public Module(int sourceModuleCount) {
		sourceModule = null;

		// Create an array of pointers to all source modules required by this
		// noise module.  Set these pointers to NULL.
		if (sourceModuleCount > 0) {
			sourceModule = new Module[sourceModuleCount];
			for (int i = 0; i < sourceModuleCount; i++) {
				sourceModule[i] = null;
			}
		} else {
			sourceModule = null;
		}

	}

    /**
     * Returns a reference to a source module connected to this noise module.
     * <p/>
     * Each noise module requires the attachment of a certain number of
     * source modules before an application can call the getValue()
     * method.
     *
     * @param index The index value assigned to the source module.
     * @return A reference to the source module.
     * @throws NoModuleException See the preconditions for more information.
     * @pre The index value ranges from 0 to one less than the number of
     * source modules required by this noise module.
     * @pre A source module with the specified index value has been added
     * to this noise module via a call to SetSourceModule().
     */
	public Module getSourceModule(int index) {
		if (index >= getSourceModuleCount() || index < 0 || sourceModule[index] == null) {
			throw new NoModuleException();
		}
		return (sourceModule[index]);

	}

    /**
     * Connects a source module to this noise module.
     *
     * A noise module mathematically combines the output values from the
     * source modules to generate the value returned by getValue().
     *
     * The index value to assign a source module is a unique identifier
     * for that source module.  If an index value has already been
     * assigned to a source module, this noise module replaces the old
     * source module with the new source module.
     *
     * Before an application can call the GetValue() method, it must
     * first connect all required source modules.  To determine the
     * number of source modules required by this noise module, call the
     * getSourceModuleCount() method.
     *
     * This source module must exist throughout the lifetime of this
     * noise module unless another source module replaces that source
     * module.
     *
     * A noise module does not modify a source module; it only modifies
     * its output values.
     * @param index An index value to assign to this source module.
     * @param sourceModule The source module to attach.
     * @throws IllegalArgumentException An invalid parameter was
     * specified; see the preconditions for more information.
     * @pre The index value ranges from 0 to one less than the number of
     * source modules required by this noise module.
     */
	public void setSourceModule(int index, Module sourceModule) {
		if (this.sourceModule == null)
			return;
		if (index >= getSourceModuleCount() || index < 0) {
			throw new IllegalArgumentException("Index must be between 0 and getSourceModuleCount()");
		}
		this.sourceModule[index] = sourceModule;
	}

    /**
     * Returns the number of source modules required by this noise
     * module.
     *
     * @return The number of source modules required by this noise module.
     */
	public abstract int getSourceModuleCount();

    /**
     * Generates an output value given the coordinates of the specified
     * input value.
     * <p/>
     * Before an application can call this method, it must first connect
     * all required source modules via the SetSourceModule() method.  If
     * these source modules are not connected to this noise module, this
     * method raises a debug assertion.
     * <p/>
     * To determine the number of source modules required by this noise
     * module, call the getSourceModuleCount() method.
     *
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @return The output value.
     * @pre All source modules required by this noise module have been
     * passed to the SetSourceModule() method.
     */
	public abstract double getValue(double x, double y, double z);
}
